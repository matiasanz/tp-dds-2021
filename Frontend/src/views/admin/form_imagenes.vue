<template>
    <section>
        <form @submit.prevent="cambiarParametrosImagenes">
            <label>Alto:</label>
            <input class="dato" v-model="imagenes.alto" required type="number" min="110">
            <br>
            <label>Ancho:</label>
            <input class="dato" v-model="imagenes.ancho" required type="number" min="110">
            <br>
            <label>Calidad:</label>
            <select class="dato" v-model="imagenes.calidad" name="calidad">
                <option selected hidden :value="imagenes.calidad">{{imagenes.calidad.toLowerCase()}}</option>
                <option value="BUENA">Alta</option>
                <option value="NORMAL">Normal</option>
                <option value="BASICA">Básica</option>
            </select>
            <br>

            <input id="submit" type="submit" value="Enviar">
        </form>

        <p>{{mensaje}}</p>
    </section>

</template>

<script>

import api from "@/api/api";

export default {
    data(){
        return ({
            mensaje: ''
        })
    }
    , methods:{
        cambiarParametrosImagenes(){
            api.setImageParams(this.org.id, this.imagenes)
                .then(()=>this.mensaje='Se actualizó correctamente')
                .catch(error => this.mensaje = error.message)
        }
    }
    , computed : {
        imagenes(){
            return  this.org===null? ({calidad: ''}): ({
                alto: this.org.imagenes.alto
                , ancho: this.org.imagenes.ancho
                , calidad: this.org.imagenes.calidad
            })
        }
    }

    , props: ['org']
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

    .dato {
        margin: 0.2em 0.1em;
        font-size: 16px;
        width: 200px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
    }

    .dato::placeholder {
        color: #36b08b;
    }


</style>