<template>
    <div>
        <h1 id="titulo">Menú Voluntarios</h1>

        <section>
            <h2 class="subtitulo">Aspirantes a Adoptantes</h2>
            <paginas :lista="adoptantes" :cantidadPorPagina="3">
                <template v-slot:elem="{elemento: publicacion}">
                    <div>
                        <h3 class="subtitulo_a_22px">Adoptante #{{publicacion.id}}</h3>
                        <div v-if="publicacion.estado==='PENDIENTE'">
                            <form @submit.prevent="homologarAdoptante(publicacion)" :class="publicacion.estado">
                                <div id="contenedor_calificacion_y_confirmacion_decision">
                                    <select class="opcion" v-model="publicacion.aprobada">
                                        <option selected hidden disabled :value="undefined">Calificación...</option>
                                        <option :value="true">Aprobada</option>
                                        <option :value="false">Rechazada</option>
                                    </select>

                                    <input id="submit" type="submit" value="Confirmar Decisión" >
                                </div>
                            </form>
                        </div>
                        <div v-else-if="publicacion.estado==='APROBADA'">
                            <h3 class="aprobada">Aprobada</h3>
                        </div>
                        <div v-else>
                            <h3 class="rechazada">Rechazada</h3>
                        </div>
                        <ul>
                            <li>
                                <strong class="dato">Nombre Completo:</strong> {{publicacion.aspirante.nombre}} {{publicacion.aspirante.apellido}}
                            </li>
                            <li>
                                <strong class="dato">Preferencias Indicadas:</strong>
                                <ul>
                                    <li v-for="(rta, preg) in publicacion.preferencias" :key="preg">
                                        <strong class="dato">¿{{preg}}?</strong> {{rta}}
                                    </li>
                                </ul>
                            </li>
                        </ul>

                        <hr>
                    </div>
                </template>
            </paginas>

        </section>

        <section>
            <h2 class="subtitulo">Mascotas en Adopción</h2>
            <paginas :lista="mascotas_adopcion" :cantidadPorPagina="3">
                <template v-slot:elem="{elemento: publicacion}">
                    <div id="contenedor_mascotas">
                        <h3 class="subtitulo_a_22px">Mascota #{{publicacion.id}}</h3>
                        <img id="imagen_mascota" :src="publicacion.mascota.fotos[0]" alt="FOTO">
                        <ul>
                            <li>
                                <strong class="dato">Dueño:</strong> {{publicacion.mascota.duenio.nombre}} {{publicacion.mascota.duenio.apellido}}
                            </li>
                            <li>
                                <strong class="dato">Respuestas:</strong>
                                <ul>
                                    <li v-for="(rta, preg) in publicacion.respuestas" :key="preg">
                                        <strong class="dato">¿{{preg}}?</strong> {{rta}}
                                    </li>
                                </ul>
                            </li>
                        </ul>

                        <div v-if="publicacion.estado==='PENDIENTE'">
                            <form @submit.prevent="homologarDador(publicacion)">
                                <div id="contenedor_calificacion_y_envio">
                                    <select class="opcion" v-model="publicacion.aprobada">
                                        <option selected hidden disabled :value="undefined">Calificación...</option>
                                        <option :value="true">Aprobada</option>
                                        <option :value="false">Rechazada</option>
                                    </select>

                                    <input id="submit" type="submit" value="Enviar">
                                </div>
                            </form>
                        </div>
                         <div v-else-if="publicacion.estado==='APROBADA'">
                            <h3 class="aprobada">Aprobada</h3>
                            <a href="/catalogo-adopcion">Ver</a>
                        </div>
                        <div v-else>
                            <h3 class="rechazada">Rechazada</h3>
                        </div>
                    </div>

                </template>
            </paginas>
        </section>
    </div>
</template>

<script>
import Paginas from "@/components/paginas";
import VoluntariosApi from "@/api/VoluntariosApi";
export default {
    props: ['idOrg']
    , data(){
        return {
            adoptantes: []
            , mascotas_adopcion: []
        }
    }
    , mounted() {
        VoluntariosApi.getAdoptantesPendientes(this.idOrg)
            .then(adoptantes => this.adoptantes = adoptantes)
        VoluntariosApi.getDadoresPendientes(this.idOrg)
            .then(dadores => this.mascotas_adopcion = dadores)
    }
    , components: {Paginas}
    , methods: {
        homologarAdoptante(publicacion){
            VoluntariosApi.homologarAdoptante(this.idOrg, publicacion.id, publicacion.aprobada)
                .then(actual => publicacion.estado=actual.estado)
        }

        , homologarDador(publicacion){
            VoluntariosApi.homologarDador(this.idOrg, publicacion.id, publicacion.aprobada)
                .then(actual => publicacion.estado=actual.estado)
        }
    }
}
</script>

<style scoped>
    .rechazada{
        background-color: red;
        font-family: Stencil, serif;
        width: 6.6em;
        border-radius: 0.4em;
        text-align: center;
    }
    .aprobada{
        background-color: #09a709;
        font-family: Stencil, serif;
        width: 6.6em;
        border-radius: 0.4em;
        text-align: center;
    }

    #titulo {
        background: #ccc;
        background: radial-gradient(#0c9db3, #05c0dc);
        font-family: 'Montserrat', sans-serif;
        text-align: center;
        text-transform: uppercase;
        font-style: normal;
        font-size: 2.5em;
        line-height: 100%;
        -webkit-text-fill-color: transparent;
        -webkit-background-clip: text;
    }

    .dato {
        color: #36b08b;
        text-decoration: underline;
    }

    .subtitulo {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
    }

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
        font-style: italic;
    }

    .opcion{
        font-size: 16px;
        width: 200px;
        height: 25px;
        border-radius: 0.5em;

        border: #36b08b 1px solid;
    }

    #submit {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 1px;
        background-color: transparent;
        display: block;
    }

    #submit:hover {
        background-color: #20e2d7;
        color: #434343;
        animation: shadow-pulse 1000ms 1;
    }
     @keyframes shadow-pulse {
          0% {
            box-shadow: 0 0 0 0 rgba(32, 226, 215, 0.6);
          }
          100% {
            box-shadow: 0 0 7px 10px rgba(32, 226, 215, 0);
          }
     }

    #contenedor_calificacion_y_confirmacion_decision {
        display: grid;
        grid-template-columns: 11% 9%;
    }

    #contenedor_calificacion_y_envio {
        display: grid;
        grid-template-columns: 11% 4%;

    }

    #contenedor_mascotas {

    }

    #imagen_mascota {
        border: black 6px dotted;
        border-radius: 0.5em;
        height: 100px;
        width: 100px;
    }

</style>