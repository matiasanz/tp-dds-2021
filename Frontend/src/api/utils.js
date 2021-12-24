export function barra_org(id) {
    return '/api/organizaciones/'+id
}

export default {
    redirectToHome: () => {
        window.location.replace('/')
    }

    , throw_error(msj){
        throw new Error(msj)
    },

}