<template>
    <div>
        <p v-show="mascotas.length===0"><em>Aún no hay publicaciones de mascotas perdidas</em></p>

        <div id="contenedor_mascotas">
            <div v-for="mascota in mascotas" :key="mascota.id">
                <br>
                <galeria :fotos="mascota.fotos"></galeria>
                <ul>
                    <li><strong class="dato_mascota">Descripción:</strong> {{mascota.descripcion}}</li>
                    <li><strong class="dato_mascota">Se la encontró en:</strong> {{mascota.ubicacion.direccion}}</li>
                    <Mapa :latitud="mascota.ubicacion.lat" :longitud="mascota.ubicacion.long"></Mapa>
                </ul>
                <button class="boton_accion" v-show="!mascota.estadoSolicitud" @click.prevent="reportarReconocida(mascota)">
                    Es mía
                </button>
                <p>{{mascota.estadoSolicitud}}</p>
            </div>
        </div>
    </div>

</template>

<script>
    import api from '@/api/api'
    import Mapa from "@/components/Mapa";
    import Galeria from "@/components/galeria";

export default {
    components: {Galeria, Mapa},
    data: () => ({mascotas: []})
    , mounted() {
        api.getMascotasRescatadas(this.idOrg)
            .then(mascotas => {
                    mascotas.forEach(m=>m.estadoSolicitud=undefined)
                    this.mascotas = mascotas
                }
            )
    }
    , props: ['idOrg']
    , methods: {
        reportarReconocida(mascota){
            mascota.estadoSolicitud = 'Procesando...'
            api.notificarMascotaReconocida(this.idOrg, mascota)
                .then(()=>mascota.estadoSolicitud = 'Hemos notificado al rescatista, pronto se pondrá en contacto con ud.')
                .catch(()=>mascota.estadoSolicitud = 'Se produjo un error al procesar la solicitud, por favor vuelva a intentarlo más tarde')
        }
    }
}
</script>

<style scoped>

    #contenedor_mascotas {
        display: grid;
        grid-template-columns: 50% 50%;
        grid-gap: 10em;
    }
</style>