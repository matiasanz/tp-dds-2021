<template>
    <div>
        <h1 id="titulo">Hogares de Transito ({{filtrado.hogares.length}})</h1>
        <div id="contenedor">
            <section>
                <div id="menu">
                    <form @change="refresh_hogares">
                        <label class="nombre-opcion">Especie:</label>
                        <select class="opcion" v-model="filtros.por_especie.especie">
                            <option value="">Cualquiera</option>
                            <option value="PERRO">Perros</option>
                            <option value="GATO">Gatos</option>
                        </select>
                        <br>
                        <br>
                        <label class="nombre-opcion">Tamaño:</label>
                        <select class="opcion" v-model="filtros.por_tamanio.tamanio">
                            <option value="" selected>Todos</option>
                            <option value="CHICO">Chico</option>
                            <option value="MEDIANO">Mediano</option>
                            <option value="GRANDE">Grande</option>
                        </select>
                        <br>
                        <br>
                        <label class="filtro">
                            <input v-model="filtros.por_patio.activado" type="checkbox"> Filtrar por patio
                        </label>
                        <br>
                        <br>
                        <label class="filtro">
                            <input class="filtro" v-model="filtros.por_disponibilidad.activado" type="checkbox"> Filtrar por disponibilidad
                        </label>
                    </form>
                </div>
            </section>

            <paginas class="pagina" :lista="filtrado.hogares" :cantidadPorPagina="4">
                <template class="selector" v-slot:elem="{elemento: hogar}">
                    <div id="contenedor-hogares-de-transito">
                        <h4 id="nombre-hogar">{{hogar.nombre}}</h4>

                        <div class="datos_hogar">
                            <ul>
                                <li><strong class="dato">Teléfono:</strong> {{hogar.telefono}}</li>
                                <li><strong class="dato">Lugares Disponibles:</strong> {{hogar.lugares_disponibles}}</li>
                                <li><strong class="dato">Tiene Patio:</strong> {{hogar.patio? "Sí":"No"}}
                                <li><strong class="dato">Direccion:</strong> {{hogar.ubicacion.direccion}}</li>
                                <li><strong class="dato">Especies Aceptadas</strong>
                                    <ul>
                                        <div v-for="especie in hogar.admisiones" :key="especie.id">
                                            <li>{{especie | plural}}</li>
                                        </div>
                                    </ul>
                                </li>

                            </ul>
                            <Mapa class="google-maps" :latitud="hogar.ubicacion.lat" :longitud="hogar.ubicacion.long"></Mapa>
                        </div>
                    </div>
                </template>
            </paginas>
        </div>
    </div>
</template>

<script>
import api from '../api/api'
import Mapa from '@/components/Mapa'
import Paginas from "@/components/paginas";
    const CUALQUIERA = ''

    const por_patio = {
        activado: false
        , test(hogar){
            return !this.activado || hogar.patio
        }
    }

    const por_especie = {
        especie: '',
        test(hogar) {
            const admisiones = hogar.admisiones
            return this.especie === CUALQUIERA || admisiones.length===1 && admisiones.includes(this.especie)
        }
    }

    const por_disponibilidad = {
        test(hogar){
            return !this.activado || hogar.lugares_disponibles>0
        }
        , activado: false
    }

    const por_tamanio = {
        tamanio: '',
        test(hogar){
            return this.tamanio===CUALQUIERA || hogar.tamanios_permitidos.includes(this.tamanio)
        }
    }

    export default {
        data(){
            return {
                filtrado: {
                    hogares: []
                }
                , hogaresDeTransito: []
                , filtros: {
                    por_patio
                    , por_especie
                    , por_disponibilidad
                    , por_tamanio
                }
                , CUALQUIERA
            }
        }
        , methods: {
            pasaFiltros(hogar){
                const filtros = Object.values(this.filtros)
                return filtros.every(f=>f.test(hogar))
            }
            , refresh_hogares() {
                this.filtrado.hogares = this.hogaresDeTransito.filter(h=>this.pasaFiltros(h))
            }
        }
        , mounted(){
            api.getHogaresDeTransito()
                .then(hogaresDeTransito => {
                    this.hogaresDeTransito = hogaresDeTransito
                    this.refresh_hogares()
                })
        }

        , components: {Paginas, Mapa}
    }


</script>

<style>

/*General*/
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

    #contenedor {
        display: grid;
        grid-template-columns: 20% 80%;
        margin-top: 50px;
    }

/*Filtros*/
    #menu {
        margin: auto;
        overflow: hidden;
    }

    .opcion{
        font-size: 16px;
        width: 200px;
        height: 25px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
    }

    .nombre-opcion {
        margin-right: 0.5em;
        font-size: 20px;
    }


    .filtro {
        margin-bottom: 30px;
        padding:5px 0 5px 60px;
        font-size: 20px;
    }

/*Hogares*/
    #contenedor-hogares-de-transito {
        column-count: 2;
        display: flex;
        flex-direction: column;
        padding-left: 9.5em;
        padding-right: 9.5em;
    }

    .datos_hogar{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    .datos_hogar > ul{
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }

    .dato {
        color: #36b08b;
        text-decoration: underline;
        border-style: none;
    }

    #nombre-hogar {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
        margin-bottom: 25px;
        font-style: italic;
    }

    .google-maps {
        width: 300px;
        height: 200px;
        border: black 1px solid;
    }

</style>