package Utils.Exceptions;

public class ErrorDeAutenticacionException extends RuntimeException{
	public ErrorDeAutenticacionException(){
		super("La contrasenia es incorrecta");
	}
}