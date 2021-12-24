<template>
    <section class="sesion">
        <div class="sesion-iniciada" v-if="usuario.datosPersonales">
            <h1 id="nombre-usuario">{{usuario.nombre}}</h1>
            <div>
                <button id="logout" @click="logout">Cerrar Sesión</button>
            </div>
        </div>
        <div class="login" v-else>
            <form @submit.prevent="login" >
                <input class="pass-y-usuario" v-model="username" type="text" placeholder="Usuario" required/>
                <input class="pass-y-usuario" v-model="password" type="password" placeholder="Contraseña" required/>
                <input class="btn-login" type="submit" value="Iniciar Sesión"/>
            </form>

            <p>{{error}}</p>
        </div>
    </section>
</template>

<script>
    import LoginApi from "../api/LoginApi"
    import cookies from "@/api/cookies";

    export default {
        data(){
            return {
                username: ''
                , password: ''
                , error: ''
            }
        },

        mounted(){
            LoginApi.reautenticar()
                .catch(() => {
                    if(this.sesionEnCurso){
                        this.logout()
                    }
                })
        }

        , computed: {
            sesionEnCurso(){
                return this.usuario.datosPersonales
            }

            , usuario(){
                return cookies.getCliente()
            }
        },

        methods: {
            login() {
                LoginApi.iniciarSesion(this.username, this.password, this)
                    .then(() => this.iniciarSesion())
                    .catch(error => this.error = error.message)
            },

            logout(){
                LoginApi.logout(this)
                    .catch(error => alert(error.message))
            },

            iniciarSesion(){
                this.username=''
                this.password=''
            }
        }
    }

</script>

<style>

    .sesion{
        width: 30em;
    }

    .login{
        margin-top: 1em;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .pass-y-usuario{
        font-size: 15px;
        border: black 2px solid;
        border-radius: 0.5em;
    }

    .login p{
        margin-top: 5px;
    }

    .btn-login{
        width: auto;
        height: 23px;
        border: black 2px solid;
        border-radius: 10px;
    }

  /**/
    .sesion-iniciada{
        display: flex;
        align-items: center;
        justify-content: right;
        margin: 0.8em;
    }

    #nombre-usuario{
        margin: 0;
    }

    #logout {
        width: auto;
        height: 24px;
        border: black 2px solid;
        border-radius: 10px;
        margin-left: 1em;
    }

    .btn-login:hover, #logout:hover {
        width: auto;
        height: 24px;
        border: black 2px solid;
        border-radius: 40px;
        background-color: #00aae4;
        color: white;
    }

</style>
