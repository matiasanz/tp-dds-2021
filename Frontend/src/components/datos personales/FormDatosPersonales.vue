<template>
    <form @submit.prevent="enviarForm">
        <input class="dato" v-model="usuario.nombre" type="text" id="fname" placeholder="Nombre" required>
        <input class="dato" v-model="usuario.apellido" type="text" id="lname" placeholder="Apellido" required>
        <br>
        <input class="dato" v-model="usuario.fecha_nacimiento" required type="text" id="date" name="date" placeholder="Fecha de Nacimiento" @blur="onBlur" onfocus="this.type='date'">
        <br>
        <br>

        <div>
            <label class="documento" for="dni-types">Documento</label>
            <br>
            <select  class="dato" v-model="usuario.tipo_documento" name="type" id="dni-types">
                <option :value="undefined" disabled selected hidden>Tipo</option>
                <option value="DNI">DNI</option>
                <option value="PASAPORTE">Pasaporte</option>
            </select>
            <input class="dato" v-model="usuario.nro_documento" type="number" id="number" placeholder="Nro" required min="10000000">
        </div>

        <input class="dato" v-model="usuario.telefono" type="number" id="phone" name="phone" placeholder="Telefono" required min="10000000">
        <input class="dato" v-model="usuario.mail" type="email" id="mail" name="mail" placeholder="Mail" required>
        <br>

        Notificarme vía
        <input v-model="usuario.medios_contacto" type="checkbox" id="wpp" value="wpp">Whatsapp
        <input v-model="usuario.medios_contacto" type="checkbox" id="sms" value="SMS">SMS
        <input v-model="usuario.medios_contacto" type="checkbox" id="email" value="MAIL">Email
        <br>

        <!--TODO: Esto separarlo en otro componente-->
        <div v-for="(contacto, index) in usuario.contactos" :key="index">
            <h3 class="subtitulo">Contacto #{{index+1}}</h3>
            <input class="dato" v-model="contacto.nombre" type="text" placeholder="Nombre">
            <input class="dato" v-model="contacto.apellido" type="text" placeholder="Apellido"><br>
            <input class="dato" v-model="contacto.telefono" type="number" placeholder="Telefono">
            <input class="dato" v-model="contacto.mail" type="email" placeholder="Mail"><br>

            Notificarlo vía
            <input v-model="contacto.medios_contacto" type="checkbox" value="wpp">Whatsapp
            <input v-model="contacto.medios_contacto" type="checkbox" value="SMS">SMS
            <input v-model="contacto.medios_contacto" type="checkbox" value="mail">Email
        </div>

        <input type="submit" id="accept" value="Aceptar">
    </form>
</template>

<script>

import LoginApi from "@/api/LoginApi";

export default {
    name: 'FormDatosPersonales'
    , props: {cantidadContactos: Number}
    , data(){
        return {
            usuario:{
                medios_contacto: []
                , contactos: []
            }
        }
    }
    , computed: {
        fecha_nac(){
            return document.getElementById('date')
        }
    }

    , mounted() {
        for(let i=0; i<this.cantidadContactos; i++){
            this.agregarContacto()
        }
    }

    , methods: {
        enviarForm(){
            LoginApi.enviarDatosPersonales(this.usuario)
                .then(()=>this.$store.commit('ingresarDatos', this.usuario))
                .catch(error => alert(error.message))
        }
        , agregarContacto(){
            this.usuario.contactos.push({medios_contacto: []})
        }
        , onBlur(){
            if(!this.usuario.fecha_nacimiento){
                this.fecha_nac.type='text'
            }
        }
    }
}
</script>

<style scoped>

    #accept {
        color: white;
        font-weight: bold;
        border-radius: 0.5em;
        border: #36b08b 2px solid;
        font-size: 15px;
        padding: 5px;
        background-color: #36b08b;
        margin-top: 30px;
        margin-bottom: 10px;
        display: block;
    }

    #accept:hover {
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

    .subtitulo {
        font-weight: bold;
        color: #36b08b;
        margin-top: 50px;
        font-style: italic;
    }

    .dato {
        margin: 0.3em;
        font-size: 16px;
        width: 200px;
        height: 25px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
    }

    .dato::placeholder {
        color: #36b08b;
    }

    .documento {
        margin-left: auto;
    }
</style>