package Modelo.Hogares;
import java.util.List;
import Modelo.Mascotas.Especie;
import Repositorios.RepoHogares.Services.AdmisionesParser;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class HogarDeTransito {
    private String id;
    private String nombre;
    private Ubicacion ubicacion;
    private String telefono;
    private Integer capacidad;
    private List<String> caracteristicas;

    @SerializedName("lugares_disponibles")
    private Integer lugaresDisponibles;

    @SerializedName("patio")
    private Boolean tienePatio;

    @SerializedName("admisiones")
    @JsonAdapter(AdmisionesParser.class)
    private List<Especie> especiesAdmitidas;

    public String getId() {
        return id;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getLugaresDisponibles() {
        return lugaresDisponibles;
    }

    public Boolean tienePatio() {
        return tienePatio;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public List<Especie> getEspeciesAdmitidas() {
        return especiesAdmitidas;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean admiteMascotasDeTamanio(TamanioMascota tamanio){
        return tamanio==TamanioMascota.CHICO || tienePatio();
    }

    public void setEspecies(List<Especie> especiesAdmitidas) {
        this.especiesAdmitidas = especiesAdmitidas;
    }
}
