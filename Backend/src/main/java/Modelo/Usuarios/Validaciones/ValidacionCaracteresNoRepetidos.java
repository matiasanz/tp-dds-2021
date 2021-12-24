package Modelo.Usuarios.Validaciones;

import java.util.regex.Pattern;

public class ValidacionCaracteresNoRepetidos extends ValidacionContrasenia{

    @Override
    public String getMensaje() {
        return "La contrasenia no debe contener caracteres repetidos";
    }

    @Override
    public boolean contraseniaValida(String contrasenia) {
        return evaluarRegex("^(?!.*(.)\\1)", contrasenia);
    }

    private boolean evaluarRegex(String regEx, String contrasenia) {
        return Pattern.compile(regEx).matcher(contrasenia).find();
    }
}
