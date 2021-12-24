package Modelo.Usuarios.Validaciones;

import Modelo.Usuarios.Usuario;

public class ValidacionUsuarioNoContenidoEnContrasenia extends ValidacionContrasenia{

    private String usuario;

    public ValidacionUsuarioNoContenidoEnContrasenia(){}

    private ValidacionUsuarioNoContenidoEnContrasenia(String duenio){
        usuario = duenio;
    }

    @Override
    public String getMensaje() {
        return "La contrasenia no debe incluir el nombre de usuario";
    }

    @Override
    public boolean apruebaUsuario(Usuario usuario) {
        return new ValidacionUsuarioNoContenidoEnContrasenia(usuario.getNombre()).contraseniaValida(usuario.getContrasenia());
    }

    @Override
    protected boolean contraseniaValida(String contrasenia) {
        if(usuario==null) throw new RuntimeException("Modelo.Usuario no seteado");
        return !contrasenia.toLowerCase().contains(usuario.toLowerCase());
    }
}
