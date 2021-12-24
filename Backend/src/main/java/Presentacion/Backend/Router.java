package Presentacion.Backend;

import Presentacion.Backend.Controladores.*;
import Presentacion.Backend.Controladores.Organizaciones.OrganizacionesController;
import Presentacion.Backend.Controladores.Organizaciones.PermisosController;
import Presentacion.Backend.Controladores.Organizaciones.VoluntariosController;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.ClienteLiviano.RouterCliente;
import Presentacion.Utils.Imagenes.ImagenDTO;
import Presentacion.Utils.Imagenes.ImagenHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.RequestUtils;
import Utils.Factory.ProveedorDeOrganizaciones;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Arrays;
import java.util.List;

public class Router {
    private int puerto;

    public Router(int puerto) {
        this.puerto = puerto;
    }

    public void execute(){
        Service service = Service.ignite().port(puerto);

        service.staticFileLocation("public");

        service.notFound(new Model("error", "not found").toJson());

        //Configuracion
        Triggers.configurar(service);

        //Rutas
        controllers().forEach(c->c.routes(service));
        new RouterCliente().execute(service);

        System.out.println("Servidor "+puerto+" iniciado correctamente");
    }

    public List<Controller> controllers(){
        return Arrays.asList(
            new LoginController()
            , new OrganizacionesController()
            , new MisMascotasController()
            , new MascotasRescatadasController()
            , new HogaresController()
            , new AdopcionController()
            , new PermisosController()
            , new VoluntariosController()
            , new NotificacionesController()
        );
    }
}