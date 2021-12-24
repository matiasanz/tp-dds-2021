package Modelo.Publicaciones;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.MascotaIdentificada;
import Repositorios.Template.Identificable;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Map;
import static Utils.Factory.ProveedorDeNotificaciones.notificacionDeMascotaAdoptada;

@Entity
@Table(name="publicacionesAdopcion")
public class PublicacionAdopcion extends Identificable implements Publicacion{

    @OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
    @SerializedName("mascota")
    private MascotaIdentificada mascotaPublicada;

    @SerializedName("respuestas")
    @ElementCollection(targetClass=String.class)
    @JoinTable(name="RespuestasAdopcion",joinColumns=@JoinColumn(name="publicacion_id"))
    @MapKeyColumn(name="pregunta")
    @Column(name="respuesta")
    Map<String, String> respuestasAPreguntas;

    @Enumerated(EnumType.ORDINAL)
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;

    public PublicacionAdopcion(){}
    public PublicacionAdopcion(MascotaIdentificada mascota, Map<String, String> respuestasAPreguntas){
        this.mascotaPublicada =mascota;
        this.respuestasAPreguntas = respuestasAPreguntas;
    }

    public Map<String, String> getRespuestasAPreguntas() {
        return respuestasAPreguntas;
    }

    public String getRespuestaAPregunta(String pregunta) {
        return respuestasAPreguntas.getOrDefault(pregunta, "Sin responder");
    }

    public MascotaIdentificada getMascotaPublicada(){
        return mascotaPublicada;
    }

    public void setMascota(MascotaIdentificada mascota) {
        this.mascotaPublicada = mascota;
    }

    public EstadoValidacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoValidacion estado) {
        this.estado = estado;
    }
}
