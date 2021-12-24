import Vue from 'vue'
import Vuex from 'vuex'
import VuexPersistence from "vuex-persist";

Vue.use(Vuex)

 let usuarioDefault = () => ({
     nombre: ''
     , datosPersonales: false
     , sesionEnCurso: false
 })

const store = new Vuex.Store({
        state: {
            usuario: usuarioDefault(),
            idOrg: null

        },
        mutations: {
            setOrg(state, idOrg) {
                state.idOrg = idOrg
            },

            iniciarSesion(state, body) {
                state.usuario.nombre = body.nombre +' '+body.apellido
                state.usuario.datosPersonales = true
                state.usuario.sesionEnCurso = true
            },

            ingresarDatos(state, body) {
                state.usuario.nombre = body.nombre+' '+body.apellido
                state.usuario.datosPersonales = true
            },

            logout(state) {
                state.usuario.nombre = ' '
                state.usuario.datosPersonales = false
                state.usuario.sesionEnCurso = false
            }
        }

        , getters: {
            getCliente: (state) => state.usuario,
            getOrg: (state) => state.idOrg
        }

        , plugins: [
            new VuexPersistence({
                storage: window.localStorage,
            }).plugin
        ]
    })

export default store