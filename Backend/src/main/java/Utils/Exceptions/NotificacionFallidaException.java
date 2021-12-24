package Utils.Exceptions;

import Modelo.Clientes.Persona;
import Modelo.MediosDeContacto.MedioNotificacion;

import java.util.List;

public class NotificacionFallidaException extends RuntimeException {
    private Boolean mensajeEnviado;

    public NotificacionFallidaException(Persona persona, List<MedioNotificacion> fallidos, Boolean mensajeFueEnviado) {
        super("No se pudo notificar a "+persona.getNombre() + " "+persona.getApellido()+" a traves de los medios "+fallidos.toString()
        + (mensajeFueEnviado? ". Pero sin embargo el mensaje fue enviado" : ""));
        this.mensajeEnviado = mensajeFueEnviado;
    }

    public boolean mensajeFueEnviado(){
        return this.mensajeEnviado;
    }
}
