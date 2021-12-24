<template>
    <iframe :src="'https://maps.google.com/maps?q=' + latitud + ',' +  longitud + '&t=&z=15&ie=UTF8&iwloc=&output=embed'" />
</template>

<script>
export default {
    name: "Mapa"
    , props: ['latitud', 'longitud']
}
</script>

<style scoped>

</style>