package Modelo.Usuarios.Validaciones;

import java.util.function.IntPredicate;

public class ValidacionContieneCaracter extends ValidacionContrasenia {
    String tipoCaracter;
    private IntPredicate predicate;

    public ValidacionContieneCaracter(String tipoCaracter, IntPredicate predicate){
        this.tipoCaracter = tipoCaracter;
        this.predicate = predicate;
    }

    @Override
    public String getMensaje() {
        return "La contraseña debe contener al menos un caracter de tipo "+tipoCaracter;
    }

    @Override
    protected boolean contraseniaValida(String contrasenia) {
        return contrasenia.chars().anyMatch(predicate);
    }
}
