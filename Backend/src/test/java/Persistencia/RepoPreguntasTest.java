package Persistencia;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import Repositorios.RepoOrganizaciones;
import Repositorios.RepoPreguntas;
import Repositorios.Template.Transaccional;
import Utils.Factory.ProveedorDeOrganizaciones;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RepoPreguntasTest implements Transaccional {
    RepoPreguntas repoPregs = new RepoPreguntas();
    RepoOrganizaciones repoOrgs = new RepoOrganizaciones();
    Organizacion org = ProveedorDeOrganizaciones.orgConNombre("Terrabusi");
    Pregunta preguntaComunAbierta = new Pregunta("Si un arbol se cae y no hay nadie escuchando hace ruido?");
    Pregunta preguntaComunCerrada = new Pregunta("Si la vida te derrota, que hay que hacer?", Arrays.asList("nadaremos"), false);
    Pregunta preguntaNoComun = new Pregunta("Cuantos ojos tiene?");

    @Before
    public void init(){
        withTransaction(()->{
            repoPregs.vaciar();
            repoPregs.agregar(preguntaComunAbierta);
            repoPregs.agregar(preguntaComunCerrada);
            org.agregarPregunta(preguntaNoComun);
            repoOrgs.agregar(org);
        });
    }

    @After
    public void clean(){
        withTransaction(()->{
            repoOrgs.eliminar(org);
            repoPregs.eliminar(preguntaNoComun);
            repoPregs.eliminar(preguntaComunCerrada);
            repoPregs.eliminar(preguntaComunAbierta);
        });
    }

    @Test
    public void todasLasPreguntasEstanEnElRepo(){
        assertEquals(3, repoPregs.getAll().size());
    }

    @Test
    public void preguntasComunesSeObtienenCorrectamente(){
        List<Pregunta> preguntasComunes = repoPregs.getPreguntasComunes();
        assertEquals(2, preguntasComunes.size());
        assertThat(
            preguntasComunes.stream().map(Pregunta::getId).collect(Collectors.toList())
            , is(Arrays.asList(preguntaComunAbierta.getId(), preguntaComunCerrada.getId()))
        );
    }

    @Test
    public void preguntasComunesNoSeMezclanConOrganizacion(){
        List<Pregunta> pregsDeOrg = org.getPreguntasADuenios();
        assertEquals(1, pregsDeOrg.size());
        assertEquals(preguntaNoComun.getId(), pregsDeOrg.get(0).getId());

    }
}