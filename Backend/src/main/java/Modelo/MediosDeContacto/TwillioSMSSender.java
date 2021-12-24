package Modelo.MediosDeContacto;

import Modelo.Clientes.Persona;
import Utils.Exceptions.NumeroDesconocidoException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;

public class TwillioSMSSender implements Notificador {

    private static final String ACCOUNT_SID = "AC621381f1e6eda571e0d2c14ede21697e"; //el SID del dashboard de twilo
    private static final String AUTH_TOKEN = "729a4f71c681be20d527cf6a6b193b6b";
    private static final String telefonoNuestro = "15852073818";
    //el numero que envia el SMS, aca va el proporcionado por twillo

    public void enviarNotificacion(Persona aQuien, Notificacion mensaje){

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try{
            Message message = Message.creator(
                new PhoneNumber("+"+ aQuien.getTelefono()), //numero al que van a llegar los mensajes, aca iria el de la persona que quiero notificar
                new PhoneNumber("+"+telefonoNuestro),
                conFormato(mensaje)
            ).create();

        } catch(ApiException e){
            throw new NumeroDesconocidoException(aQuien.getTelefono());
            /*TODO:
             * si se vencio la cuenta, tira la misma excepcion con el mensaje "authenticate"
             * si el numero existe pero no esta registrado tira este mensaje -> The number  is unverified. Trial accounts cannot send messages to unverified numbers; verify  at twilio.com/user/account/phone-numbers/verified, or purchase a Twilio number to send messages to unverified numbers.
             * si el numero no existe tira la misma excepcion con el mensaje -> The 'To' number +0 is not a valid phone number.
             */
        }
    }

    private String conFormato(Notificacion mensaje){
        return "["+mensaje.getAsunto()+"] "+ mensaje.getCuerpo();
    }
}
