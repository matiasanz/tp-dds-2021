package Presentacion.Backend.Controladores.Organizaciones;

import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.Publicacion;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Usuarios.Usuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.VoluntariosService;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.RequestUtils;
import Utils.Exceptions.PermisosInsuficientesException;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.PublicacionInexistenteException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Presentacion.Utils.Model.Models.toJson;

public class VoluntariosController implements Controller {
    private VoluntariosService voluntariosService = new VoluntariosService();

    @Override
    public void routes(Service service){
        service.get("/api/organizaciones/:idOrg/pendientes/adoptantes", this::getAdoptantesPendientes);
        service.get("/api/organizaciones/:idOrg/pendientes/dadores", this::getDadoresPendientes);
        service.put("/api/organizaciones/:idOrg/pendientes/adoptantes/:idPublicacion", this::homologarAdoptante);
        service.put("/api/organizaciones/:idOrg/pendientes/dadores/:idPublicacion", this::homologarDador);
    }

    private String getDadoresPendientes(Request request, Response response) {
        List<PublicacionAdopcion> pendientes = voluntariosService.getPublicacionesPendientes(request, Organizacion::getMascotasEnAdopcion);
        return toJson(pendientes);
    }

    private String getAdoptantesPendientes(Request request, Response response) {
        return toJson(voluntariosService.getPublicacionesPendientes(request, Organizacion::getPublicacionesAdoptantes));
    }

    private String homologarDador(Request request, Response response) {
        return homologarPublicacion(request, Organizacion::getMascotasEnAdopcion);
    }

    private String homologarAdoptante(Request request, Response response) {
        return homologarPublicacion(request, Organizacion::getPublicacionesAdoptantes);
    }

    private <T extends Publicacion> String homologarPublicacion(Request request, Function<Organizacion, List<T>> getter){
        Model body = RequestUtils.getBody(request);
        try{
            Long id = RequestUtils.getParams(request).longValue("idpublicacion");
            EstadoValidacion estado = body.getBoolean("aprobada")? EstadoValidacion.APROBADA: EstadoValidacion.RECHAZADA;
            T p = voluntariosService.homologarPublicacion(request, id, estado, getter);
            return toJson(p);
        } catch (ConversionFallidaException e){
            throw ErrorHandler.haltWithMessage(400, e.getMessage());
        } catch (PublicacionInexistenteException e){
            throw ErrorHandler.haltWithMessage(404, e.getMessage());
        }
    }
}
