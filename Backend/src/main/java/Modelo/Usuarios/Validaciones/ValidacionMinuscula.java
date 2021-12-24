package Modelo.Usuarios.Validaciones;

public class ValidacionMinuscula extends ValidacionContieneCaracter{
    public ValidacionMinuscula(){
        super("minuscula", Character::isLowerCase);
    }
}
