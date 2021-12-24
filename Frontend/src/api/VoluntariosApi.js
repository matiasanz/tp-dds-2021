import axios from "axios";
import {barra_org} from "@/api/utils";


function barra_pendientes(idOrg, deQueTipo){
    return barra_org(idOrg)+'/pendientes/'+deQueTipo
}

function handleError(idOrg, response){
    switch (response.status){
        case 401: {
            window.location.replace('/organizaciones/'+idOrg)
            break
        }
        default: {
            alert('Error: '+response.data.error)
        }
    }
}

function getPublicacionesPendientes(idOrg, deQueTipo){
    return axios.get(barra_pendientes(idOrg, deQueTipo))
        .then(res=>res.data)
        .catch(err=>handleError(idOrg, err.response))
}

function homologarPublicacionesPendientes(idOrg, deQueTipo, idPublicacion, aprobada){
    return axios.put(barra_pendientes(idOrg, deQueTipo)+'/'+idPublicacion,  {aprobada: aprobada})
        .then(res=>res.data)
        .catch(err=>handleError(idOrg, err.response))
}

export default {
    getAdoptantesPendientes(idOrg){
        return getPublicacionesPendientes(idOrg, 'adoptantes')
    }
    , getDadoresPendientes(idOrg){
        return getPublicacionesPendientes(idOrg, 'dadores')
    }

    , homologarDador(idOrg, idPublicacion, aprobada){
        return homologarPublicacionesPendientes(idOrg, 'dadores', idPublicacion, aprobada)
    }

    , homologarAdoptante(idOrg, idPublicacion, aprobada){
        return homologarPublicacionesPendientes(idOrg, 'adoptantes', idPublicacion, aprobada)
    }
}