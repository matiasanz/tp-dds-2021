package Presentacion.Backend.Controladores.Template;

import Modelo.Clientes.Cliente;
import Modelo.Organizaciones.Organizacion;
import Modelo.Usuarios.Usuario;
import Presentacion.Services.Template.Servicio;
import Presentacion.Services.Template.SessionManager;
import Presentacion.Utils.Imagenes.ImagenDTO;
import Presentacion.Utils.Imagenes.ImagenHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Repositorios.*;
import Repositorios.Template.Transaccional;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.IdDesconocidoException;
import spark.Request;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.List;

import static Presentacion.Utils.RequestUtils.*;
import static spark.Spark.get;
import static Presentacion.Utils.ErrorHandler.haltException;

public interface Controller extends Servicio {
    void routes(Service service);
}

