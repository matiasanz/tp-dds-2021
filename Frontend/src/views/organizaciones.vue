<template>
    <section>
        <!-- IMG PRINCIPAL -->
        <h1 id="titulo">¡Bienvenidos!</h1>

        <div id="cuerpo">
            <section id="orgs">
                <h2>Organizaciones</h2>
                <div class="indicaciones">
                    <h3>Seleccione con cuál desea operar</h3>
                    <div v-show="orgActual.isPresent">
                      <h3>Actualmente está operando con <em>{{orgActual.nombre}}</em></h3>
                    </div>
                </div>

                <div id="columnasOrg">
                    <div v-for="org in organizaciones" :key="org.id" >
                        <button class="btn-org" @click="elegirOrg(org)">
                            {{org.nombre}}
                        </button>
                    </div>
                </div>
            </section>
            <img id="img-ppal" src="@/assets/principal.png" alt="imagen-principal">
        </div>

    </section>
</template>

<script>
import api from '../api/api'
import cookies from "@/api/cookies";
export default {
    data(){ return {organizaciones: [], orgActual: {isPresent: false}} }
    , mounted(){
        api.getOrganizaciones()
            .then(orgs => this.organizaciones = orgs)
            .then(this.refreshOrgActual)
    }
    , methods :{
        elegirOrg(org){
            window.location.href = '/organizaciones/'+org.id
        }
        , refreshOrgActual(orgs){
            let org = orgs.find(o => o.id === cookies.getIdOrg())
            if(org!==undefined){
                org.isPresent = true
                this.orgActual = org
            }
        }
    }
}
</script>

<style>
    body {
        font-family: 'Montserrat', sans-serif;
        background-color: #ededed;
    }

    #cuerpo{
      display: flex;
      justify-content: space-around;
    }

    #titulo{
      font-size: 2.5rem;
      color: #36b08b;
      text-align: center;
    }

    #img-ppal{
      width: 30em;
    }

    .indicaciones{
        display: flex;
        justify-content: space-between;
    }

    h2 {
      color: #36b08b;
      text-align: left;
      display: block;
    }

    h3 {
      color: #444444;
      text-align: left;
      margin-bottom: 20px;
    }

    #columnasOrg {
      text-align: center;
      margin-top: 0;
      column-gap: 5em;
      columns: 4 2em;
      column-rule: 2px dotted #138D75;
    }

    .btn-org{
        font-family: 'Montserrat', sans-serif;
        display:inline-block;
        color: #0c9db3;
        border: 1px solid #0c9db3;
        border-radius: 20px;
        font-size: 14px;
        padding: 1em;
        margin-bottom: 20px;
        width: 15em;
    }

    .btn-org:hover {
        background-color: #d3d3d3;
        text-decoration: underline;
    }

</style>