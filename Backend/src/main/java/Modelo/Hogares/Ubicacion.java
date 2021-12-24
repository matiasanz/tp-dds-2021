package Modelo.Hogares;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.awt.geom.Point2D;

@Embeddable
public class Ubicacion {
    String direccion;
    @SerializedName("lat")
    Double latitud;
    @SerializedName("long")
    Double longitud;

    public Ubicacion(){}
    public Ubicacion(String direccion, Double latitud, Double longitud){
        this.direccion = direccion;
        this.latitud=latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getDireccion(){
        return direccion;
    }

    public Double distanciaA(Ubicacion ubicacion){
        return Point2D.distance(latitud, longitud, ubicacion.getLatitud(), ubicacion.getLongitud());
    }
}
