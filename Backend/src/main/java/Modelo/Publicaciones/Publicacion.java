package Modelo.Publicaciones;

import Repositorios.Template.Identificable;
import Repositorios.Template.Identificado;

public interface Publicacion extends Identificado {
    EstadoValidacion getEstado();
    void setEstado(EstadoValidacion estadoValidacion);
}
