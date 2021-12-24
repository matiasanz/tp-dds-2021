package Utils.Exceptions;

import Presentacion.Utils.Model.Model;

import java.util.List;
import java.util.stream.Collectors;

public class ContraseniaInseguraException extends RegistroFallidoException {
	List<String> motivos;
	public ContraseniaInseguraException(List<String> erroresDetectados) {
		super(new Model("errores", erroresDetectados).toJson());
		this.motivos = erroresDetectados;
	}

	@Override
	public List<String> getMotivos(){
		return motivos;
	}
}
