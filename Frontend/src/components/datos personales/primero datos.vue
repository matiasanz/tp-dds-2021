<template>
    <div>
        <section v-if="!identificado">
            <h3 class="subtitulo">Primero identifíquese</h3>
            <FormDatosPersonales :cantidadContactos="1"></FormDatosPersonales>
        </section>
        <section v-else-if="!registrado && !omitido">
            <h3 class="subtitulo">¿Desea Registrarse?</h3>
            <registrarse></registrarse>
            <button @click.prevent="omitir">Omitir</button>
        </section>
        <div v-else>
            <slot></slot>
        </div>
    </div>
</template>

<script>
import cookies from "@/api/cookies";
import FormDatosPersonales from "./FormDatosPersonales";
import Registrarse from "@/components/datos personales/registrarse";

export default {
    name: "primero-datos"
    , components: {Registrarse, FormDatosPersonales}
    , data: ()=>({omitido: false})
    , computed: {
        identificado() {
            return cookies.getCliente().datosPersonales
        }
        , registrado(){
            return cookies.getCliente().sesionEnCurso
        }
    }
    , methods:{
        omitir(){
            this.omitido=true
        }
    }
}
</script>

<style scoped>
    .subtitulo {
        font-weight: 700;
        color: #36b08b;
        margin-bottom: 25px;
    }
</style>