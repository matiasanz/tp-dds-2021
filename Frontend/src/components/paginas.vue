<template>
    <div>
        <div v-for="(unElemento, index) in elementosDePagina" :key="index">
            <slot name="elem" :elemento="unElemento">
            </slot>
        </div>
        <div class="indice-pags" v-show="cantidadPaginas>1">
            <button class="boton-cambio-de-pagina" :disabled="!hayPagsAntes" @click.prevent="retrocederPagina">Anterior</button>
            <div v-for="index in cantidadPaginas" :key="index">
                <button class="numero-pagina" :disabled="pagina===index" @click="setPagina(index)">{{index}}</button>
            </div>
            <button class="boton-cambio-de-pagina" :disabled="!hayPagsDespues" @click.prevent="avanzarPagina">Siguiente</button>
        </div>
    </div>
</template>

<script>
export default {
    name:"paginas",
    data() {
        return {
            pagina: 1
        }
    }
    , props: {
        lista: Array
        , cantidadPorPagina: Number
    }
    , computed: {
        elementosDePagina() {
            let dondeEmpiezo = (this.pagina - 1) * this.cantidadPorPagina
            return this.lista.slice(dondeEmpiezo, dondeEmpiezo + this.cantidadPorPagina)
        }
        , cantidadPaginas() {
            return Math.ceil(this.lista.length / this.cantidadPorPagina)
        },
        hayPagsAntes() {
            return 1 < this.pagina
        },
        hayPagsDespues() {
            return this.pagina < this.cantidadPaginas
        }
    }
    , methods: {
        setPagina(index) {
            this.pagina = index
        },
        retrocederPagina() {
            this.pagina--
        },
        avanzarPagina() {
            this.pagina++
        }
    }
}
</script>

<style scoped>
    .indice-pags{
        display: flex;
        flex-direction: row;
        position:absolute;
        z-index: 1;
        left: 50%;
        background-color: white;
    }

    .boton-cambio-de-pagina {
        border-radius: 4px;
        background-color: #36b08b;
        color: #3c3f41;
        font-weight: bold;
        margin: 4px;
    }

    .boton-cambio-de-pagina:hover {
        border: black 2px solid;
        background-color: #36b08b;
        color: black;
        font-weight: bold;
    }

    .numero-pagina:disabled{
        background-color: green;
    }

    .boton-cambio-de-pagina:disabled{
        visibility: hidden;
    }

    .numero-pagina {
        border-radius: 4px;
        background-color: #36b08b;
        color: #3c3f41;
        font-weight: bold;
        margin: 4px 2px;
    }

     .numero-pagina:hover {
        border-radius: 4px;
        color: black;
        font-weight: bold;
     }


</style>