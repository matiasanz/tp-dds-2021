package Presentacion.Backend.Controladores;

import Modelo.Clientes.Cliente;
import Modelo.Clientes.Persona;
import Modelo.Usuarios.Usuario;
import Modelo.Usuarios.ValidadorUsuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.LoginService;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Utils.Exceptions.*;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Request;
import spark.Response;
import spark.Service;

import java.net.HttpURLConnection;

import static Presentacion.Utils.RequestUtils.getBody;

public class LoginController implements Controller {
    private LoginService loginService = new LoginService();
    public void routes(Service service) {

        service.get("/api/usuario", this::getSesionActual);
        service.post("/api/login", this::iniciarSesion);
        service.post("/api/mis-datos", this::ingresarDatosPersonales);
        service.post("/api/signup", this::crearUsuario);
        service.get("/api/logout", this::logout);
    }

    private String getSesionActual(Request request, Response response) {
        return loginService.datosUsuario(extraerCliente(request)).toJson();
    }

    private String logout(Request request, Response response) {
        loginService.logout(request);
        return "";
    }

    private ValidadorUsuario validadorUsuario = new ValidadorUsuario(repoUsuarios);

    private String crearUsuario(Request request, Response response) {
        try {
            Cliente duenio = extraerCliente(request);

            Usuario usuario = getBody(request).classValue(Usuario.class);

            withTransaction(() -> {
                usuario.setDatosPersonales(duenio);

                validadorUsuario.validarUsuario(usuario);

                repoUsuarios.agregar(usuario);
            });

            response.status(HttpURLConnection.HTTP_CREATED);

            return loginService.iniciarSesion(usuario.getNombre(), usuario.getContrasenia(), request).toJson();

        } catch (ContraseniaInseguraException e) {
            throw ErrorHandler.haltException(402, e.getMessage());

        } catch (UsuarioOcupadoException e) {
            throw ErrorHandler.haltWithMessage(403, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String iniciarSesion(Request request, Response response) {
        Model body = getBody(request);
        String nombreUsuario = body.stringValue("username");
        String contrasenia = body.stringValue("password");

        return loginService.iniciarSesion(nombreUsuario, contrasenia, request).toJson();
    }

    private String ingresarDatosPersonales(Request request, Response response) {
        Cliente cliente;

        try {
            cliente = getBody(request).classValue("cliente", Cliente.class);;
            validarDatosPersonales(cliente);
        } catch (ConversionFallidaException | DatosFaltantesException e) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }

        withTransaction(() -> {
            repoClientes.agregar(cliente);
        });

        clientes.guardarCredenciales(cliente, request);
        response.status(HttpURLConnection.HTTP_CREATED);
        return "{}";
    }

    private void validarDatosPersonales(Cliente cliente) {
        new ValidadorNoNull("cliente", cliente)
                .conDato("nombre", cliente.getNombre())
                .conDato("apellido", cliente.getApellido())
                .conDato("mail", cliente.getMail())
                .conDato("telefono", cliente.getTelefono())
                .conDato("fecha_nacimiento", cliente.getFechaNacimiento())
                .conDato("contactos", cliente.getContactos())
                .validar();

        int[] orden = {0};
        cliente.getContactos().forEach(c->validarDatosContacto(c, "contacto"+(++orden[0])));
    }

    private void validarDatosContacto(Persona persona, String label) {
        new ValidadorNoNull(label, persona)
                .conDato("nombre", persona.getNombre())
                .conDato("apellido", persona.getApellido())
                .conDato("telefono", persona.getTelefono())
                .conDato("mail", persona.getMail())
                .conDato("medios_contacto",persona.getMediosNotificacion())
        .validar();

        int cantidadMinimaMedios = 1;

        if(persona.getMediosNotificacion().size() < 1){
            throw ErrorHandler.haltWithMessage(400, "Debe ingresar al menos " +cantidadMinimaMedios+" medio de contacto para "+label);
        }

    }


}
