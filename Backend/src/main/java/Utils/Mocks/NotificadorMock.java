package Utils.Mocks;

import Modelo.Clientes.Persona;
import Modelo.MediosDeContacto.Notificador;
import Modelo.MediosDeContacto.Notificacion;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NotificadorMock implements Notificador {
    private Map<Persona, List<Notificacion>> bandejas = new HashMap<>();

    public void enviarNotificacion(Persona destinatario, Notificacion mensaje){
        if(!bandejas.containsKey(destinatario)){
            bandejas.put(destinatario, new LinkedList<Notificacion>());
        }

        bandejas.get(destinatario).add(mensaje);
    }

    public void eliminarBandejas(){
        bandejas = new HashMap<>();
    }

    public List<Notificacion> getBandeja(Persona deQuien){
        return bandejas.get(deQuien);
    }
}
