<template>
    <section>
        <button id="boton_generar_publicacion" v-if="!vistaPublicacion" @click="vistaPublicacion=true">Generar una publicación</button>
        <div v-else>
            <h2 class="subtitulo">Generar una publicación</h2>
            <primero-datos>
                <template>
                    <div>
                        <form @submit.prevent="generarPublicacion">
                            <div id="form_publicacion" v-show="preguntas.length>0">
                                <h3 class="subtitulo_a_20px">Describa cómo imagina a su futura mascota</h3>
                                <cuestionario v-model="respuestas" :preguntas="preguntas"></cuestionario>
                            </div>
                            <section id="contenedor_botones">
                                <input id="boton_crear_publicacion" type="submit" value="Crear Publicación">
                                <button id="boton_cancelar" @click="vistaPublicacion=false">Cancelar</button>
                            </section>
                        </form>
                        <p>{{mensaje}}</p>
                    </div>
                </template>
            </primero-datos>

        </div>
    </section>
</template>

<script>
import api from "@/api/api";
import Cuestionario from '@/components/cuestionario'
import PrimeroDatos from "@/components/datos personales/primero datos";

export default {
    name: 'quiero-adoptar',
    data(){
        return {respuestas: {}, preguntas: [], mensaje: '', vistaPublicacion: false}
    }
    , props: ['idOrg', 'usuario']
    , mounted() {
        api.getPreguntasAdoptantes(this.idOrg)
            .then(preguntas => this.preguntas = preguntas)
    }
    , methods: {
        generarPublicacion(){
            api.generarPublicacion(this.idOrg, this.respuestas)
                .then(()=>this.mensaje = 'Se creó correctamente')
                .catch(error => this.mensaje = error.message)
        }
    }
    , components:{
        PrimeroDatos, Cuestionario
    }
}
</script>

<style scoped>

    #contenedor_botones{
        margin-top: 10px;
        display: grid;
        grid-template-columns: 150px 90px;
        grid-template-rows: 30px;
        gap: 0.5em;

    }


    #boton_generar_publicacion {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: white;
        margin-bottom: 10px;
        display: block;
    }

    #boton_crear_publicacion {
        border: #36b08b 2px solid;
        color: white;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: #36b08b;
        display: block;
    }

    #boton_generar_publicacion:hover, #boton_crear_publicacion:hover {
        background-color: #20e2d7;
        color: #434343;
        font-weight: bold;
        animation: shadow-pulse 1000ms 1;
    }
    @keyframes shadow-pulse {
        0% {
            box-shadow: 0 0 0 0 rgba(32, 226, 215, 0.6);
        }
        100% {
            box-shadow: 0 0 8px 16px rgba(32, 226, 215, 0);
        }
    }

    #boton_cancelar {
        color: #36b08b;
        font-weight: bold;
        border: #36b08b 2px solid;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: transparent;
        display: block;
        float: right;
    }

    #boton_cancelar:hover {
        background-color: #d3d3d3;
    }

    .subtitulo {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
    }

    .subtitulo_a_20px {
        font-weight: 700;
        color: #36b08b;
        font-size: 20px;
    }

    #form_publicacion {
        margin-top: 5em;
    }



</style>