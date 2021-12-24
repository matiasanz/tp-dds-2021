package Modelo.Usuarios.Validaciones;

public class ValidacionLongitudMinima extends ValidacionContrasenia{

    private int longitudMinima;

    public ValidacionLongitudMinima(int longMinima){
        this.longitudMinima=longMinima;
    }

    @Override
    public String getMensaje() {
        return "La contrasenia debe tener al menos "+ longitudMinima +" caracteres";
    }

    @Override
    public boolean contraseniaValida(String contrasenia) {
        return contrasenia.length() >= longitudMinima;
    }
}
