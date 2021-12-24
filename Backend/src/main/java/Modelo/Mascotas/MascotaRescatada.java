package Modelo.Mascotas;

import Modelo.Clientes.Cliente;
import Modelo.Hogares.Ubicacion;
import Presentacion.Utils.Model.TypeAdapters.FotosJsonAdapter;
import Repositorios.Template.Identificable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class MascotaRescatada extends Identificable {

    @Expose
    @ManyToOne(cascade = CascadeType.PERSIST)
    Cliente rescatista;
    @Embedded
    Ubicacion ubicacion;

    @SerializedName("descripcion")
    @Column(length = 255)
    String descripcionEstado;

    @JsonAdapter(FotosJsonAdapter.class)
    @ElementCollection
    @JoinTable(name="fotos_rescatadas")
    @JoinColumn(name="mascota")
    @Column(name="path")
    List<String> fotos = new LinkedList<>();

    public MascotaRescatada(){}
    public MascotaRescatada(Cliente rescatista, Ubicacion ubicacion, String descripcionEstado) {
        this.rescatista = rescatista;
        this.ubicacion = ubicacion;
        this.descripcionEstado = descripcionEstado;
    }

    public void setFotos(List<String> fotos){
        this.fotos = new LinkedList<>(fotos);
    }
    public void agregarFoto(String path){
        this.fotos.add(path);
    }

    public void setRescatista(Cliente rescatista) {
        this.rescatista=rescatista;
    }

    //Getters
    public Cliente getRescatista() {
        return rescatista;
    }
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    public String getDescripcionEstado() {
        return descripcionEstado;
    }
    public List<String> getFotos() {
        return fotos;
    }

}
