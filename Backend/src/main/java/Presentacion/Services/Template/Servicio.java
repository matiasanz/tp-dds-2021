package Presentacion.Services.Template;

import Modelo.Clientes.Cliente;
import Modelo.Organizaciones.Organizacion;
import Modelo.Usuarios.Usuario;
import Presentacion.Utils.Imagenes.ImagenDTO;
import Presentacion.Utils.Imagenes.ImagenHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Repositorios.*;
import Repositorios.RepoUsuarios.RepoUsuarios;
import Repositorios.RepoUsuarios.RepoUsuariosDB;
import Repositorios.Template.Transaccional;
import Utils.Exceptions.IdDesconocidoException;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import spark.Request;

import java.net.HttpURLConnection;
import java.util.List;

import static Presentacion.Utils.ErrorHandler.haltException;
import static Presentacion.Utils.RequestUtils.getParams;

public interface Servicio extends Transaccional {
    RepoOrganizaciones repoOrgs = new RepoOrganizaciones();
    RepoUsuarios repoUsuarios = new RepoUsuariosDB();
    RepoPreguntas repoPreguntas = new RepoPreguntas();
    RepoClientes repoClientes = new RepoClientes();

    SessionManager<Usuario> usuarios = new SessionManager<>(repoUsuarios);
    SessionManager<Cliente> clientes = new SessionManager<>(repoClientes);

    ImagenHandler imagenHandler = new ImagenHandler();

    default Organizacion extraerOrganizacion(Request req){
        try {
            Long idOrg = getParams(req).longValue("idorg");
            return withTransaction(()->repoOrgs.get(idOrg));
        }catch (ConversionFallidaException | IdDesconocidoException e) {
            throw haltException(HttpURLConnection.HTTP_NOT_FOUND);
        }
    }

    default Cliente extraerCliente(Request request){
        return usuarios.sesionEnCurso(request)? extraerUsuario(request).getDatosPersonales()
            : clientes.get(request, "primero debe ingresar sus datos");
    }

    default Usuario extraerUsuario(Request request){
        return usuarios.get(request, "Para acceder al contenido, primero debe autenticarse como usuario");
    }

    default List<String> recibirFotos(String key, Organizacion org, Model body){
        List<ImagenDTO> recibidas = body.listValue(key, ImagenDTO.class);
        ValidadorNoNull.validar(key, recibidas);

        return imagenHandler.recibirImagenes(recibidas, org);
    }
}
