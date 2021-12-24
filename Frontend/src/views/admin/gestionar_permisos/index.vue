<template>
    <div>
        <section>
            <h3 class="subtitulo_a_22px">Nombrar Administrador</h3>
            <GestorRol :rol="Permisos.ADMIN" :usuarios="usuarios" :enviarSeleccion="this.cargarNuevoAdministrador"></GestorRol>
        </section>

        <section>
            <h3 class="subtitulo_a_22px">Nombrar Voluntario</h3>
            <GestorRol :rol="Permisos.VALIDADOR" :usuarios="usuarios" :enviar-seleccion="this.cargarNuevoVoluntario"></GestorRol>
        </section>
    </div>
</template>

<script>
    import api from "@/api/api";
    import GestorRol from "./GestorRol";

    export default {
        components: {GestorRol},
        data(){
            return {usuarios:[]
            , Permisos : {
                    ADMIN: 'administrador'
                    , VALIDADOR: 'voluntario'
                }
            }

        }, mounted(){
            api.getUsuarios(this.idOrg)
                .then(usuarios => this.usuarios = usuarios)
        }
        , props: ['idOrg']

        , methods: {
            cargarNuevoAdministrador(nombreUsuario){
                return api.hacerAdmin(this.idOrg, nombreUsuario)
            }
            , cargarNuevoVoluntario(nombreUsuario){
                return api.hacerVoluntario(this.idOrg, nombreUsuario)
            }
        }

    }
</script>

<style type="text/css">

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
    }


</style>