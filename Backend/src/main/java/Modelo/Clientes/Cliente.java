package Modelo.Clientes;

import Modelo.Hogares.Ubicacion;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.MediosDeContacto.MedioNotificacion;

import java.time.LocalDate;
import java.util.List;
import Modelo.MediosDeContacto.Notificacion;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name="Clientes")
@DiscriminatorValue("c")
public class Cliente extends Persona {

    @SerializedName("fecha_nacimiento")
    @JsonFormat(pattern="dd/mm/yyyy")
    LocalDate fechaNacimiento;

    @SerializedName("tipo_documento")
    @Enumerated(EnumType.ORDINAL)
    TipoDocumento tipoDocumento;

    Long numeroDocumento;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="persona")
    @Column(name="contacto")
    List<Persona> contactos;
    @Embedded
    Ubicacion ubicacion;

    public Cliente(){super();}

    //TODO: Muchos args
    public Cliente(String nombre, String apellido, Long telefono, String mail, List<MedioNotificacion> formasDeNotificacion
        , LocalDate fechaNacimiento, TipoDocumento tipoDocumento, Long numeroDocumento, List<Persona> contactos) {

        super(nombre, apellido, telefono, mail, formasDeNotificacion);
        this.fechaNacimiento = fechaNacimiento;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.contactos = contactos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public List<Persona> getContactos() {
        return contactos;
    }

    public void enviarNotificacion(Notificacion mensaje){
        super.enviarNotificacion(mensaje);
        contactos.forEach(c->c.enviarNotificacion(mensaje));
    }

    public void agregarContacto(Persona nuevoContacto) {
        contactos.add(nuevoContacto);
    }

    public boolean esDuenioDe(MascotaIdentificada mascota) {
        return mascota.getDuenio().matchId(this);
    }
}
