package Modelo.Recomendaciones;

import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Publicaciones.PublicacionAdoptante;
import Utils.Factory.ProveedorDeMascotas;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PublicacionesTest {
    PublicacionAdoptante publicacion;
    String preguntaRespondida = "Color de ojos";
    String respuesta = "marrones";

    Recomendador controller = new Recomendador(null);

    Map<String, String> caracs;

    @Before
    public void init(){
        publicacion= new PublicacionAdoptante(null);
        publicacion.agregarPreferencia(preguntaRespondida, respuesta);
        publicacion.agregarPreferencia("especie", "perro");

        caracs = new HashMap<>();
        caracs.put(preguntaRespondida, respuesta);
    }

    @Test
    public void lasPreferenciasDeUnaPublicacionSeGuardanCorrectamente(){
        assertEquals(2, publicacion.getPreferencias().size());
        assertEquals(respuesta, publicacion.getPreferencia(preguntaRespondida));
        assertEquals("perro", publicacion.getPreferencia("especie"));
    }

    @Test
    public void lasPreferenciasNoAclaradasTienenRespuestaDefault(){
        assertEquals("No opina", publicacion.getPreferencia("color de pelo"));
    }

    @Test
    public void matchearMascotaConMitadBienYMitadMal(){
        caracs.put("especie", "gato");
        assertEquals(0.5f, porcentajeDeCoincidencia(mascotaConCaracs(caracs)), 0.0);
    }

    @Test
    public void matchearMascotaConTodoMal(){
        caracs.put(preguntaRespondida, "equisde");
        float porcentaje = porcentajeDeCoincidencia(mascotaConCaracs(caracs));
        assertEquals(0f, porcentaje, 0.0);
    }

    @Test
    public void matchearMascotaConTodoBien(){
        caracs.put("especie", "perro");
        assertEquals(1f, porcentajeDeCoincidencia(mascotaConCaracs(caracs)), 0.0);
    }

    private float porcentajeDeCoincidencia(PublicacionAdopcion mascota){
        return controller.porcentajeDeCoincidencia(publicacion, mascota);
    }

    private PublicacionAdopcion mascotaConCaracs(Map<String, String> caracs){
        return new PublicacionAdopcion(ProveedorDeMascotas.mascotaIdentificada(), caracs);
    }
}