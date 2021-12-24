package Persistencia;

import Modelo.Clientes.Cliente;
import Modelo.Clientes.Persona;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import Modelo.Publicaciones.PublicacionAdoptante;
import Modelo.Usuarios.Usuario;
import Repositorios.Template.Identificable;
import Repositorios.Template.RepoDB;
import Utils.Factory.ProveedorDeContactos;
import Utils.Factory.ProveedorDeMascotas;
import Utils.Factory.ProveedorDeOrganizaciones;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Factory.ProveedorDeClientes.getClienteFull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InsercionesTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

    @Test
    public void insertoUnCliente(){
        Cliente unCliente = getClienteFull();
        Cliente leido = insertarLeerValidandoIdYVaciarDB(unCliente);
        assertNotEquals(leido, unCliente);
        assertEquals(unCliente.getNumeroDocumento(), leido.getNumeroDocumento());
        assertEquals(unCliente.getFechaNacimiento(), leido.getFechaNacimiento());
    }

    @Test
    public void insertoUnContacto(){
        Persona pers = ProveedorDeContactos.contactoStub();
        insertarLeerValidandoIdYVaciarDB(pers);
    }

    @Test
    public void insertoUnaMascotaIdentificada(){
        MascotaIdentificada m = ProveedorDeMascotas.mascotaIdentificada();
        insertarLeerValidandoIdYVaciarDB(m);
    }

    @Test
    public void insertoUnaMascotaRescatada(){
        MascotaRescatada m = ProveedorDeMascotas.rescatadaStub();
        insertarLeerValidandoIdYVaciarDB(m);
    }

    @Test
    public void insertoUnaOrg(){
        Organizacion o = ProveedorDeOrganizaciones.orgConNombre("C.A.O.S.");
        insertarLeerValidandoIdYVaciarDB(o);
    }

    @Test
    public void insertoUnaMascotaEnAdopcion(){
        PublicacionAdopcion p = new PublicacionAdopcion(ProveedorDeMascotas.mascotaIdentificada(), caracs());

        insertarLeerValidandoIdYVaciarDB(p);
    }

    @Test
    public void insertoPublicacionDeAdoptante(){
        PublicacionAdoptante p = new PublicacionAdoptante(getClienteFull());
        p.setPreferencias(caracs());
        insertarLeerValidandoIdYVaciarDB(p);
    }

    @Test
    public void insertoUnaPregunta(){
        String pregunta = "¿De que color es el caballo blanco de san martin?";
        List<String> rtas = Arrays.asList("de perlas", "era una mula");

        Pregunta insertada = new Pregunta(pregunta, rtas, true);
        Pregunta leida = insertarLeerValidandoIdYVaciarDB(insertada);

        assertEquals(leida.permiteLibreRespuesta(), insertada.permiteLibreRespuesta());
        assertEquals(leida.getDetalle(), insertada.getDetalle());
    }

    @Test
    public void insertoUnUsuario(){
        Usuario u = new Usuario("replay", "wannaplay", getClienteFull());
        insertarLeerValidandoIdYVaciarDB(u);
    }

    //No es lo mas lindo del mundo, pero cumple
    private <T extends Identificable> T insertarLeerValidandoIdYVaciarDB(T entidad){
        RepoDB<T> repo = new RepoDB(entidad.getClass());
        withTransaction(()->repo.agregar(entidad));

        assertNotNull(entidad.getId());

        entityManager().close();

        T leido = repo.findById(entidad.getId()).get();

        withTransaction(()-> repo.eliminar(leido));

        return leido;
    }

    private Map<String, String> caracs(){
        Map<String, String> rtas = new HashMap<>();
        rtas.put("ojos", "grices");
        rtas.put("bigote", "afeitado");
        return rtas;
    }
}
