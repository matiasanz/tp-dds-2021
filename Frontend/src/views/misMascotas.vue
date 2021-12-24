<template>

    <section id="misMascotas">
        <h1 id="titulo">Mis Mascotas</h1>
        <p>{{error}}</p>
        <div class="contenedor">
            <div id="cargar_mascota">
                <button class="boton_cargar_mascota" v-if="!vistaNuevaMascota" @click.prevent="vistaNuevaMascota = true">Cargar otra mascota</button>
                <section v-else>
                    <button @click.prevent="vistaNuevaMascota = false" class="boton_cancelar">Cancelar</button>
                    <div v-show="vistaNuevaMascota">
                        <form-mascota class="form-mascota" :idOrg="idOrg" @creada="onMascotaCreada"/>
                    </div>
                </section>
            </div>
            <p v-show="mis_mascotas.length===0 && !vistaNuevaMascota"><em>Actualmente no tienes ninguna mascota registrada en esta organización <strong>¿Qué esperás?</strong></em></p>
                <section>
                    <paginas id="contenedor-grilla-mis-mascotas" :lista="mis_mascotas" :cantidad-por-pagina="9">
                        <template v-slot:elem="{elemento: mascota}">
                            <div class="mascota">
                                <h3 id="nombre_mascota">{{mascota.nombre | capitalize}} <span :class="mascota.situacion">{{mascota.situacion | capitalize}}</span></h3>

                                <galeria id="imagen_mascota" :fotos="mascota.fotos"></galeria>
                                <div>
                                    <ul>
                                        <li>
                                            <strong class="dato">Nombre:</strong> {{mascota.nombre | capitalize}}
                                        </li>
                                        <li>
                                            <strong class="dato">Apodo:</strong> {{mascota.apodo | capitalize}}
                                        </li>
                                        <li>
                                            <strong class="dato">Edad Aproximada:</strong> {{mascota.edad_aproximada}} años
                                        </li>
                                    </ul>

                                    <button class="reporte_perdida_o_encontrada" v-show="mascota.situacion!=='ADOPCION'" @click="reportarMascota(mascota)">
                                        <p v-if="mascota.situacion==='PERDIDA'" >La encontré</p>
                                        <p v-else>Reportar perdida</p>
                                    </button>
                                    <p>{{mascota.mensajePorReporte}}</p>
                                </div>
                            </div>
                        </template>
                    </paginas>
                </section>

        </div>

    </section>
</template>

<script>
    import FormMascota from "../components/crear mascota";
    import cookies from "@/api/cookies";

    import api from "../api/api"
    import Galeria from "@/components/galeria";
    import Paginas from "@/components/paginas";

    export default {
        name: "organizacion"
        , data: () => ({
            mis_mascotas: [],
            error: '',
            vistaNuevaMascota: false
        })
        , components: {
            Paginas,
            Galeria,
            FormMascota
        }, props: ['idOrg']
        , mounted() {
            this.cargarMascotas()
        }
        , watch : {
            logueado(){
                this.cargarMascotas()
            }
        }

        ,methods: {
            cargarMascotas(){
                api.getMisMascotas(this.idOrg)
                    .then(mascotas=>this.mis_mascotas = mascotas)
                    .catch(err => this.error = err.message)
            }
            , reportarMascota(mascota){
                const nuevaSituacion = mascota.situacion==='PERDIDA'?
                    'EN_CASA': 'PERDIDA'
                api.reportarSituacionDeMascota(this.idOrg, mascota, nuevaSituacion)
                    .then(m => {
                        mascota.situacion = m.situacion
                        mascota.mensajePorReporte = m.situacion==='PERDIDA'?
                            'Te deseamos que la encuentres pronto': '¡Nos alegramos mucho!'
                    })
            }
            , onMascotaCreada(creada) {
                this.mis_mascotas.push(creada)
                this.vistaNuevaMascota = false
            }
        },
        computed:{
             logueado(){
                return cookies.getCliente().sesionEnCurso
             }
        }
    }
</script>

<style scoped>

/*Etiquetas*/
    .EN_CASA{
        visibility: hidden;
    }

    .ADOPCION{
        background-color: #008cff;
        font-family: Stencil, serif;
        color: black;
        width: 700px;
        font-style: normal;
        border-radius: 0.4em;
        text-align: center;
    }

    .PERDIDA{
        background-color: #ff006a;
        color: black;
        font-family: Stencil, serif;
        width: 700px;
        border-radius: 0.4em;
        font-style: normal;
    }

    .contenedor {
        display: grid;
        grid-template-columns: 25% 60%;
    }

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

    .boton_cargar_mascota, .reporte_perdida_o_encontrada {
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

    .boton_cargar_mascota:hover, .reporte_perdida_o_encontrada:hover {
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

    .boton_cancelar {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: transparent;
        margin-bottom: 3em;
        display: block;
    }

    .boton_cancelar:hover {
        background-color: #d3d3d3;
    }

    #nombre_mascota {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
        font-style: italic;
    }

    .dato {
        border: none;
        text-decoration: underline;
        color: #36b08b;
    }


    #contenedor-grilla-mis-mascotas {
        display: grid;
        grid-template-columns: auto auto auto;
        grid-template-rows: 400px;
    }

</style>