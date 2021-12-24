import axios from "axios";
import {barra_org} from "@/api/utils";
import utils from "@/api/utils";

function formato_imagenes(imagenes){
    let dar_formato = imagen => ({name: imagen.name, type: imagen.type, content: imagen.content()})
    return imagenes.map(dar_formato)
}

function defaultHandler(error){
    switch (error.response.status){
        case 500: {
            alert('Explotó una computadora del labo de medrano\nal querer enviar'+ error.response.config.url)
            break
        }
        default: {
            alert('Excepcion no manejada\n Error: ' + error.response.data.error)
            throw error
        }
    }
}

const manejarErrorNotificacion = err => {
    const res = err.response
    if(res.status === 503){
        if(!res.data.mensaje_enviado){
            throw err
        }
    } else{
        defaultHandler(err)
    }
}

export default {
    getOrganizaciones(){
        return axios.get('/api/organizaciones')
            .then(res => res.data)

    },

    getOrganizacion(idOrg){
        return axios.get(barra_org(idOrg))
            .then(res=>res.data)
            .catch(defaultHandler)
    },

    getHogaresDeTransito(){
        return axios.get('/api/hogares')
            .then(res => res.data)

    },

    getMisMascotas(idOrg){
        return axios.get(barra_org(idOrg) + "/mis-mascotas")
            .then(res => res.data)
            .catch(error => {
                switch (error.response.status){
                    case 401:
                        return []
                }

                defaultHandler(error)
            }
        )
    },

    getMascotasPerdidas(idOrg) {
       return axios.get(barra_org(idOrg)+'/mascotas-perdidas')
            .then(res => res.data)
            .catch(defaultHandler)
    },

    getMascotasEnAdopcion(idOrg) {
           return axios.get(barra_org(idOrg)+'/catalogo-adopcion')
                .then(res => res.data)
                .catch(defaultHandler)
        },

    getMascotasRescatadas(idOrg) {
        return axios.get(barra_org(idOrg)+'/mascotas-rescatadas')
            .then(res => res.data)
            .catch(defaultHandler)
    }
    , generarPublicacion(idOrg, respuestas) {
        return axios.post(barra_org(idOrg)+'/adoptantes', {preferencias: respuestas})
            .then(res => res.data)
            .catch(err => {
                switch (err.response.status){
                    case 400:
                        utils.throw_error('Las preguntas no se respondieron correctamente')
                        break
                    default:
                        defaultHandler(err)
                }
            })

    }

    , darEnAdopcion(idOrg, mascota, respuestas) {
        return axios.post(barra_org(idOrg)+'/catalogo-adopcion', {id_mascota: parseInt(mascota.id), respuestas: respuestas})
            .then(res => res.data)
            .catch(err => {
                switch (err.response.status){
                    case 400:
                        utils.throw_error(err.response.data.error)
                        break
                    default:
                        defaultHandler(err)
                }
            })
    },

    crearMascota(idOrg, mascota, perdida, fotos){
        mascota.situacion = perdida? 'PERDIDA':'EN_CASA'

        return axios.post( barra_org(idOrg)+'/mis-mascotas', {mascota: mascota, fotos: formato_imagenes(fotos)})
            .then(res => res.data.mascota)
            .catch(defaultHandler)
    },
    agregarCaracteristica(idOrg, caracteristica) {
        return axios.post(barra_org(idOrg)+'/caracteristicas-pedidas', caracteristica)
            .then(res => res.data)
            .catch(defaultHandler)
    },

    agregarPregunta(idOrg, caracteristica) {
        return axios.post(barra_org(idOrg)+'/preguntas-adoptantes', caracteristica)
            .then(res => res.data)
            .catch(defaultHandler)
    },
    setImageParams(idOrg, imagenes) {
        return axios.put(barra_org(idOrg)+'/imageparams', {imagenes})
            .then(res => res.data)
            .catch(error => {
                switch (error.response.status){
                    case 400: {
                        utils.throw_error(error.response.data.error)
                        break
                    }

                    default: {
                        defaultHandler(error)
                    }
                }
            })
    },
    agregarMascotaEncontrada(idOrg, mascota, fotos) {
        return axios.post(barra_org(idOrg)+'/mascotas-rescatadas', {mascota: mascota, fotos: formato_imagenes(fotos)})
            .then(res => res.data)
            .catch(error => {
                switch (error.response.status) {
                    case 400: {
                        utils.throw_error(error.response.data.error)
                        break
                    }
                    default: {
                        defaultHandler(error)
                    }
                }
            }
        )
    },
    getPreguntasAdoptantes(idOrg) {
        return axios.get(barra_org(idOrg)+'/preguntas-adoptantes')
            .then(res => res.data)
            .then(preguntas => preguntas.comunes.concat(preguntas.propias))
            .catch(error => defaultHandler(error))
    },

    getUsuarios(idOrg){
        return axios.get(barra_org(idOrg)+'/usuarios')
            .then(res => res.data)
            .catch(defaultHandler)
    }
    , hacerAdmin(idOrg, nombreNuevoAdmin) {
        return axios.post(barra_org(idOrg)+'/administradores', {nuevo_admin: nombreNuevoAdmin})
            .then(res => res.data)
            .catch(e => this.manejarErrorDeAsignacionDePermisos(e, nombreNuevoAdmin,'administrador'))
    },

    hacerVoluntario(idOrg, nombre){
        return axios.post(barra_org(idOrg)+'/voluntarios', {voluntario: nombre})
            .then(res => res.data)
            .catch(e => this.manejarErrorDeAsignacionDePermisos(e, nombre, 'voluntario'))
    }
    , manejarErrorDeAsignacionDePermisos: (err, nombre, rol) => {
        let mensaje = ''
        switch (err.response.status){
            case 404:
                mensaje = 'No se encontró ningún usuario con el nombre '+nombre
                break
            case 409:
                mensaje = 'El usuario '+nombre+' ya tiene permisos de '+rol
                break
            default: {
                defaultHandler(err)
            }
        }

        throw new Error(mensaje)
    },
    reportarSituacionDeMascota( idOrg, mascota, nuevaSituacion) {
        switch (nuevaSituacion){
            case 'PERDIDA': {
                return axios.post(barra_org(idOrg)+'/mascotas-perdidas', {id_mascota: mascota.id})
                    .then(res=>res.data.mascota)
                    .catch(defaultHandler)
            }
            case 'EN_CASA':{
                return axios.put(barra_org(idOrg)+'/mis-mascotas/'+mascota.id)
                    .then(res=>res.data.mascota)
                    .catch(defaultHandler)
            }

            default:
                alert('Se leyo situacion desconocida: '+nuevaSituacion)
        }
    },

    adoptarMascota(idOrg, publicacion){
        return axios.post(barra_org(idOrg)+'/catalogo-adopcion/'+publicacion.id+'/solicitudes')
            .then(res => res.data)
            .catch(manejarErrorNotificacion)
    }

    , notificarMascotaEncontrada(idOrg, mascota){
        return axios.post(barra_org(idOrg)+'/mascotas-perdidas/'+mascota.id+'/te-encontre')
            .then(res => res.data)
            .catch(manejarErrorNotificacion)
    }

    , notificarMascotaReconocida(idOrg, mascota){
        return axios.post(barra_org(idOrg)+'/mascotas-rescatadas/'+mascota.id+'/te-encontre')
            .then(res=>res.data)
            .catch(manejarErrorNotificacion)
    }
}

