<template>
    <primero-datos>
        <template>
            <div id="contenedor">
                <form @submit.prevent="crearMascota">
                    <InputIMG id="imagen" v-model="fotos"></InputIMG>

                    <div>
                        <input class="dato" v-model="mascota.nombre" type="text" placeholder='Nombre..' required>
                        <br>

                        <input class="dato" v-model="mascota.apodo" type="text" placeholder="Apodo..." required>
                        <br>

                        <input class="dato" v-model="mascota.edad_aproximada" type="number" placeholder="Edad..." min="0" required>
                        <br>

                        <select class="opcion" v-model="mascota.sexo" required>
                            <option value="" disabled selected hidden>Sexo...</option>
                            <option value="MACHO">Macho</option>
                            <option value="HEMBRA">Hembra</option>
                        </select>
                        <br>

                        <select class="opcion" v-model="mascota.especie" required>
                            <option value="" disabled selected hidden>Especie...</option>
                            <option value="PERRO">Perro</option>
                            <option value="GATO">Gato</option>
                        </select>
                        <br>
                        <textarea class="dato" v-model="mascota.descripcion" placeholder="Descripción Física..." required/>

                        <div v-if="org.caracteristicas.length>0">
                            <h3 class="subtitulo_a_22px">Características</h3>
                            <cuestionario v-model="mascota.caracteristicas" :preguntas="org.caracteristicas"></cuestionario>
                        </div>
                        <br v-else>

                        <input type="submit" id="boton_confirmar" value="Confirmar">

                        <p>{{mensaje}}</p>
                        <p>{{error}}</p>
                    </div>
                </form>
            </div>

        </template>
    </primero-datos>

</template>

<script>
    import api from "@/api/api";
    import InputIMG from "@/components/imagenes"
    import Cuestionario from "@/components/cuestionario"
    import PrimeroDatos from "@/components/datos personales/primero datos";

    function mascota_vacia(){
        return ({
            nombre: ''
            , apodo: ''
            , descripcion: ''
            , edad_aproximada: null
            , sexo: ''
            , caracteristicas: {}
            , especie: ''
            , error: ''
        })
    }

    export default {
        props: {
            idOrg: Number
            , perdida:{
                type: Boolean
                , default: ()=>false
            }
        }

        , data(){
            return {
                mascota: mascota_vacia()
                , fotos: []
                , org: {caracteristicas: []}
                , mensaje: ''
                , error: ''
            }
        }
        , mounted(){
            api.getOrganizacion(this.idOrg)
                .then(org => this.org = org)
        }
        , methods : {
            crearMascota() {
                if (this.fotos.length < 1){
                    this.error = 'Debe ingresar al menos una foto'
                    return
                }

                api.crearMascota(this.idOrg, this.mascota, this.perdida, this.fotos)
                    .then(mascotaCreada=>{
                        this.error=''
                        this.$emit('creada', mascotaCreada)
                        this.mensaje="Su mascota se ha creado correctamente"
                        this.mascota = mascota_vacia()
                        this.fotos = []
                    })
                    .catch(error => this.error = error)

            }
        }
        , components: {PrimeroDatos, Cuestionario, InputIMG}
    }
</script>

<style type="text/css">

    #contenedor {
        display: grid;
    }

    #contenedor form{
        max-width: fit-content;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    #imagen {
        margin-bottom: 10px;
    }

    #boton_confirmar {
        border-radius: 4px;
        padding: 5px;
        margin-left: 5px;
        background-color: #36b08b;
        border: #36b08b 2px solid;
        color: white;
        font-weight: bold;
        vertical-align: bottom;
    }

    #boton_confirmar:hover {
        animation: shadow-pulse 1000ms 1;
        background-color: #20e2d7;
        color: #434343;
        font-weight: bold;
    }
    @keyframes shadow-pulse {
        0% {
            box-shadow: 0 0 0 0 rgba(32, 226, 215, 0.6);
        }
        100% {
            box-shadow: 0 0 8px 16px rgba(32, 226, 215, 0);
        }
    }

    .select {
        border-radius: 5px;
        width: 200px;
    }

    .opcion{
        font-size: 16px;
        width: 200px;
        height: 25px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
        margin: 0.3em;
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

    .dato_mascota {
        color: #36b08b;
        font-weight: bold;
        text-decoration: underline;
    }

    .dato::placeholder {
        color: #36b08b;
    }

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
    }


</style>
