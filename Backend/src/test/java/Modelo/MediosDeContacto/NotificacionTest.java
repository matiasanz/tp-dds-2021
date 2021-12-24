package Modelo.MediosDeContacto;

import Modelo.Clientes.Cliente;
import Modelo.Clientes.Persona;
import Modelo.Clientes.TipoDocumento;
import Utils.Mocks.NotificadorMock;
import org.junit.After;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotificacionTest {

    MedioNotificacion medio = MedioNotificacion.MOCK;
    NotificadorMock notificadorDePrueba = (NotificadorMock) medio.sender;
    List<MedioNotificacion> medios = Arrays.asList(medio);

    Persona contacto1 = new Persona("Carla","Lopez",1,null, medios);
    Persona contacto2 = new Persona("Nicolas","Perez",2,null, medios);

    Cliente cliente = new Cliente("Salvador","Mendez",541130599461l,"salvamendez@gmail.com"
        , medios
        , LocalDate.of(1312,2,14), TipoDocumento.PASAPORTE,42356784L
        , Arrays.asList(contacto1, contacto2)
    );

    @After
    public void clean(){
        notificadorDePrueba.eliminarBandejas();
    }

    @Test
    public void alNotificarUnClienteTodosSusContactosSeNotifican(){
        cliente.enviarNotificacion(new Notificacion("Alerta alerta", "Estas por romper la dieta"));

        assertEquals(1, notificadorDePrueba.getBandeja(cliente).size());
        assertEquals(1, notificadorDePrueba.getBandeja(contacto1).size());
        assertEquals(1, notificadorDePrueba.getBandeja(contacto2).size());
    }

}
