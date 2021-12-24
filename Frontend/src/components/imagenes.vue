<template>
    <div class="centrado">
        <label for="file-input">
            <img id="subir-imagen" :src="require('@/assets/camara.png')" alt="SUBIR FOTOS"/>
        </label>

        <input hidden id="file-input" type="file" multiple accept="image/*" @change="onFileChange($event)">

        <section v-show="value.length>0" class="img-ingresadas">
            <div class="img-ingresada" v-for="(imagen, index) in value" :key="index">
                <img id="imagen_a_ingresar" :src="imagen.src" :alt="'FOTO '+index"/>
                <button id="boton_eliminar" @click.prevent="removeImage(index)">Eliminar</button>
            </div>
        </section>
    </div>
</template>

<script>

    class Image{
        constructor(file, src) {
            this.name = file.name
            this.type = file.type.split('/')[1]
            this.src = src
        }

        content(){
            return this.src.split(',')[1]
        }
    }

    export default {
        name: "InputIMG"
        ,props: {
            value: Array
        }
        , methods: {
            onFileChange(e) {
                let files = e.target.files || e.dataTransfer.files;

                for(let i=0; i<files.length; i++){
                    this.createImage(files[i])
                }
            },
            createImage(file) {
                let reader = new FileReader();
                reader.onload = (e) => {
                    this.value.push(new Image(file, e.target.result));
                    this.change()
                };

                reader.readAsDataURL(file);
            },

            removeImage(index) {
                this.value.splice(index, 1);
                this.change()
            }
            , change(){
                this.$emit('input', this.value)
            }
        }
    }
</script>

<style scoped>
    .centrado{
        display: flex;
        flex-direction: column;
        align-items: center;
        width: fit-content;
        width: 280px;

    }

    #subir-imagen{
        background-color: gray;
        border-radius: 1em;
        max-height: 3em;
        max-width: 3em;
        padding: 1em;
    }

    #subir-imagen:hover, #boton_eliminar:hover{
        background-color: #d3d3d3;
        cursor: pointer;
    }

    #boton_eliminar {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        margin: 0 5px;
        background-color: white;
    }

    #imagen_a_ingresar {
        width: 60px;
        height: 60px;
        margin: 10px auto;
    }


    .img-ingresadas{
        display: grid;
        grid-template-columns: repeat(3, auto);
        grid-gap: 1em;
        background-color: #ece5e570;
        padding: 0.5em;
        border-radius: 1em;
   }

    .img-ingresadas div{
        display: flex;
        align-items: center;
        flex-direction: column;
    }
</style>