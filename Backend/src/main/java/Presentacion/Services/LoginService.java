package Presentacion.Services;

import Modelo.Clientes.Cliente;
import Modelo.Usuarios.Usuario;
import Presentacion.Services.Template.Servicio;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Utils.Exceptions.ContraseniaIncorrectaException;
import Utils.Exceptions.UsuarioNoExisteException;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;
import java.util.function.Consumer;

import static Presentacion.Utils.RequestUtils.getBody;

public class LoginService implements Servicio {
    public Model iniciarSesion(String nombreUsuario, String contrasenia, Request request) {
        try {
            Usuario usuario = repoUsuarios.get(nombreUsuario);
            usuario.autenticar(contrasenia);

            usuarios.guardarCredenciales(usuario, request);

            return datosUsuario(usuario.getDatosPersonales());
        } catch (UsuarioNoExisteException | ContraseniaIncorrectaException e) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_UNAUTHORIZED, "El usuario y/o la contraseña son incorrectos");
        }
    }

    public void logout(Request request) {
        extraerCliente(request);
        usuarios.quitarCredenciales(request);
        clientes.quitarCredenciales(request);
    }

    public Model datosUsuario(Cliente cliente){
        return new Model("nombre", cliente.getNombre())
            .append("apellido", cliente.getApellido());
    }

    public void ifLogueado(Request req, Consumer<Usuario> consumer){
        if(usuarios.sesionEnCurso(req)){
            consumer.accept(usuarios.find(req).get());
        }
    }
}
