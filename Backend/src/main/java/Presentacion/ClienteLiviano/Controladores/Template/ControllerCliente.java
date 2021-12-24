package Presentacion.ClienteLiviano.Controladores.Template;

import Modelo.Clientes.Cliente;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.LoginService;
import Presentacion.Utils.Model.Model;
import spark.Request;
import spark.Service;
import spark.template.handlebars.HandlebarsTemplateEngine;

public abstract class ControllerCliente implements Controller {
    private static final HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    private LoginService loginService = new LoginService();

    @Override
    public void routes(Service service){
        routes(service, engine);
    }

    public abstract void routes(Service service, HandlebarsTemplateEngine engine);

    protected Model modeloBase(Request request){
        Model modelo = new Model();
        loginService.ifLogueado(request,
            usuario -> {
                Cliente persona = usuario.getDatosPersonales();
                modelo.append("usuario", new Model().append("nombre_real", persona.getNombre()).append("apellido", persona.getApellido()));
            }
        );
        return modelo;
    }
}
