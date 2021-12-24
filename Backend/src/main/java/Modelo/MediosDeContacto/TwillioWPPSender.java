package Modelo.MediosDeContacto;

import Utils.Exceptions.NumeroDesconocidoException;
import Modelo.Clientes.Persona;
import com.twilio.Twilio;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwillioWPPSender implements Notificador {
    static final String ACCOUNT_SID = "AC621381f1e6eda571e0d2c14ede21697e";
    static final String AUTH_TOKEN = "729a4f71c681be20d527cf6a6b193b6b";
    protected static final String nuestroTelefono = "14155238886";

    public void enviarNotificacion(Persona aQuien, Notificacion mensaje){
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            try{
                Message message = Message.creator(
                    new PhoneNumber("whatsapp:+"+aQuien.getTelefono()),
                    new PhoneNumber("whatsapp:+"+nuestroTelefono),
                    conFormato(mensaje)
                ).create();

            } catch (ApiException e){
                throw new NumeroDesconocidoException(aQuien.getTelefono());
            }
        }

    private String conFormato(Notificacion notificacion){
        return "*["+notificacion.getAsunto()+"]* "+notificacion.getCuerpo();
    }
}
