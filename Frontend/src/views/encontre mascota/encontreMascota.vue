<template>
    <primero-datos>
        <template v-slot:default>
            <div id="datos-mascota-encontrada">
                <form class="form-grilla" @submit.prevent="submit">
                    <div>
                        <h3>Fotos</h3>
                        <InputIMG v-model="fotos"/>
                        <br>
                        <br>
                        <h3>Estado</h3>
                        <textarea class="dato" v-model="mascota.descripcion" placeholder="Descripción del Estado en que se Encontró..." required/>

                        <input class="submit" type="submit" value="Confirmar">
                    </div>

                    <div>
                        <h3>Ubicacion</h3>
                        <p>Indique la dirección y colóquela en el mapa (presione 'Aceptar')</p>
                        <input class="dato" v-model="mascota.ubicacion.direccion" type="text" placeholder="Dirección" required>
                        <br>
                        <br>
                        <div id="mapa">
                            <InputMapa  v-model="mascota.ubicacion"></InputMapa>
                        </div>
                    </div>
                </form>
                <p class="positivo">{{mensaje}}</p>
                <p class="error">{{error}}</p>
            </div>
        </template>
    </primero-datos>
</template>

<script>
    import InputIMG from "@/components/imagenes";
    import api from "@/api/api";
    import PrimeroDatos from "@/components/datos personales/primero datos"
    import InputMapa from "@/components/InputMapa";
    function mascotaBase() {
        return ({
            ubicacion: {
                lat: -34.6037345,
                long: -58.3837591
            }
        })
    }

    export default {
        name: "encontreMascota"
        , components: {InputMapa, InputIMG, PrimeroDatos}
        , props: ['idOrg']
        , data(){
            return {
                mascota: mascotaBase()
                , fotos: []
                , error: '', mensaje:''
            }
        }
        , methods: {
            submit(){
                api.agregarMascotaEncontrada(this.idOrg, this.mascota, this.fotos)
                    .then(()=>{
                        this.mascota = mascotaBase()
                        this.mensaje='Su mascota se creo correctamente'
                    })
                    .catch(error=>this.error=error.message)
            }
        }
    }

</script>

<style scoped>
    .positivo{
        color: green;
    }
    .error{
        color: darkred;
    }

    h3 {
        font-weight: 700;
        color: #36b08b;
    }

    .dato {
        margin: 0.3em;
        font-size: 16px;
        width: 200px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
    }

    .dato::placeholder {
         color: #36b08b;
    }
    .submit {
        border: #36b08b 2px solid;
        color: white;
        border-radius: 0.5em;
        font-size: 15px;
        font-weight: bold;
        padding: 5px;
        background-color: #36b08b;
        margin-left: 5px;
        display: block;
    }


    .submit:hover {
        background-color: #20e2d7;
        color: #434343;
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

    .form-grilla{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    #mapa {
        border: black 2px solid;
    }

</style>