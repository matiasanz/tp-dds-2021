package Modelo.Usuarios;

import Repositorios.RepoUsuarios.RepoUsuarios;
import Utils.Exceptions.ContraseniaInseguraException;
import Utils.Exceptions.UsuarioOcupadoException;
import Modelo.Usuarios.Validaciones.*;

import java.util.*;
import java.lang.*;
import java.util.stream.Collectors;

public class ValidadorUsuario {

    private int LONGITUD_MINIMA_SEGUN_NIST = 8;

    private RepoUsuarios repoUsuarios;
    private List<ValidacionContrasenia> validacionesDeContrasenia = new ArrayList<>();

    public ValidadorUsuario(RepoUsuarios repoUsuarios){
        this.repoUsuarios=repoUsuarios;

        //TODO Ver si esta bien agregarlos aca
        validacionesDeContrasenia.addAll(Arrays.asList(
            new ValidacionLongitudMinima(LONGITUD_MINIMA_SEGUN_NIST)
            , new ValidacionNoTrivial()
            , new ValidacionMinuscula()
            , new ValidacionMayuscula()
            , new ValidacionNumero()
            , new ValidacionCaracteresNoRepetidos()
            , new ValidacionUsuarioNoContenidoEnContrasenia()
        ));
    }

    public void validarUsuario(Usuario usuario){
        validarNombre(usuario);
        validarContrasenia(usuario);
    }

    public void validarNombre(Usuario usuario){
        String nombre = usuario.getNombre();
        if(repoUsuarios.nombreOcupado(nombre))
            throw new UsuarioOcupadoException(nombre);
    }

    public void validarContrasenia(Usuario usuario) {
        List<String> erroresDetectados = validacionesDeContrasenia.stream()
            .filter(validacion -> !validacion.apruebaUsuario(usuario))
            .map(ValidacionContrasenia::getMensaje)
            .collect(Collectors.toList());

        if(!erroresDetectados.isEmpty()){
            throw new ContraseniaInseguraException(erroresDetectados);
        }
    }
}