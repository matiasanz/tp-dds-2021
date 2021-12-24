package Utils.Factory;

import Modelo.Clientes.Persona;

import java.util.ArrayList;

public class ProveedorDeContactos {
    public static Persona contactoConTelefono(Long telefono){
        Persona persona = contactoStub();
        persona.setTelefono(telefono);
        return persona;
    }

    public static Persona contactoConMail(String mail){
        Persona persona = contactoStub();
        persona.setMail(mail);
        return persona;
    }

    public static Persona contactoStub(){
        return new Persona("Diego", "Cembal", 0, "_@mail.com", new ArrayList<>());
    }
}
