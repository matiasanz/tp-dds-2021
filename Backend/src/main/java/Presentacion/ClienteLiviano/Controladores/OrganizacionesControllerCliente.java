package Presentacion.ClienteLiviano.Controladores;

import Modelo.Organizaciones.Organizacion;
import Presentacion.ClienteLiviano.Controladores.Template.ControllerCliente;
import Presentacion.Services.LoginService;
import Presentacion.Services.OrganizacionService;
import Presentacion.Utils.Model.Model;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.List;

public class OrganizacionesControllerCliente extends ControllerCliente {
    private OrganizacionService organizacionService = new OrganizacionService();
    private LoginService loginService = new LoginService();

    @Override
    public void routes(Service service, HandlebarsTemplateEngine engine) {
        service.get("/", this::getOrganizaciones, engine);
        service.get("/organizaciones/:idOrg", this::getOrganizacion, engine);
    }

    public ModelAndView getOrganizaciones(Request req, Response res) {
        Model modelo = modeloBase(req)
            .append("organizaciones", repoOrgs.getAll());

        return new ModelAndView(modelo,"organizaciones.hbs");
    }

    public ModelAndView getOrganizacion(Request req, Response res){
        Model modelo = modeloBase(req)
            .append("organizacion", extraerOrganizacion(req));
        return new ModelAndView(modelo, "organizacion.hbs");
    }

}
