<template>
    <div id="contenedor_datos_mascota">
            <div v-for="publicacion in publicaciones" :key="publicacion.id">
                    <div>
                        <div class="mascota">
                            <h4 class="nombre_mascota">{{publicacion.mascota.nombre}}</h4>
                            <galeria class="imagen_mascota" :fotos="publicacion.mascota.fotos"></galeria>
                            <ul>
                                <li><strong class="dato">Nombre:</strong> {{publicacion.mascota.nombre}}</li>
                                <li><strong class="dato">Apodo:</strong> {{publicacion.mascota.apodo}}</li>
                                <li><strong class="dato">Edad Aproximada:</strong> {{publicacion.mascota.edad_aproximada}} años</li>
                                <li><strong class="dato">Descripcion:</strong> {{publicacion.mascota.descripcion}}</li>
                                <li><strong class="dato">Especie:</strong> {{publicacion.mascota.especie.toLowerCase()}}  </li>
                                <li><strong class="dato">Preguntas al dueño:</strong>
                                    <ul>
                                        <li v-for="(respuesta, pregunta) in publicacion.respuestas" :key="pregunta">
                                            <strong class="dato">¿{{pregunta}}?</strong> {{respuesta}}
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                            <button class="boton_accion" v-show="!publicacion.mascota.estadoSolicitud" @click.prevent="solicitarAdopcion(publicacion)">
                                Solicitar Adopción
                            </button>
                            <p>{{publicacion.mascota.estadoSolicitud}}</p>
                        </div>
                    </div>
            </div>
    </div>

</template>

<script>
import api from "@/api/api"
import Galeria from "@/components/galeria";

export default {
    components: {Galeria},
    data: () => ({publicaciones: []})
    , mounted(){
        api.getMascotasEnAdopcion(this.idOrg)
            .then(publicaciones =>{
                publicaciones.forEach(p=>p.mascota.estadoSolicitud = '')
                this.publicaciones = publicaciones
            })
    }
    , methods: {
        solicitarAdopcion(publicacion){
            const mascota = publicacion.mascota
            mascota.estadoSolicitud = 'Enviando...'
            api.adoptarMascota(this.idOrg, publicacion)
                .then(()=>mascota.estadoSolicitud = 'Nos hemos comunicado con el dueño, pronto se contactará con ud.')
                .catch(()=>mascota.estadoSolicitud = 'Se ha producido un error al querer procesar la solicitud. Por favor intente de nuevo más tarde y disculpe las molestias ocasionadas')
        }
    }
    , props: ['idOrg']
}
</script>

<style scoped>

    .nombre_mascota {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
        font-style: italic;
        text-transform: capitalize;
    }

    .dato {
        border: none;
        text-decoration: underline;
        color: #36b08b;
    }

    #contenedor_datos_mascota {
      display: grid;
      grid-template-columns: 80% 80% 80%;
      position: absolute;
      left: 650px;
      top: 250px;
    }


</style>