<template>
    <div>
        <div v-for="pregunta in preguntas" :key="pregunta.id">
            <label class="pregunta">¿{{pregunta.detalle}}?</label>
            <br>
            <input v-if="pregunta.libre_rta" type="text" v-model="value[pregunta.detalle]" list="opciones" class="select" @input="change">
            <select v-else v-model="value[pregunta.detalle]" class="select" @change="change">
                  <option v-for="(rta, index) in pregunta.rtas_predefinidas" :key="index" :value="rta">{{rta}}</option>
            </select>

            <datalist id="opciones">
                <option v-for="(rta, index) in pregunta.rtas_predefinidas" :key="index" :value="rta"/>
            </datalist>
        </div>
    </div>
</template>

<script>

export default {
    props: {
        value: Object
        , preguntas: Array
    }
    , methods: {
        change() {
            this.$emit('input', this.value)
        }
    }
}
</script>

<style scoped>

    .pregunta {
        font-size: 16px;
        padding-bottom: 1em;
    }

    .select {
        font-size: 16px;
        width: 200px;
        height: 25px;
        border-radius: 0.5em;
        color: #36b08b;
        border: #36b08b 1px solid;
        margin-bottom: 10px;
    }

</style>