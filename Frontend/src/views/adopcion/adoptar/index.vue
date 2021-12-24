<template>
    <div>
        <h1 id="titulo">Adoptar</h1>

        <div id="adoptante">

            <quiero-adoptar :idOrg="idOrg" :usuario="usuario"></quiero-adoptar>

            <h2 class="subtitulo">Catálogo de Mascotas en Adopción</h2>
            <MascotasEnAdopcion :idOrg="idOrg"/>

        </div>
    </div>

</template>

<script>
import cookies from "@/api/cookies";
import QuieroAdoptar from './quieroAdoptar'
import MascotasEnAdopcion from './mascotasEnAdopcion'

export default {
    name: "menu-adopcion",
    props: ['idOrg']
    , computed: {
        usuario(){
            return cookies.getCliente()
        }
    }
    , components:{QuieroAdoptar, MascotasEnAdopcion}
}

</script>

<style scoped>
    #titulo{
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

    #adoptante {
        display: grid;
        grid-template-columns: 50% 50%;
    }



</style>