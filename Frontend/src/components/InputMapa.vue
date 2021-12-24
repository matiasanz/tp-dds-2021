<template>
    <div>
        <GmapMap
            class="map"
            :center="centro"
            :zoom="15"
            map-type-id="terrain"
            @click="reubicarMarcador"
        >
            <GmapMarker
                :position="posicion"
            ></GmapMarker>
        </GmapMap>
    </div>
</template>

<script>

export default {
    name: "InputMapa"
    , props: ['value']
    , data: () => ({
        centro: {
            lat: -34.6037345,
            lng: -58.3837591
        }
    })
    , computed: {
        posicion(){
            return {lat: this.value.lat, lng: this.value.long}
        }
    }
    ,
    methods: {
        reubicarMarcador(data) {
            this.value.lat = data.latLng.lat();
            this.value.long = data.latLng.lng();
            this.$emit('input', this.value)
        }
    }
}
</script>

<style scoped>
.map {
    height: 20em;
    width: 40em;
}
</style>