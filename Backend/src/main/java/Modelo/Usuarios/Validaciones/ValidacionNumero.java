package Modelo.Usuarios.Validaciones;

public class ValidacionNumero extends ValidacionContieneCaracter{
    public ValidacionNumero(){
        super("numerico", Character::isDigit);
    }
}
