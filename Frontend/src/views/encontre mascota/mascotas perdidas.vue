<template>
    <div id="contenedor_mascotas_buscadas">
        <p class="mensaje" v-show="mascotas.length===0"><em>Todavía no hay ninguna mascota publicada</em></p>

        <div v-for="mascota in mascotas" :key="mascota.id">
            <h3 class="subtitulo_a_22px">{{mascota.nombre}}</h3>
            <galeria :fotos="mascota.fotos"></galeria>
            <ul>
                <li><strong class="dato">Apodo:</strong> {{mascota.apodo}}</li>
                <li><strong class="dato">Edad Aproximada:</strong> {{mascota.edad_aproximada}} años</li>
                <li><strong class="dato">Descripcion:</strong> {{mascota.descripcion}}</li>
                <li><strong class="dato">Especie:</strong> {{mascota.especie | capitalize}}</li>
                <li v-show="mascota.caracteristicas.length>0">
                    <strong class="dato">Características:</strong>
                    <ul>
                        <li v-for="(value, key) in mascota.caracteristicas" :key="key">
                            <strong class="dato">¿{{key}}?</strong> {{value}}
                        </li>
                    </ul>
                </li>
            </ul>
            <button class="boton_accion" v-show="!mascota.estadoSolicitud" @click.prevent="reportarAvistaje(mascota)">
                Reportar Avistaje
            </button>
            <p>{{mascota.estadoSolicitud}}</p>
        </div>
    </div>

</template>

<script>
import api from "@/api/api"
import Galeria from "@/components/galeria";

export default {
    components: {Galeria},
    data: () => ({mascotas: []})
    , mounted(){
        api.getMascotasPerdidas(this.idOrg)
            .then(mascotas => {
                mascotas.forEach(m=>m.estadoSolicitud = undefined)
                this.mascotas = mascotas
            })
    }
    , props: ['idOrg']
    , methods:{
        reportarAvistaje(mascota){
            mascota.estadoSolicitud = 'Procesando...'
            api.notificarMascotaEncontrada(this.idOrg, mascota)
                .then(()=>mascota.estadoSolicitud = 'Hemos notificado al dueño, pronto se pondrá en contacto con ud.')
                .catch(() => mascota.estadoSolicitud = 'Se produjo un error al procesar la solicitud. Por favor vuelva a intentarlo más tarde')
        }
    }
}
</script>

<style scoped>

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 20px;
        font-style: italic;
        text-transform: capitalize;
    }

    .dato{
        border: none;
        text-decoration: underline;
        color: #36b08b;
    }

    #contenedor_mascotas_buscadas {
        display: grid;
        padding-left: 3em;
        padding-right: 3em;
        grid-template-columns: auto auto auto auto;
        grid-gap: 15px;
    }


</style>