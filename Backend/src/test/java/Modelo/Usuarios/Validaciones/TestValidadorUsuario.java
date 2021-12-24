package Modelo.Usuarios.Validaciones;
import Modelo.Usuarios.Usuario;
import Modelo.Usuarios.ValidadorUsuario;
import Utils.Mocks.RepoUsuariosMock;
import org.junit.Test;
import static org.junit.Assert.*;

import Utils.Exceptions.ContraseniaInseguraException;

public class TestValidadorUsuario {

    RepoUsuariosMock repoUsuarios = new RepoUsuariosMock();

    //Contraseña contiene minuscula
        ValidacionContrasenia validadorMinuscula = new ValidacionMinuscula();

        @Test
        public void unaContraseñaContieneMinuscula(){
            assertTrue(validadorMinuscula.contraseniaValida("hola"));
        }

        @Test
        public void unaContraseñaNoContieneMinuscula(){
            assertFalse(validadorMinuscula.contraseniaValida("HOLA"));
        }


    //Contraseña contiene mayuscula
        ValidacionContrasenia validadorMayuscula = new ValidacionMayuscula();

        @Test
        public void unaContraseñaContieneMayuscula(){
            assertTrue(validadorMayuscula.contraseniaValida("Hola"));
        }

        @Test
        public void unaContraseñaNoContieneMayuscula(){
            assertFalse(validadorMayuscula.contraseniaValida("hola"));
        }

    //Contraseña contiene numero
        ValidacionContrasenia validacionNumero = new ValidacionNumero();

        @Test
        public void unaContraseñaContieneNumero(){
            assertTrue(validacionNumero.contraseniaValida("Hola123"));
        }

        @Test
        public void unaContraseñaNoContieneNumero(){
            assertFalse(validacionNumero.contraseniaValida("hola"));
        }

    //Longitud minima *****************************************
        ValidacionContrasenia validacionLongitud = new ValidacionLongitudMinima(8);

        @Test
        public void unaContraseniaCon7CaracteresEsDemasiadoCorta(){
            assertFalse(validacionLongitud.contraseniaValida("1234567"));
        }

        @Test
        public void unaCon8CaracteresAlcanza() {
            assertTrue(validacionLongitud.contraseniaValida("12345678"));
        }

        @Test
        public void masDe8CaracteresEstaOk() {
            assertTrue(validacionLongitud.contraseniaValida("123456789"));
        }

	//Modelo.Usuario contenido en contrasenia ************************
        ValidacionContrasenia validacionNombreEnContrasenia = new ValidacionUsuarioNoContenidoEnContrasenia();

        @Test
        public void contraseniaIgualAlUsuarioEsDetectada() {
            Usuario u = new Usuario("admin", "admin", null);
            assertFalse(validacionNombreEnContrasenia.apruebaUsuario(u));
        }

        @Test
        public void usuarioAlPrincipioEsDetectado() {
            Usuario u = new Usuario("lucho", "LuchoKpoOnTheMoonForever", null);
            assertFalse(validacionNombreEnContrasenia.apruebaUsuario(u));
        }

        @Test
        public void nombreContenidoAlFinal() {
            Usuario u = new Usuario("carlitos", "elmascrackesCarlitos", null);
            assertFalse(validacionNombreEnContrasenia.apruebaUsuario(u));
        }

        @Test
        public void nombreContenidoEnElMedio() {
            Usuario u = new Usuario("lider", "nanananananananlidernanannanaa", null);
            assertFalse(validacionNombreEnContrasenia.apruebaUsuario(u));
        }

        @Test
        public void nombreNoTextualNoSalta() {
            Usuario u = new Usuario("lucho", "oluch", null);
            assertTrue(validacionNombreEnContrasenia.apruebaUsuario(u));
        }
    
    //contrasenias triviales *********************************
        ValidacionContrasenia validacionTrivial = new ValidacionNoTrivial();

        @Test
        public void contraseniaEntreLas10000peores() {
            assertFalse(validacionTrivial.contraseniaValida("qwerty"));
        }

        @Test
        public void otraContraseniaEntreLas10000peores() {
            assertFalse(validacionTrivial.contraseniaValida("12345678"));
        }
    
    //caracteres repetidos************************************
    ValidacionContrasenia validacionCaracsRepetidos = new ValidacionCaracteresNoRepetidos();

    @Test
    public void validarCaracteresRepetidosConDosCaracteres() {     
    	assertFalse(validacionCaracsRepetidos.contraseniaValida("pepeholapp"));
    }

    //varios problemas se detectan correctamente

    ValidadorUsuario validador = new ValidadorUsuario(new RepoUsuariosMock());

    @Test (expected = ContraseniaInseguraException.class)
    public void unaContraseniaInseguraEsRechazada() {
    	validador.validarContrasenia(new Usuario("123","123", null));
    }
    
    @Test
    public void losMotivosSeInformanCorrectamente() {
    	try {    		
    		validador.validarContrasenia(new Usuario("123","123", null));
    		assertEquals("Excepcion", "No hubo excepcion"); //Fuerzo a que rompa en caso de no excepcion
    		
    	} catch(ContraseniaInseguraException e){
    		assertEquals(5, e.getMotivos().size());
    	}
    }

}