<template>
    <div>
        <h1 id="titulo">Perdí Mascota</h1>

        <div id="contenedor_form_y_mascotas_encontradas">
            <section>
                <h2>Publicar Búsqueda</h2>
                <FormMascota :id-org="idOrg" :perdida="true" @creada="redirect"></FormMascota>
            </section>
            <section class="mascotas-encontradas">
                <h2>Mascotas Encontradas</h2>
                <MascotasRescatadas :idOrg="idOrg"/>
            </section>
        </div>
    </div>
</template>

<script>
    import MascotasRescatadas from "./MascotasRescatadas";
    import FormMascota from "@/components/crear mascota";

    export default {
        name: "organizacion"

        , components: {
            FormMascota,
            MascotasRescatadas
        }
        , props: ['idOrg']
        , methods: {
            redirect(){
                window.location.href = '/mis-mascotas'
            }
        }
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

    h2 {
        font-weight: 700;
        color: #36b08b;
        font-size: 25px;
        margin-bottom: 25px;
    }

    #contenedor_form_y_mascotas_encontradas {
        display: grid;
        grid-template-columns: 20% 80%;
    }

    .mascotas-encontradas{
        display: flex;
        flex-direction: column;
        align-items: center;
    }



</style>