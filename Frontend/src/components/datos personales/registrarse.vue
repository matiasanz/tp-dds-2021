<template>
    <div>
        <form @submit.prevent="registrarse">
            <input type="text" v-model="username" placeholder="Usuario..." required>
            <br>
            <input type="password" v-model="password" placeholder="Contraseña..." required>
            <br>
            <input type="password" v-model="password_repetida" placeholder="Repita su contraseña..." required>
            <br>
            <input type="submit" value="Registrarme">
        </form>
        <section v-show="errores.length>0">
            <h4>Errores detectados:</h4>
            <ul>
                <li v-for="(error, index) in errores" :key="index">
                    <p>{{error}}</p>
                </li>
            </ul>
        </section>
    </div>
</template>

<script>

import LoginApi from "@/api/LoginApi";

export default {
    name: "registrarse"
    , data(){
        return {
            username: ''
            , password: ''
            , password_repetida: ''
            , errores: []
        }
    }

    , methods: {
        registrarse(){
            if(this.password !== this.password_repetida){
                this.errores = ['Las contraseñas ingresadas no coinciden']
                return
            }

            LoginApi.registrarse(this.username, this.password, this)
                .catch(error => this.errores = error.errores)
        }
    }


}
</script>

<style scoped>

</style>