<template>
    <div>
        <h1>404 - Pifiaste</h1>
        <p>La ruta '{{ruta}}' no existe o aún no ha sido implementada. Gracias, vuelva prontos</p>
        <img src="@/assets/404.jpg">
    </div>
</template>

<script>
export default {
    name: "NotFound"
    , props: {
        'ruta': String
    }
}
</script>

<style scoped>

</style>