<template>
    <section class="contenedor_datos_administrador">
        <form @submit.prevent="enviarPregunta">
            ¿ <input class="dato" v-model="pregunta.detalle" type="text" placeholder="Pregunta" required> ?
            <br><br>

            <label>Respuestas Predefinidas</label>

            <div id="rtas_posibles">
                <div id="contenedor_boton_quitar_y_respuesta" v-for="index in rtas.length" :key="index">
                    <input class="dato" v-model="rtas[index-1]" :placeholder="'Respuesta '+index" required>
                    <button class="boton_quitar" @click.prevent="quitarRespuesta(index-1)">Quitar</button>
                </div>

                <button class="boton_agregar" @click.prevent="agregarRespuesta()">Agregar</button>
            </div>

            <input type="checkbox" v-model="pregunta.libre_rta"> Permitir respuesta libre
            <br>

            <input id="submit" type="submit" value="Enviar">
        </form>

        <p>{{mensaje}}</p>

    </section>
</template>

<script>
    export default {
        data() {
            return {
                pregunta: {
                    detalle: ''
                    , libre_rta: false
                    , rtas_predefinidas: ['']
                }
                , mensaje: ''
            }
        }
        , props: {
            enviar_pregunta: {
                type: Function
            }
        }
        , computed: {
            rtas(){
                return this.pregunta.rtas_predefinidas
            }
        }
        , methods: {
            agregarRespuesta(){
                this.rtas.push('')
            }

            , quitarRespuesta(index){
                return this.rtas.splice(index, 1)
            }
            , enviarPregunta(){
                this.enviar_pregunta(this.pregunta)
                    .then(()=>this.mensaje='Se creó correctamente')
                    .catch(error=>this.mensaje=error.data.error)
                    .catch(error=>this.mensaje=error.message)
            }
        }
    }
</script>

<style scoped>

    #submit {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: transparent;
        margin-top: 10px;
        display: block;
    }

    #submit:hover{
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
            box-shadow: 0 0 7px 10px rgba(32, 226, 215, 0);
        }
    }

    .boton_quitar {
        border: red 1px solid;
        color: white;
        border-radius: 0.7em;
        font-size: 15px;
        padding: 3px;
        margin: 3px 2px;
        background-color: red;
        display: block;
    }

    .boton_quitar:hover {
        background-color: #cb3234;
    }
    .boton_agregar {
        border: #36b08b 1px solid;
        color: white;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 3px;
        margin-top: 5px;
        margin-bottom: 10px;
        background-color: #36b08b;
        display: block;
    }

    .boton_agregar:hover {
        background-color: #20e2d7;
        color: #434343;
    }

    .dato {
         margin: 0.2em 0.1em;
         font-size: 16px;
         width: 200px;
         border-radius: 0.5em;
         color: #36b08b;
         border: #36b08b 1px solid;
         padding: 5px;
    }

    .dato::placeholder {
         color: #36b08b;
    }

    #contenedor_boton_quitar_y_respuesta {
        display: grid;
        grid-template-columns: 12.5% 4%;
        grid-template-rows: 35px;
    }


</style>