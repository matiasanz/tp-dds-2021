package Presentacion.ClienteLiviano;

import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.ClienteLiviano.Controladores.AdopcionController;
import Presentacion.ClienteLiviano.Controladores.LoginControllerCliente;
import Presentacion.ClienteLiviano.Controladores.OrganizacionesControllerCliente;
import Presentacion.ClienteLiviano.Controladores.VoluntariosControllerCliente;
import spark.Service;
import java.util.Arrays;
import java.util.List;

public class RouterCliente {

    public void execute(Service service){
        controllers().forEach(c->c.routes(service));
    }

    private List<Controller> controllers(){
        return Arrays.asList(
            new OrganizacionesControllerCliente()
            , new VoluntariosControllerCliente()
            , new LoginControllerCliente()
            , new AdopcionController()
        );
    }
}
