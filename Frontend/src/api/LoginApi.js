import axios from "axios";
import utils from "@/api/utils";

function mantener_sesion(vue, usuario){
    vue.$store.commit('iniciarSesion', usuario)
}

function liberar_sesion(vue){
    vue.$store.commit('logout')
}

export default {
    enviarDatosPersonales(datosPersonales){
        return axios.post("/api/mis-datos", {cliente: datosPersonales})
            .then(res => res.data)
            .catch(err => {
                throw new Error(err.response.data.error)
            })
    },

    //*******************************
    registrarse(username, password, vue){
        return axios.post('/api/signup', {username: username, password: password})
            .then(res => {
                mantener_sesion(vue, res.data)
            }).catch(errorRecibido => {
                const errorPropagado = new Error()
                switch (errorRecibido.response.status){
                    case 402: {
                        errorPropagado.errores = errorRecibido.response.data.errores
                        throw errorPropagado
                    }

                    case 403: {
                        errorPropagado.errores = [errorRecibido.response.data.error]
                        break
                    }
                    default:{
                        if(errorRecibido.response)
                            alert(errorRecibido.response.data.error)
                        else{
                            alert(errorRecibido.message)
                        }
                    }
                }
            })
    },

    iniciarSesion(username, password, vue) {
        return axios.post('/api/login', {username, password})
            .then(res => {
                mantener_sesion(vue, res.data)
            })
            .catch( error => {
                if(!error.response){
                    alert(error.message)
                    return
                }

                switch (error.response.status) {

                    case 401: //Contrasenia incorrecta
                        utils.throw_error('El nombre de usuario y/o la contraseña son incorrectos')
                        break

                    case 408: //Timed out
                        utils.throw_error("El servidor se demoró demasiado en contestar")
                        break

                    default : {
                        alert('¿Ejecutaste el servidor?\nError: ' + error.message)
                        break
                    }
                }
            })
    },
    reautenticar() {
        return axios.get('/api/usuario')
            .then(res => res.data)
    }, logout(vue) {
        liberar_sesion(vue)
        return axios.get('/api/logout')
            .catch(error => utils.throw_error(error.response.data.error))
    }
}