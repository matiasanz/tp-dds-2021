<template>
    <div>
        <img :src="foto || require('@/assets/default.png')" alt="FOTO">
        <div id="botones_fotos" v-show="fotos.length>1">
            <button @click.prevent="retrocederPagina" :disabled="!hayPagsAntes">Anterior</button>
            <button @click.prevent="avanzarPagina" :disabled="!hayPagsDespues">Siguiente</button>
        </div>
    </div>
</template>

<script>

export default {
    name: 'galeria',
    props: {fotos: Array}
    , data: ()=>({index: 0})
    , computed:{
        foto(){
            return this.fotos[this.index]
        }
        , hayPagsAntes() {
            return this.index>0
        }
        , hayPagsDespues() {
            return this.index<this.fotos.length -1
        }

    }
    , methods:{
        avanzarPagina() {
            if(this.hayPagsDespues)
            this.index++
        }
        , retrocederPagina() {
            if(this.hayPagsAntes)
                this.index--
        }
    }
}
</script>

<style scoped>
    img{
        border-radius: 1em;
        border: black 2px solid;
        margin-left: 60px;
        width: 125px;
        height: 125px;
    }

    #botones_fotos {
        margin-top: 10px;
        margin-left: 40px;
    }

    button {
        border-radius: 4px;
        border: black 2px solid;
        color: white;
        font-weight: bold;
        background-color: #36b08b;
        width: 80px;
        margin-left: 2px;
    }

    button:hover {
        background-color: #20e2d7;
        color: #434343;
    }

</style>