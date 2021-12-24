package Utils.Factory;
import Modelo.Imagenes.CalidadImagen;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import java.util.List;

public class ProveedorDeOrganizaciones {
    public static Organizacion orgConNombre(String nombre){
        return new Organizacion(nombre, 1024, 720, CalidadImagen.BASICA);
    }

public static Organizacion orgConNombreYCaracteristicas(String nombre, List<Pregunta> caracteristicas){
        Organizacion org = new Organizacion(nombre, 1024, 720, CalidadImagen.BASICA);
        org.setCaracteristicas(caracteristicas);
        return org;
    }
}
