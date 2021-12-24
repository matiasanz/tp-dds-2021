<template>
    <div>
        <h1 id="titulo">Dar en adopción</h1>
        <div class="menu-adopcion">
            <div>
                <section>
                    <button v-if="!vistaNuevaMascota" class="boton_cargar_mascota" @click.prevent="onClickCargarMascota">Cargar una mascota</button>
                    <button v-else @click.prevent="vistaNuevaMascota = false" class="boton_cancelar">Cancelar</button>

                </section>

                <section id="cargar-mascota" v-show="vistaNuevaMascota">
                    <h2 class="subtitulo">Nueva Mascota</h2>
                    <form-mascota :idOrg="idOrg" v-on:creada="this.agregarMascota"/>
                </section>

                <section id="mascota-elegida" v-show="!mascotaElegida.vacia">
                    <h2 class="subtitulo_seleccion_mascota">Ha seleccionado dar en adopción a {{mascotaElegida.nombre}}</h2>
                    <div class="mascota">
                        <img class="mascotas" :src="mascotaElegida.fotos[0]" alt="FOTO">
                        <h3 class="subtitulo_seleccion_mascota">Responda las siguientes preguntas:</h3>
                        <form @submit.prevent="darEnAdopcion">
                            <cuestionario v-model="respuestas" :preguntas="preguntas_adopcion"></cuestionario>
                            <div id="botones">
                                <input id="submit" type="submit"  class="boton_confirmar" value="Confirmar">
                                <button class="boton_cancelar" @click.prevent="deseleccionarMascota">Cancelar</button>
                            </div>
                        </form>
                    </div>
                </section>
            </div>

            <div>
                <h2 class="titulo_seleccion_mascota">Seleccionar Mascota</h2>
                <p>{{error}}</p>

                <div id="contenedor_mascotas" v-if="mascotas_en_casa.length>0">
                    <div v-for="mascota in mascotas_en_casa" :key="mascota.id">

                        <button class="opcion_mascota" @click.prevent="elegirMascota(mascota)">
                            <img class="imagen_mascota" :src="mascota.fotos[0]" alt="FOTO">
                            <h3>{{mascota.nombre}}</h3>
                        </button>

                    </div>
                </div>
                <center v-else>
                    <em>
                        Aún no tienes ninguna mascota cargada en esta organización
                    </em>
                </center>
            </div>
        </div>
    </div>
</template>

<script>
    import api from '@/api/api'
    import FormMascota from "@/components/crear mascota";
    import Cuestionario from "@/components/cuestionario";
    import cookies from "@/api/cookies";
    const mascotaVacia = {fotos: [], vacia: true}
    export default {
        components: {Cuestionario, FormMascota}
        , data(){
            return {
                mis_mascotas: []
                , preguntas_adopcion: []
                , error: ''
                , respuestas: {}
                , vistaNuevaMascota: false
                , mascotaElegida: mascotaVacia
            }
        }
        , computed:{
            logueado(){
                return cookies.getCliente().sesionEnCurso
            }
            , mascotas_en_casa(){
                return this.mis_mascotas.filter(m=>m.situacion==='EN_CASA')
            }
        }
        , props: ['idOrg']
        , watch: {
            logueado(){
                this.recuperarMascotas()
            }
        }
        , mounted(){
            this.recuperarMascotas()
            api.getPreguntasAdoptantes(this.idOrg)
                .then(pregs => this.preguntas_adopcion = pregs)
        }
        , methods: {
            elegirMascota(mascota){
                this.vistaNuevaMascota = false
                this.mascotaElegida = mascota
            }
            , deseleccionarMascota(){
                this.mascotaElegida = mascotaVacia
            }
            , darEnAdopcion(){
                api.darEnAdopcion(this.idOrg, this.mascotaElegida, this.respuestas)
                    .then(()=>{
                        window.location.href = ('/mis-mascotas')
                    })
                    .catch(error => alert(error.message))
            }
            , recuperarMascotas(){
                api.getMisMascotas(this.idOrg)
                    .then(mascotas=>this.mis_mascotas = mascotas)
                    .catch(err => this.error = err.message)
            }
            , agregarMascota(creada){
                this.mis_mascotas.push(creada)
                this.vistaNuevaMascota = false
            }
            , onClickCargarMascota(){
                this.deseleccionarMascota()
                this.vistaNuevaMascota = true
            }
        }
    }
</script>

<style type="text/css">

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

        #sin-registro {
            color:red;
            font-style: italic;
            margin-bottom: 25px;
        }

        .menu-adopcion{
            display: grid;
            grid-template-columns: 30% 70%;
        }

        .boton_cargar_mascota {
            border: #36b08b 2px solid;
            color: #36b08b;
            font-weight: bold;
            border-radius: 0.5em;
            font-size: 15px;
            padding: 5px;
            background-color: transparent;
            margin-bottom: 10px;
            display: block;
        }

        .boton_cargar_mascota:hover, .boton_confirmar:hover {
            background-color: #20e2d7;
            color: #434343;
            animation: shadow-pulse 1000ms 1;
            font-weight: bold;
        }

        .boton_cancelar {
            border: #36b08b 2px solid;
            color: #36b08b;
            font-weight: bold;
            border-radius: 0.5em;
            font-size: 15px;
            padding: 5px;
            background-color: transparent;
            display: block;
        }

        .boton_cancelar:hover {
            background-color: #d3d3d3;
        }


        .titulo_seleccion_mascota {
            text-align: center;
            font-weight: 700;
            color: #36b08b;
            font-size: 25px;
            margin-bottom: 25px;
        }

        .subtitulo_seleccion_mascota {
            font-weight: 700;
            color: #36b08b;
            font-size: 22px;
            margin-bottom: 25px;
        }
         .subtitulo {
            font-weight: 700;
            color: #36b08b;
            font-size: 22px;
         }

        #nueva-mascota {
            border: none;
        }

        .opcion_mascota {
            height: 175px;
            width: 150px;
            margin: 15px;
            border-radius: 0.5em;
            border: black 2px solid;
            background-color: #36b08b;
            color: white;
            text-transform: capitalize;
            letter-spacing: 1px;
        }
        .opcion_mascota:hover {
            background-color: #20e2d7;
            color: #434343;
            font-weight: bold;
        }

        .imagen_mascota {
            height: 100px;
            width: 100px;
            border-radius: 10px;
            border: black 2px solid;
        }

        .nombre_mascota {
            color: white;
            font-size: 20px;
        }

        img.mascotas {
            border-radius: 0.5em;
            border: black 2px solid;
            margin: 10px auto auto auto;
            height: 125px;
            width: 125px;
        }

        .boton_confirmar {
            border: #36b08b 2.3px solid;
            color: white;
            font-weight: bold;
            border-radius: 0.5em;
            font-size: 15px;
            padding: 5px;
            background-color: #36b08b;
            margin: 10px 0px 10px 0px;
            display: block;
        }

        #contenedor_mascotas {
            display: grid;
            grid-template-columns: repeat(4, auto);
            grid-gap: 3em;
        }

</style>
