package Modelo.MediosDeContacto;

import Utils.Exceptions.NumeroDesconocidoException;
import Utils.Factory.ProveedorDeContactos;
import Utils.Factory.ProveedorDeNotificaciones;
import org.junit.Test;

import Modelo.Clientes.Persona;
public class NotificadorSMSTest {
    private long telefonoDePrueba = 541130599461l;
    TwillioSMSSender notificadorSMS =new TwillioSMSSender();
    Notificacion mensajeDePrueba = ProveedorDeNotificaciones.mensajeDePrueba();

/*
    public void enviaSMSCorrectamente(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(telefonoDePrueba);
        notificadorSMS.enviarNotificacion(cliente, new Notificacion("patitas", "estoy probando los mensajes"));
    }
*/

    @Test (expected = NumeroDesconocidoException.class)
    public void numeroExistePeroNoEstaRegistradoYFallaAlEnviar(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(5491136463867l);
        notificadorSMS.enviarNotificacion(cliente, mensajeDePrueba);
    }

    @Test (expected = NumeroDesconocidoException.class)
    public void numeroNoExisteYFallaAlEnviar(){
        Persona cliente = ProveedorDeContactos.contactoConTelefono(0l);
        notificadorSMS.enviarNotificacion(cliente, mensajeDePrueba);
    }

}
