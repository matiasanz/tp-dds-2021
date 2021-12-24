<template>
    <div id="app">
        <header>
            <a id="logo" :href="'/organizaciones/'+idOrg" >
                <img src="@/assets/logo.jpeg" alt="logo-header">
            </a>
            <nav>
                <a href="/hogares-de-transito">Hogares de Tránsito</a>
            </nav>
            <Login/>
        </header>

        <main>
            <router-view/>
        </main>

        <footer>
            <p>© Copyright Grupo 9 {{year}}</p>
        </footer>
    </div>
</template>

<script>
    import Login from "./components/login.vue";
    import axios from "axios";
    import cookies from "@/api/cookies";

    export default {
        data () {
            return {
                year: new Date().getFullYear()
            }
        }
        , mounted() {
            axios.create({
                withCredentials: true
            })
        },
        computed: {
            idOrg(){
                return cookies.getIdOrg() || ''
            }
        }
        , components: {Login} //nombre default el de la variable
    }
</script>

<style>
    @import url('https://fonts.googleapis.com/css?family=Montserrat&family=Poppins:wght@700&display=swap');
    @import "/botones.css";

    body{
        margin-top: 0;
        margin-right: 0;
        margin-left: 0;
/*        background-color: #ededed;*/
    }

    header{
        display: flex;
        background-color: #36b08b;
        justify-content: space-around;
        align-items: center;
        height: 65px;
        width:100%;
        border: black;
    }

    header nav{
        display: flex;
        flex-direction: row;

    }

    header nav a{
        font-family: 'Montserrat', sans-serif;
        font-size: 16px;
        color: white;
        padding: 1.2em 0.5em;
        border-radius: 1em;
    }

    header nav a:hover{
        background-color: #138D75;
    }

    #logo img{
        height: 4em;
    }

    #logo img:hover{
        border-radius: 1em;
        filter: grayscale(50%);
    }

    main, footer{
        padding-left: 2em;
        padding-right: 2em;
    }

    footer{
        width: 100%;
    }

    footer {
        font-family: 'Montserrat', sans-serif;
        position: fixed;
        bottom: 1px;
        z-index: 0;
        color: rgba(0, 0, 0, 0.38);
        text-align: center;
    }

    input[type=submit], button:hover{
        cursor: pointer;
    }

</style>