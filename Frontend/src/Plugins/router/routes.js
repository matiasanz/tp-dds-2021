import Home from "@/views/Home";
import cookies from "@/api/cookies";

function getIdOrg(orElseRedirect = true){
    const idOrg = cookies.getIdOrg()

    if(idOrg===null && orElseRedirect){
        window.location.replace('/organizaciones')
    }

    return idOrg
}

const solo_idOrg = ()=>({idOrg: getIdOrg()})

const routes = [
    {
        path: '/organizaciones/:idOrg',
        component: Home
        , props: (route) => ({idOrg: parseInt(route.params.idOrg)})
    },
    {
        path: '/organizaciones'
        , alias: '/'
        , component: ()=>import('@/views/organizaciones'),
        props: ()=> ({idOrg: getIdOrg(false)})
    },
    {
        path: '/perdi-mascota'
        , component: () => import('@/views/perdi mascota')
        , props: solo_idOrg
    }
    , {
        path: '/encontre-mascota'
        , component: ()=>import('@/views/encontre mascota'),
        props: solo_idOrg
    },
    {
        path: '/catalogo-adopcion',
        component: () => import( '@/views/adopcion/adoptar')
        , props: solo_idOrg
    },
    {
        path: '/dar-en-adopcion',
        component: () => import( '@/views/adopcion/dar')
        , props: solo_idOrg
    },
    {
        path: '/hogares-de-transito',
        component: () => import('@/views/hogaresDeTransito')
    },

    {
        path: '/admin',
        component: () => import('@/views/admin/index')
        , props: solo_idOrg
    },
    {
        path: '/mis-mascotas',
        component: () => import('@/views/misMascotas')
        , props: solo_idOrg
    },
    {
        path: '/voluntario'
        , component: ()=>import('@/views/voluntario')
        , props: solo_idOrg
    }
    ,{
        path: '*',
        component: () => import('@/views/NotFound')
        , props: route => ({ruta: route.path})
    }
]

export default routes