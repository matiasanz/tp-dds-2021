package Modelo.Publicaciones;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.EstadoPublicacion;
import Repositorios.Template.Identificable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class PublicacionAdoptante extends Identificable implements Publicacion {
    @OneToOne(cascade= CascadeType.ALL)
    private Cliente aspirante;

    @ElementCollection(targetClass=String.class)
    @CollectionTable(name="preferencias_publicacionAdoptante",joinColumns=@JoinColumn(name="publicacion_id"))
    @MapKeyColumn(name="pregunta")
    @Column(name="respuesta")
    private Map<String, String> preferencias = new HashMap<>();

    @Enumerated(EnumType.ORDINAL)
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;

    public void setAspirante(Cliente aspirante) {
        this.aspirante = aspirante;
    }

    public PublicacionAdoptante(){}
    public PublicacionAdoptante(Cliente cliente){
        this.aspirante = cliente;
    }

    public Cliente getAspirante() {
        return aspirante;
    }

    public String getPreferencia(String pregunta){
        return preferencias.getOrDefault(pregunta, "No opina");
    }

    public Map<String, String> getPreferencias(){
        return preferencias;
    }

    public void agregarPreferencia(String pregunta, String preferencia){
        preferencias.put(pregunta, preferencia);
    }

    public void setPreferencias(Map<String, String> preferencias) {
        this.preferencias = preferencias;
    }

    public EstadoValidacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoValidacion estado) {
        this.estado = estado;
    }
}
