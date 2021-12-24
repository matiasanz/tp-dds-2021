<template>
    <div>
        <h1 id="titulo">Encontré una Mascota</h1>

        <section>
            <button class="btn-mascota-encontrada" v-if="!vistaPublicacion" @click="vistaPublicacion=true">Publicar Mascota Encontrada</button>
            <div v-else>
                <button @click.prevent="vistaPublicacion=false" class="boton-cancelar">Cancelar</button>

                <h2 class="subtitulo">Publicar Mascota Encontrada</h2>
                <EncontreMascota :id-org="idOrg"></EncontreMascota>
            </div>
        </section>

        <aside>
            <p>Si no la podes cuidar, te recomendamos estos <a id="hogares" href="/hogares-de-transito">Hogares Transitorios</a></p>
        </aside>

        <section>
            <h2 id="mascotas-buscadas">Mascotas Buscadas</h2>
            <MascotasPerdidas :idOrg="idOrg"/>
        </section>
    </div>
</template>

<script>
    import MascotasPerdidas from "./mascotas perdidas";
    import EncontreMascota from "./encontreMascota"

    export default {
        name: "organizacion"
        , data: () => ({
            vistaPublicacion: false
        })
        , components: {
            MascotasPerdidas, EncontreMascota
        }
        , props: ['idOrg']
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

    .btn-mascota-encontrada {
        font-size: 16px;
        font-weight: bold;
        border: #36b08b 2px solid;
        color: #36b08b;
        background-color: transparent;
        padding: 0.5em;
        border-radius: 4px;
    }

    .btn-mascota-encontrada:hover {
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

    .boton-cancelar {
        font-size: 16px;
        font-weight: bold;
        border: #36b08b 2px solid;
        color: #36b08b;
        background-color: transparent;
        padding: 0.5em;
        border-radius: 4px;
    }

    .boton-cancelar:hover {
        background-color: #d3d3d3;
    }

    #mascotas-buscadas {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
        margin-bottom: 25px;
    }

     #hogares:link, #hogares:visited {
        text-decoration: none;
        color: #36b08b;
        font-style: italic;
    }

    #hogares:hover {
        text-decoration: underline;
    }

    .subtitulo {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;

    }

</style>