package Modelo.MediosDeContacto;

import Modelo.Clientes.Persona;

public interface Notificador {
    void enviarNotificacion(Persona aQuien, Notificacion mensaje);
}
