package Modelo.MediosDeContacto;

import Utils.Exceptions.MailNoEnviadoException;
import Modelo.Clientes.Persona;
import Utils.Factory.ProveedorDeContactos;
import org.junit.Test;

public class NotificadorMailTest {

    JavaMailSender notificadorMail =new JavaMailSender();

/*
    @Test
    public void enviaMailCorrectamente(){
        Persona cliente = ProveedorDeContactos.contactoConMail("salvamendez@gmail.com");
        notificadorMail.enviarNotificacion(cliente, new Notificacion("Test dds 2021", "Tu TP funciona 10 puntos, vas a promocionar"));
    }
*/

    @Test (expected = MailNoEnviadoException.class)
    public void siDireccionNoExisteNoEnvia(){
        Persona cliente = ProveedorDeContactos.contactoConMail("lalaldauihuiuidfhuiduila");
        notificadorMail.enviarNotificacion(cliente, new Notificacion("Test dds", "Paga raton"));
    }

}
