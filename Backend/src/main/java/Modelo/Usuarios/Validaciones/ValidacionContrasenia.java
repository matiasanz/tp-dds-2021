package Modelo.Usuarios.Validaciones;

import Modelo.Usuarios.Usuario;

public abstract class ValidacionContrasenia {
    public abstract String getMensaje();
    protected abstract boolean contraseniaValida(String contrasenia);

    public boolean apruebaUsuario(Usuario usuario){
        return contraseniaValida(usuario.getContrasenia());
    }
}