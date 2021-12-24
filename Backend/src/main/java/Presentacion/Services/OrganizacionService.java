package Presentacion.Services;
import Modelo.Organizaciones.Organizacion;
import Modelo.Usuarios.Usuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.Template.Servicio;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import spark.Request;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class OrganizacionService implements Servicio {

    public void validarPermisosAdministrador(Request request, Organizacion org){
        validarPermisos(extraerUsuario(request), org, Organizacion::esAdministrador);
    }

    protected void validarPermisos(Usuario usuario, Organizacion org, BiPredicate<Organizacion, Usuario> permisosSuficientes){
        if (!permisosSuficientes.test(org, usuario)) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_UNAUTHORIZED, "Permisos insuficientes");
        }
    }

    public Model parseModelUsuario(Usuario usuario){
        return new Model()
            .append("nombre", usuario.getNombre())
            .append("id", usuario.getId())
            ;
    }

    public Model parseModel(Organizacion organizacion){
        return new Model()
            .append("nombre", organizacion.getNombre())
            .append("id", organizacion.getId())
            ;
    }
}
