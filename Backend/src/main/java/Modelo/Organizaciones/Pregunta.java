package Modelo.Organizaciones;

import Repositorios.Template.Identificable;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="Preguntas")
public class Pregunta extends Identificable {
    private String detalle;

    @SerializedName("rtas_predefinidas")
    @ElementCollection
    @JoinTable(name = "RespuestasPredefinidas", joinColumns = @JoinColumn(name="pregunta"))
    @Column(name = "detalle")
    private List<String> respuestasPredefinidas = new ArrayList<>();
    @SerializedName("libre_rta")
    private Boolean opcionRespuestaLibre;

    public Pregunta(){}
    public Pregunta(String detalle, List<String> respuestasPredefinidas, boolean opcionLibre){
        this.detalle=detalle;
        this.respuestasPredefinidas.addAll(respuestasPredefinidas);
        this.opcionRespuestaLibre=opcionLibre;
    }
    public Pregunta(String detalle) {
        this.detalle = detalle;
        this.opcionRespuestaLibre=true;
    }

    public String getDetalle() {
        return detalle;
    }

    public List<String> getRespuestasPredefinidas(){
        return respuestasPredefinidas;
    }

    public Boolean permiteLibreRespuesta(){
        return opcionRespuestaLibre;
    }

    private void setRespuestasPredefinidas(List<String> rtas){
        this.respuestasPredefinidas = rtas;
    }

    public boolean admiteRespuesta(String respuesta) {
        return permiteLibreRespuesta() || respuestasPredefinidas.contains(respuesta);
    }
}
