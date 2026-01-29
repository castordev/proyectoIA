package modelo.contenido;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;

public abstract class Contenido {
	protected final String id;
	protected String titulo;
	protected int reproducciones;
	protected int likes;
	protected int duracionSegundos;
	protected ArrayList<String> tags;
	protected boolean disponible;
	protected Date fechaPublicacion;

	protected Contenido(String titulo, int duracionSegundos) throws DuracionInvalidaException {
		this.id = UUID.randomUUID().toString();
		this.titulo = (titulo == null || titulo.isBlank()) ? "(sin titulo)" : titulo;
		this.duracionSegundos = duracionSegundos;
		this.tags = new ArrayList<>();
		this.disponible = true;
		this.fechaPublicacion = new Date();
		validarDuracion();
	}

	public String getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getReproducciones() {
		return reproducciones;
	}

	public int getLikes() {
		return likes;
	}

	public int getDuracionSegundos() {
		return duracionSegundos;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public abstract void reproducir() throws Exception;

	public void aumentarReproducciones() {
		reproducciones++;
	}

	public void agregarLike() {
		likes++;
	}

	public boolean esPopular() {
		return reproducciones > 100_000;
	}

	public void validarDuracion() throws DuracionInvalidaException {
		if (duracionSegundos <= 0) {
			throw new DuracionInvalidaException("Duracion invalida: " + duracionSegundos);
		}
	}

	protected void validarDisponible() throws ContenidoNoDisponibleException {
		if (!disponible) {
			throw new ContenidoNoDisponibleException("Contenido no disponible: " + titulo);
		}
	}
}
