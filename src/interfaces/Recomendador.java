package interfaces;

import java.util.ArrayList;

import excepciones.recomendacion.RecomendacionException;
import modelo.contenido.Contenido;
import modelo.usuarios.Usuario;

public interface Recomendador {
	ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException;
	ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException;
}
