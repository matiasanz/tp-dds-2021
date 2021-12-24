import store from "@/Plugins/storage";

let getters = store.getters

export default {
    getIdOrg(){
        return getters.getOrg
    },

    getCliente(){
        return getters.getCliente
    }
}
