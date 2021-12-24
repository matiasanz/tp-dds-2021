package Presentacion;

import Presentacion.Backend.Router;
import Presentacion.ClienteLiviano.RouterCliente;
import spark.debug.DebugScreen;

public class Application {

    public static void main(String[] args) {
        //Mostrar stack trace en html en caso de excepcion no manejada:
        DebugScreen.enableDebugScreen();

        new Bootstrap().execute();
        new Router(8080).execute();
    }
}
