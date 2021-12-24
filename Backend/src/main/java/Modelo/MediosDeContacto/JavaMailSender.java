package Modelo.MediosDeContacto;
import Utils.Exceptions.MailNoEnviadoException;

import Modelo.Clientes.Persona;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

public class JavaMailSender implements Notificador {
    static final String nombre = "Rescate de Patitas";
    static final String direccionEmail = "rescatedepatitas.info@gmail.com";
    static final String password = "Rescatame!!";

    public void enviarNotificacion(Persona aQuien, Notificacion mensaje) {

        Session sesion = Session.getDefaultInstance(getProperties(), getAutenticador());

        try {
            Message msg = crearMensaje(mensaje, aQuien, sesion);
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailNoEnviadoException(mensaje, aQuien);
        }
    }

    private Message crearMensaje(Notificacion mensaje, Persona destinatario, Session autenticador) throws MessagingException, UnsupportedEncodingException {
        Message msg = new MimeMessage(autenticador);
                msg.setFrom(new InternetAddress(direccionEmail, nombre));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario.getMail(), destinatario.getNombre()));
                msg.setSubject(mensaje.getAsunto());
                msg.setText(mensaje.getCuerpo());

        return msg;
    }

    private Properties getProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    private Authenticator getAutenticador(){
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(direccionEmail, password);
            }
        };
    }
}

