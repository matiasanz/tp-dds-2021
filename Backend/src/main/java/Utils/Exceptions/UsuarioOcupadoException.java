package Utils.Exceptions;

import java.util.Arrays;
import java.util.List;

public class UsuarioOcupadoException extends RegistroFallidoException{
    public UsuarioOcupadoException(String nombre){
        super("El nombre de usuario "+nombre+" se encuentra ocupado");
    }

    @Override
    public List<String> getMotivos(){
        return Arrays.asList(getMessage());
    }
}
