package Utils.Exceptions;

import Modelo.Clientes.Persona;
import Modelo.MediosDeContacto.Notificacion;

public class MailNoEnviadoException extends RuntimeException{
    public MailNoEnviadoException(Notificacion mensaje, Persona contacto){
        super("no se pudo enviar a la cuenta "+contacto.getMail()+ " el mensaje ["+mensaje.getAsunto()+"] "+mensaje.getCuerpo());
    }
}
