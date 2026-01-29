package modelo.contenido;

import java.util.ArrayList;

import enums.CategoriaPodcast;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.contenido.EpisodioNoEncontradoException;
import excepciones.contenido.TranscripcionNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import interfaces.Descargable;
import interfaces.Reproducible;
import modelo.artistas.Creador;

public class Podcast extends Contenido implements Reproducible, Descargable {
	private final Creador creador;
	private final int numeroEpisodio;
	private final int temporada;
	private final String descripcion;
	private final CategoriaPodcast categoria;
	private final ArrayList<String> invitados;
	private final String transcripcion;
	private boolean descargado;

	public Podcast(String titulo, int duracionSegundos, Creador creador, int temporada, int numeroEpisodio, String descripcion,
			CategoriaPodcast categoria, String transcripcion) throws DuracionInvalidaException {
		super(titulo, duracionSegundos);
		this.creador = creador;
		this.temporada = temporada;
		this.numeroEpisodio = numeroEpisodio;
		this.descripcion = (descripcion == null) ? "" : descripcion;
		this.categoria = categoria;
		this.invitados = new ArrayList<>();
		this.transcripcion = transcripcion;
		this.descargado = false;
	}

	public Creador getCreador() {
		return creador;
	}

	public int getNumeroEpisodio() {
		return numeroEpisodio;
	}

	public int getTemporada() {
		return temporada;
	}

	public CategoriaPodcast getCategoria() {
		return categoria;
	}

	public String obtenerDescripcion() {
		return descripcion;
	}

	public void agregarInvitado(String nombre) {
		if (nombre != null && !nombre.isBlank()) {
			invitados.add(nombre.trim());
		}
	}

	public boolean esTemporadaNueva() {
		return temporada >= 5;
	}

	public String obtenerTranscripcion() throws TranscripcionNoDisponibleException {
		if (transcripcion == null || transcripcion.isBlank()) {
			throw new TranscripcionNoDisponibleException("No hay transcripcion para: " + titulo);
		}
		return transcripcion;
	}

	public void validarEpisodio() throws EpisodioNoEncontradoException {
		if (numeroEpisodio <= 0 || temporada <= 0) {
			throw new EpisodioNoEncontradoException("Episodio/temporada invalido: T" + temporada + "E" + numeroEpisodio);
		}
	}

	@Override
	public void reproducir() throws Exception {
		play();
	}

	@Override
	public void play() {
		try {
			validarDisponible();
			validarEpisodio();
			aumentarReproducciones();
			System.out.println("▶ Reproduciendo podcast: \"" + titulo + "\" (" + categoria + ") - "
					+ (creador != null ? creador.getNombreCanal() : "(sin creador)") + " T" + temporada + "E" + numeroEpisodio);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void pause() {
		System.out.println("⏸ Pausa podcast: \"" + titulo + "\"");
	}

	@Override
	public void stop() {
		System.out.println("⏹ Stop podcast: \"" + titulo + "\"");
	}

	@Override
	public int getDuracion() {
		return getDuracionSegundos();
	}

	@Override
	public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
		if (descargado) {
			throw new ContenidoYaDescargadoException("Ya descargado: " + titulo);
		}
		descargado = true;
		return true;
	}

	@Override
	public boolean eliminarDescarga() {
		boolean antes = descargado;
		descargado = false;
		return antes;
	}

	@Override
	public int espacioRequerido() {
		return Math.max(1, duracionSegundos / 2);
	}
}
