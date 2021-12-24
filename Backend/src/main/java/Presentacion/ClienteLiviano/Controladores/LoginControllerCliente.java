package Presentacion.ClienteLiviano.Controladores;

import Modelo.Usuarios.Usuario;
import Presentacion.ClienteLiviano.Controladores.Template.ControllerCliente;
import Presentacion.Services.LoginService;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.function.Consumer;

public class LoginControllerCliente extends ControllerCliente {
    private LoginService loginService = new LoginService();

    @Override
    public void routes(Service service, HandlebarsTemplateEngine engine) {
        service.post("/login", this::login);
        service.get("/logout", this::logout);
    }

    private Void logout(Request request, Response response) {
        response.redirect("/");
        loginService.logout(request);
        return null;
    }

    private Void login(Request request, Response response) {
        response.redirect("/");
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if(username!=null && password!=null){
            loginService.iniciarSesion(username, password, request);
        }

        return null;
    }
}
