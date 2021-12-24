package Presentacion.Backend.Controladores.Organizaciones;

import Modelo.Organizaciones.Organizacion;
import Modelo.Usuarios.Usuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.OrganizacionService;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Utils.Exceptions.PermisosYaAsignadosException;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import Utils.Exceptions.UsuarioNoExisteException;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.naming.ldap.Control;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static Presentacion.Utils.Model.Models.toJson;
import static Presentacion.Utils.RequestUtils.getBody;

public class PermisosController implements Controller {
    private OrganizacionService orgService = new OrganizacionService();

    @Override
    public void routes(Service service) {
        service.get("/api/organizaciones/:idOrg/usuarios", this::getUsuarios);
        service.post("/api/organizaciones/:idOrg/administradores", this::agregarAdmin);
        service.post("/api/organizaciones/:idOrg/voluntarios", this::agregarVoluntario);
    }

    public String getUsuarios(Request req, Response res) {
        Organizacion org = extraerOrganizacion(req);

        List<Model> usuarios = repoUsuarios
            .getAll().stream()
            .map(u -> parseModelUsuario(u, org))
            .collect(Collectors.toList());

        return toJson(usuarios);
    }

    public String agregarAdmin(Request req, Response res) {
        return cambiarPermisos(req, "nuevo_admin", Organizacion::agregarAdministrador);
    }

    public String agregarVoluntario(Request req, Response res){
        return cambiarPermisos(req, "voluntario", Organizacion::agregarVoluntario);
    }

    private String cambiarPermisos(Request req, String key, BiConsumer<Organizacion, Usuario> cambio){
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        orgService.validarPermisosAdministrador(req, org);
        try{
            String nombreUsuario= body.stringValue(key);
            ValidadorNoNull.validar(key, nombreUsuario);

            return cambiarPermisos(nombreUsuario, org, cambio);

        } catch (ConversionFallidaException | DatosFaltantesException e){
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }

    }

    private String cambiarPermisos(String nombreUsuario, Organizacion org, BiConsumer<Organizacion, Usuario> cambio) {
        try{
            Usuario usuario = repoUsuarios.get(nombreUsuario);

            withTransaction(() -> {
                cambio.accept(org, usuario);
            });

            return parseModelUsuario(usuario, org).toJson();

        } catch(UsuarioNoExisteException e){
            throw ErrorHandler.haltWithMessage(404, e.getMessage());
        } catch(PermisosYaAsignadosException e){
            throw ErrorHandler.haltWithMessage(409, e.getMessage());
        }
    }

    private Model parseModelUsuario(Usuario usuario, Organizacion org){
        Model permisos = new Model()
            .append("administrador", org.esAdministrador(usuario))
            .append("voluntario", org.esVoluntario(usuario));

        return orgService.parseModelUsuario(usuario)
            .append("permisos", permisos);
    }
}
