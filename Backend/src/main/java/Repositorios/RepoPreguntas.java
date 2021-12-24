package Repositorios;

import Modelo.Organizaciones.Pregunta;
import Repositorios.Template.RepoDB;

import java.util.LinkedList;
import java.util.List;

public class RepoPreguntas extends RepoDB<Pregunta> {
    public RepoPreguntas(){
        super(Pregunta.class);
    }

    public List<Pregunta> getPreguntasComunes(){
        return entityManager().createNativeQuery(
            "select * from Preguntas p where p.org_carac is null and p.org_preg is null"
            , Pregunta.class
        ).getResultList();
    }
}
