package Utils.Factory;
import Modelo.Clientes.*;
import Modelo.MediosDeContacto.MedioNotificacion;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ProveedorDeClientes {
    public static Cliente getClienteFull(){
        Cliente cliente =  new Cliente("Juan", "ferro", 123l
            , "juanferro1400@gmail.com", Collections.singletonList(MedioNotificacion.MAIL), LocalDate.of(25, 2, 1), TipoDocumento.DNI, 0l, new LinkedList<>());

        Cliente contacto = clienteConNombre("Esther");
        contacto.setApellido("Piscore de García");
        cliente.agregarContacto(contacto);

        return cliente;
    }

    public static Cliente clienteConNombre(String nombre) {
        return new Cliente(nombre, "Lampone", 549113059461L
            , "matias.l.anzorandia@gmail.com", Collections.emptyList(), LocalDate.now(), TipoDocumento.DNI, 0L, new LinkedList<>());
    }
}
