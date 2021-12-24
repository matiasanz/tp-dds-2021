<template>
    <main>
        <h1 id="titulo">Menu Administrador</h1>

        <div class="contenedor_datos_administrador">
            <h3 class="subtitulo">Gestionar roles de usuario</h3>
            <GestionarRoles :idOrg="idOrg"></GestionarRoles>

            <h3 class="subtitulo_a_22px">Imagenes</h3>
            <form_imagenes v-show="org!==null" :org="org"/>

            <h3 class="subtitulo_a_22px">Caracteristicas</h3>
            <form_preguntas :enviar_pregunta="agregar_caracteristica"/>

            <h3 class="subtitulo_a_22px">Preguntas a adoptantes</h3>
            <form_preguntas :enviar_pregunta="agregar_pregunta"/>
        </div>

    </main>
</template>

<script>
    import form_preguntas from "./form_preguntas";
    import form_imagenes from "./form_imagenes";
    import api from "@/api/api";
    import GestionarRoles from "./gestionar_permisos";

    export default {
        data() {
            return ({
                org: null
            })
        }
        , props: ['idOrg']
        , mounted() {
            api.getOrganizacion(this.idOrg)
                .then(org => this.org = org)
        }

        , methods: {
            agregar_caracteristica(caracteristica){
                return api.agregarCaracteristica(this.idOrg, caracteristica)
            }
            , agregar_pregunta(pregunta){
                return api.agregarPregunta(this.idOrg, pregunta)
            }
        },
        components: {
            GestionarRoles,
            form_preguntas,
            form_imagenes
        }
    }
</script>

<style scoped>

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

    .subtitulo {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
    }

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
    }


</style>