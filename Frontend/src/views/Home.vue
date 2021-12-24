<template>
    <div class="ppal">
        <h1 class="titulo">{{orgActual.nombre}}</h1>
        <div class="contenedor">
            <section class="col-izq">
                <h2>Otras Organizaciones</h2>

                <div v-if="organizaciones.length>0">
                    <div v-for="org in organizaciones.slice(0,8)" :key="org.id">
                        <a class="organizaciones" :href="'/organizaciones/'+org.id">{{org.nombre}}</a>
                    </div>
                    <div>
                        <a class="organizaciones" href="/organizaciones">Ver todas</a>
                    </div>
                </div>
                <p v-else>
                    <em>Cargando...</em>
                </p>
            </section>

            <div class="col-ppal">
                <div class="fila-1">
                    <section class="perdidas">
                        <a class="accion" href="/perdi-mascota">
                            <h3>Perdí mi mascota</h3>
                            <img src="@/assets/dogshadow3.png" id="imagen-missing">
                        </a>
                        <a class="accion"  href="/encontre-mascota">
                            <h3>Encontré la mascota de alguien</h3>
                            <img src="@/assets/DOGWITHPERSON.png" id="imagen-found">
                        </a>

                    </section>
                    <section class="adopcion">
                        <h2>Adopción</h2>

                        <div class="columnas">
                            <button class="btn btn-hover" onclick="window.location.href = ('/catalogo-adopcion')">Quiero Adoptar</button>
                            <button class="btn btn-hover" onclick="window.location.href = ('/dar-en-adopcion')">Quiero dar en adopción</button>
                        </div>
                    </section>
                </div>
                <aside class="aside">
                    <a class="anuncio" href="/mis-mascotas">
                        <h3>Registrá tu Mascota</h3>
                        <p>
                            ¡Mandanos tu dirección y te enviaremos una chapita!.
                        </p>
                    </a>
                </aside>
            </div>
            <div id="col-der">
                <a id="administrador" class="boton_resaltado" href="/admin" v-show="orgActual.permisos.administrador">Soy Administrador</a>
                <a id="voluntario" class="boton_resaltado" href="/voluntario" v-show="orgActual.permisos.voluntario">Soy Voluntario</a>
            </div>
        </div>

    </div>

</template>

<script>

    import api from "@/api/api";
    import cookies from "@/api/cookies";

    export default {
        data(){
            return {
                organizaciones: []
                , orgActual: {nombre: 'Rescate de Patitas', permisos:{}}
            }
        }
        , computed: {
            logueado(){
                return cookies.getCliente().sesionEnCurso
            }
        }
        , watch: {
            logueado(valorActual){
                if(valorActual){
                    this.refreshOrg()
                } else{
                    this.orgActual.permisos={}
                }
            }
        }
        , props: ['idOrg']
        , mounted(){
            this.refreshOrg()
                .then(()=>{
                    api.getOrganizaciones()
                        .then(orgs =>this.organizaciones = orgs)
                })
        }
        , methods: {
            refreshOrg(){
                return api.getOrganizacion(this.idOrg)
                    .then(orgActual=>{
                        this.orgActual = orgActual
                        this.$store.commit('setOrg', this.idOrg)
                    })
                    .catch(()=>{
                        window.location.replace('/organizaciones')
                    })
            }
        }
    }
</script>

<style scoped>

    .ppal{
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .contenedor{
        display: flex;
        text-align: center;
    }

    h2{
        color: #36b08b;
        text-align: center;
        font-weight: 700;
        font-family: 'Montserrat', sans-serif;
    }

    /****************************************************************+*/
    .col-izq{
        padding-left: 1em;
        padding-right: 1em;
        width: fit-content;
    }

    .col-izq ul{
        text-align: left;
    }

    a.organizaciones{
      font-weight: 700;
      font-family: 'Montserrat', sans-serif;
      text-transform: uppercase;
      text-decoration: none;
      display: inline-grid;
      margin-bottom: 7px;
      background-color: white;
      color: #0c9db3;
      border: 1px solid #0c9db3;
      border-radius: 20px;
      font-size: 14px;
      padding: 10px 25px 10px 25px;
      width: 160px;
    }

    a.organizaciones:hover {
      text-decoration: underline;
      background-color: #d3d3d3;
    }

    /****************************************************************+*/

    .col-ppal{
        display: flex;
        flex-direction: column;
        padding: 0 1em;
    }
    .col-ppal .fila-1{
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        margin: 1em 0 1.5em;

    }

    #imagen-missing {
        margin-top: 4.32em;
        margin-bottom: 4.32em;
        transform: scale(1.5);
    }

    #imagen-found {
      transform: scale(0.8);
    }

    .perdidas{
        align-items: center;
        justify-content: space-between;
        width: 50%;
        display: flex;
        grid-template-columns: auto auto auto;

    }

    .perdidas a{
        background-color: white;
        display: flex;
        flex-direction: column;
        word-wrap: break-word;
        width: 7em;
        border: black 1px solid;
        border-radius: 1em;

        margin-right: 1em;
        padding: 4em;
        font-family: Arial, sans-serif ;
        font-size: 12px;
    }

    .adopcion{
        background-color: #ffffff;
        border: black 2px solid;
        border-radius: 2em;
        margin-left: 1em;
        padding: 0 1em;
    }

    .adopcion > h2 {
      margin-top: 1.5em;
    }


    .adopcion div{
        width: 31em;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }

    .adopcion div button {
        border-radius: 0.5em;
        margin: 3em 1em;
        font-size: 18px;
    }

    .aside{
        border: black 1px solid;
        border-radius: 1em;
        background-image: linear-gradient(#ffffff, #afafaf);
        background-color: #e5e3e3;
        font-size: 18px;
    }

    .aside > p{
        text-align: justify;
    }

    .titulo{
      background: #ccc;
      background: radial-gradient(#0c9db3, #05c0dc);
      font-family: 'Montserrat', sans-serif;
      font-size: 2.5rem;
      line-height: 100%;
      -webkit-text-fill-color: transparent;
      -webkit-background-clip: text;
    }


    .btn {
      position: relative;
      display: block;
      height: 75px;
      width: 220px;
      border-radius: 4px;
      text-transform: uppercase;
      background-color: transparent;
      color: #36b08b;
      font-size: 16px;
      overflow: hidden;
      transition: all 500ms ease;
      border: 2px solid #159a93;
      margin-bottom: 40px;
      z-index: 0;
      font-weight: 700;
      font-family: 'Montserrat', sans-serif;
      cursor: pointer;
    }

    .btn-hover:hover {
      animation: shadow-pulse 1000ms 1;
      background-color: #20e2d7;
      color: #434343;
    }
    @keyframes shadow-pulse {
      0% {
        box-shadow: 0 0 0 0 rgba(32, 226, 215, 0.6);
      }
      100% {
        box-shadow: 0 0 8px 16px rgba(32, 226, 215, 0);
      }
    }

  /****************************************************************+*/
    #col-der{
        padding: 1em;
        width: 11em;
    }

    #col-der a:link, #col-der a:visited{
        border:#36b08b 2px solid;
        border-radius: 4px;
        padding: 0.5em;
        text-decoration: none;
        color: #36b08b;
        font-weight: bold;
        margin-bottom: 0.8em;
    }

    #col-der a:hover {
        animation: shadow-pulse 1000ms 1;
        background-color: #20e7f7;
        color: #434343;
    }

    h3 {
        text-decoration: none;
    }

    a.anuncio:link, a.anuncio:visited {
        text-decoration:none;
        font-style: italic;
        background-color: transparent;

    }

    a.anuncio:hover {
        color: #ff6961;
    }

    a.accion:link, a.accion:visited {
        text-decoration: none;
        color: #36b08b;

    }

    a.accion:hover {
        text-decoration: underline;
    }

</style>