<template>
    <div>
        <form @submit.prevent="seleccionar" autocomplete="off">
            <div id="contenedor_boton_y_tipo_usuario">
                <input class="dato" v-model="nombreUsuario" type="search" name="busquedaUsuarios" :list="'usuarios_list_'+rol" required>

                <input type="submit" class="boton_confirmar" value="Confirmar">
            </div>
            <datalist :id="'usuarios_list_'+rol" >
                <option  v-for="usuario in usuariosSinPermisos(rol)" :key="usuario.id">
                    {{usuario.nombre}}
                </option>
            </datalist>
        </form>

        <p class="error">{{error}}</p>

        <div v-show="usuarios.find(u=>u.permisos[rol])">
            <h3 class="subtitulo_a_22px">Actuales:</h3>
            <ul>
                <li id="usuarios" v-for="usuario in usuariosConPermisos(rol)" :key="usuario.id">
                    {{usuario.nombre}}
                </li>
            </ul>
        </div>
    </div>

</template>

<script>
export default {
    name: "GestorRol",
    data(){
        return {
            nombreUsuario: ''
            , error: ''
        }
    },
    props: {
        rol: String,
        enviarSeleccion: Function
        , usuarios: Array
    }
    , methods: {
        seleccionar(){
            if(!confirm("Estás seguro de querer nombrar "+this.rol+" a "+this.nombreUsuario+"?")){
                return
            }

            return this.enviarSeleccion(this.nombreUsuario)
                .then(nuevoUsuario => {
                    this.usuarioConNombre(this.nombreUsuario).permisos =nuevoUsuario.permisos
                    this.nombreUsuario=''
                })
                .catch(error => this.error = error.message)
        }
        , usuarioConNombre(nombre){
            return this.usuarios.find(u=>u.nombre===nombre)
        }
        , usuariosConPermisos(rol){
            return this.usuarios.filter(u=>u.permisos[rol])
        }
        , usuariosSinPermisos(rol){
            return this.usuarios.filter(u=>!u.permisos[rol])
        }
    }
}
</script>

<style scoped>

    .boton_confirmar {
        border: #36b08b 2px solid;
        color: #36b08b;
        font-weight: bold;
        border-radius: 0.5em;
        font-size: 15px;
        padding: 5px;
        background-color: transparent;
        margin: 3px 5px;
        display: block;
    }

    .boton_confirmar:hover {
        background-color: #20e2d7;
        color: #434343;
    }

    .dato {
         margin: 0.2em 0.1em;
         font-size: 15px;
         width: 200px;
         border-radius: 0.5em;
         color: #36b08b;
         border: #36b08b 1px solid;
    }

    #contenedor_boton_y_tipo_usuario
    {
        display:flex;
    }

    .subtitulo_a_22px {
        font-weight: 700;
        color: #36b08b;
        font-size: 22px;
        font-style: italic;
    }

    #usuarios {
        font-style: italic;
        font-list-style: square;
    }
</style>