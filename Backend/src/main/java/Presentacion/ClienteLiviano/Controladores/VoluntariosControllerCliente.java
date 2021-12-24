package Presentacion.ClienteLiviano.Controladores;

import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.Publicacion;
import Presentacion.ClienteLiviano.Controladores.Template.ControllerCliente;
import Presentacion.Services.VoluntariosService;
import Presentacion.Utils.Model.Model;
import spark.*;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.List;
import java.util.function.Function;

public class VoluntariosControllerCliente extends ControllerCliente {
    private VoluntariosService voluntariosService = new VoluntariosService();

    @Override
    public void routes(Service service, HandlebarsTemplateEngine engine){
        service.get("/organizaciones/:idOrg/menu-voluntarios", this::getMenuValidador, engine);
        service.post("/organizaciones/:idOrg/catalogo-adopcion/:idPublicacion", this::homologarAdopcion);
        service.post("/organizaciones/:idOrg/adoptantes/:idPublicacion", this::homologarAdoptante);
    }

    private Void homologarAdoptante(Request request, Response response){
        return homologarPublicacion(request, response, Organizacion::getPublicacionesAdoptantes, "/adoptantes");
    }

    private Void homologarAdopcion(Request request, Response response) {
        return homologarPublicacion(request, response, Organizacion::getMascotasEnAdopcion, "/catalogo-adopcion");
    }

    private <T extends Publicacion> Void homologarPublicacion(Request request, Response response, Function<Organizacion, List<T>> getter, String nextRoute){
        Organizacion org = extraerOrganizacion(request);
        try {
            EstadoValidacion estadoValidacion = request.queryMap("aprobada").booleanValue()? EstadoValidacion.APROBADA: EstadoValidacion.RECHAZADA;
            Long idPublicacion = Long.parseLong(request.params("idPublicacion"));
            voluntariosService.homologarPublicacion(request, idPublicacion, estadoValidacion, getter);

            String redirect = "/organizaciones/"+org.getId();

            redirect += estadoValidacion==EstadoValidacion.APROBADA?
                nextRoute : "/menu-voluntarios"
            ;

            response.redirect(redirect);

        } catch (Exception e){
            response.redirect("/organizaciones/"+org.getId()+"/menu-voluntarios");
        }

        return null;
    }

    private ModelAndView getMenuValidador(Request request, Response response) {
        Model modelo = modeloBase(request);
        try{
            Organizacion org = extraerOrganizacion(request);
            modelo
                .append("idOrg", org.getId())
                .append("adoptantes", voluntariosService.getPublicacionesPendientes(request, Organizacion::getPublicacionesAdoptantes))
                .append("dadores", voluntariosService.getPublicacionesPendientes(request, Organizacion::getMascotasEnAdopcion));

            return new ModelAndView(modelo, "voluntarios.hbs");

        } catch (HaltException e){
            response.redirect("/");
            return null;
        }
    }
}
