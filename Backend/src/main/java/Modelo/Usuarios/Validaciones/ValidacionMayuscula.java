package Modelo.Usuarios.Validaciones;

public class ValidacionMayuscula extends ValidacionContieneCaracter{
    public ValidacionMayuscula(){
        super("mayuscula", Character::isUpperCase);
    }
}
