package Modelo.MediosDeContacto;

import Utils.Exceptions.NumeroDesconocidoException;
import Modelo.Clientes.Persona;
import Utils.Factory.ProveedorDeContactos;
import Utils.Factory.ProveedorDeNotificaciones;
import org.junit.Test;

public class NotificadorWspTest {
    private static long telefonoDePrueba = 5491130599461l;
    static TwillioWPPSender notificadorWsp =new TwillioWPPSender();
    static Notificacion mensajeDePrueba = ProveedorDeNotificaciones.mensajeDePrueba();

    /*
    public void enviarWPP(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(telefonoDePrueba);
        notificadorWsp.enviarNotificacion(cliente, new Notificacion("Patitas", "Holis"));
    }
    */

    @Test (expected = NumeroDesconocidoException.class)
    public void numeroExistePeroNoEstaRegistradoYFallaAlEnviar(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(541136463867l);
        notificadorWsp.enviarNotificacion(cliente, mensajeDePrueba);
    }

    @Test (expected = NumeroDesconocidoException.class)
    public void numeroNoExisteYFallaAlEnviar(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(0l);
        notificadorWsp.enviarNotificacion(cliente, mensajeDePrueba);
    }
}
