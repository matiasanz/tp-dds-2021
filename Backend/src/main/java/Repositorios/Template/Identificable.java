package Repositorios.Template;

import Modelo.Organizaciones.Organizacion;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Identificable implements Identificado{

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
}
