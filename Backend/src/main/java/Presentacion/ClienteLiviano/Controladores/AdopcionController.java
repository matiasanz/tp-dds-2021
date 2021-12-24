package Presentacion.ClienteLiviano.Controladores;

import Modelo.Clientes.Cliente;
import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.Publicacion;
import Presentacion.ClienteLiviano.Controladores.Template.ControllerCliente;
import Presentacion.Services.LoginService;
import Presentacion.Utils.Model.Model;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdopcionController extends ControllerCliente {
    private LoginService loginService = new LoginService();

    @Override
    public void routes(Service service, HandlebarsTemplateEngine engine) {
        service.get("/organizaciones/:idOrg/catalogo-adopcion", this::getMascotasEnAdopcion, engine);
        service.get("/organizaciones/:idOrg/adoptantes", this::getPublicacionesAdoptantes, engine);
    }

    private ModelAndView getPublicacionesAdoptantes(Request request, Response res){
        return getPublicaciones(request, Organizacion::getPublicacionesAdoptantes, "adoptantes.hbs");
    }

    private ModelAndView getMascotasEnAdopcion(Request request, Response response) {
        return getPublicaciones(request, Organizacion::getMascotasEnAdopcion, "catalogo-adopcion.hbs");
    }

    private <T extends Publicacion> ModelAndView getPublicaciones(Request request, Function<Organizacion, List<T>> getter, String viewName){
        Organizacion org = extraerOrganizacion(request);
        Model model = modeloBase(request)
            .append("idOrg", org.getId())
            .append("publicaciones",  getter.apply(org).stream().filter(m->m.getEstado()== EstadoValidacion.APROBADA).collect(Collectors.toList()))
            ;

        return new ModelAndView(model, viewName);
    }
}
