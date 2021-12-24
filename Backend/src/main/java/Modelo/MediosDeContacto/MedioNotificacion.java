package Modelo.MediosDeContacto;

import Modelo.Clientes.Persona;
import Utils.Exceptions.MailNoEnviadoException;
import Utils.Exceptions.NumeroDesconocidoException;
import Utils.Mocks.NotificadorMock;

public enum MedioNotificacion {
    MAIL(new JavaMailSender())
    , SMS(new TwillioSMSSender())
    , WPP(new TwillioWPPSender())
    , MOCK(new NotificadorMock())
    ;

    protected Notificador sender;

    MedioNotificacion(Notificador sender){
        this.sender = sender;
    }

    public boolean enviarNotificacion(Persona persona, Notificacion notificacion){
        try {
            sender.enviarNotificacion(persona, notificacion);
            return true;
        } catch (MailNoEnviadoException | NumeroDesconocidoException e){
            return false;
        }
    }
}
