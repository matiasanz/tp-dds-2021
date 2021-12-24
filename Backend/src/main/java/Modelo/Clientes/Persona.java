package Modelo.Clientes;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import Modelo.MediosDeContacto.MedioNotificacion;
import Modelo.MediosDeContacto.Notificacion;
import Repositorios.Template.Identificable;
import Utils.Exceptions.NotificacionFallidaException;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name="Personas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
@DiscriminatorValue("p")
public class Persona extends Identificable {
    String nombre;
    String apellido;
    Long telefono;
    String mail;

    @SerializedName("medios_contacto")
    @Enumerated(EnumType.ORDINAL)
    @ElementCollection
    List<MedioNotificacion> mediosNotificacion = new LinkedList<>();

    public Persona(){super();}

    public Persona(String nombre, String apellido, long telefono, String mail, List<MedioNotificacion> mediosNotificacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.mail = mail;
        this.mediosNotificacion.addAll(mediosNotificacion);
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Long getTelefono() {
        return telefono;
    }

    public String getMail() {
        return mail;
    }

    public List<MedioNotificacion> getMediosNotificacion() {
        return mediosNotificacion;
    }

    public void enviarNotificacion(Notificacion mensaje){
        List<MedioNotificacion> fallidos = mediosNotificacion.stream()
            .filter(n->!n.enviarNotificacion(this, mensaje))
            .collect(Collectors.toList());

        if(!fallidos.isEmpty()){
            throw new NotificacionFallidaException(this, fallidos, fallidos.size() == mediosNotificacion.size());
        }
    }

    public void setTelefono(Long telefono) {
        this.telefono=telefono;
    }

    public void agregarMedioDeContacto(MedioNotificacion medio){
        if(this.mediosNotificacion.contains(medio)){
            throw new RuntimeException("Se intento agregar un medio de notificacion repetido: "+medio+", al cliente '"+nombre+" "+apellido+"'");
        }

        this.mediosNotificacion.add(medio);
    }

    public void setMail(String mail){
        this.mail=mail;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
