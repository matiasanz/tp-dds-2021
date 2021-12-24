package Modelo.Mascotas;

import Modelo.Clientes.Cliente;
import Presentacion.Utils.Model.TypeAdapters.FotosJsonAdapter;
import Repositorios.Template.Identificable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="MascotasIdentificadas")
public class MascotaIdentificada extends Identificable {
    @Column(length = 30)
    private String nombre;

    @Column(length = 30)
    String apodo;

    @SerializedName("edad_aproximada")
    Integer edadAproximada;

    @Enumerated(EnumType.ORDINAL)
    SexoMascota sexo;

    @Enumerated(EnumType.ORDINAL)
    Especie especie;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Expose
    private Cliente duenio;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.ORDINAL)
    SituacionMascota situacion = SituacionMascota.EN_CASA;

    @JsonAdapter(FotosJsonAdapter.class)
    @ElementCollection
    @JoinTable(name="fotos_identificadas")
    @JoinColumn(name="mascota")
    @Column(name="path")
    private List<String> fotos = new LinkedList<>();

    @ElementCollection(targetClass=String.class)
    @JoinTable(name="CaracteristicasMascotas",joinColumns=@JoinColumn(name="mascota"))
    @MapKeyColumn(name="clave")
    @Column(name="detalle")
    Map<String,String> caracteristicas = new HashMap<>();

    public MascotaIdentificada(){}
    public MascotaIdentificada(String nombre, String apodo, int edadAproximada,
                               SexoMascota sexo, String descripcion, Especie especie, Cliente duenio) {
        this.nombre = nombre;
        this.duenio = duenio;
        this.descripcion = descripcion;
        this.apodo = apodo;
        this.edadAproximada = edadAproximada;
        this.sexo = sexo;
        this.especie = especie;
    }


    public void agregarCaracteristica(String caracteristica, String valor){
        this.caracteristicas.put(caracteristica,valor);
    }

    //Getters
    public String getApodo() {
        return apodo;
    }
    public int getEdadAproximada() {
        return edadAproximada;
    }
    public SexoMascota getSexo() {
        return sexo;
    }
    public Especie getEspecie() {
        return especie;
    }
    public Cliente getDuenio() {
        return duenio;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public Map<String, String> getCaracteristicas() {
        return caracteristicas;
    }

    //Setters
    public void setDuenio(Cliente duenio) {
        this.duenio = duenio;
    }
    public SituacionMascota getSituacion() {
        return situacion;
    }

    public void setSituacion(SituacionMascota situacion){
        this.situacion = situacion;
    }

    public void agregarFoto(String foto) {
        this.fotos.add(foto);
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }
}