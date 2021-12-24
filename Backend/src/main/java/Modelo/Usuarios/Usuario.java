package Modelo.Usuarios;

import Modelo.Clientes.Cliente;
import Modelo.Organizaciones.Organizacion;
import Repositorios.Template.Identificable;
import Utils.Exceptions.ContraseniaIncorrectaException;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Usuarios")
public class Usuario extends Identificable {

    @SerializedName("username")
    private String nombre;

    @SerializedName("password")
    private String contrasenia;

    @OneToOne(cascade= CascadeType.ALL)
    private Cliente datosPersonales;

    //(?)
    @ManyToMany(mappedBy = "administradores")
    List<Organizacion> organizacionesAdministradas = new ArrayList<>();
    @ManyToMany(mappedBy = "voluntarios")
    List<Organizacion> organizacionesVoluntario = new ArrayList<>();

    public Usuario(){}
    public Usuario(String nombre, String contrasenia, Cliente datosPersonales) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.datosPersonales = datosPersonales;
    }

    public void agregarOrganizacionAdministrada(Organizacion org){
        organizacionesAdministradas.add(org);
    }

    public void agregarOrganizacionVoluntaria(Organizacion org){
        organizacionesVoluntario.add(org);
    }

    public Cliente getDatosPersonales(){
        return datosPersonales;
    }

	public String getContrasenia() {
		return contrasenia;
	}

	public String getNombre() {
		return nombre;
	}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void autenticar(String contrasenia) {
        if(!this.contrasenia.equals(contrasenia)){
            throw new ContraseniaIncorrectaException();
        }
    }

    public void setDatosPersonales(Cliente datosPersonales) {
        this.datosPersonales=datosPersonales;
    }
}