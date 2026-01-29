package modelo.contenido;

import enums.GeneroMusical;
import excepciones.contenido.ArchivoAudioNoEncontradoException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.contenido.LetraNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import interfaces.Descargable;
import interfaces.Reproducible;
import modelo.artistas.Album;
import modelo.artistas.Artista;

public class Cancion extends Contenido implements Reproducible, Descargable {
	private final String letra;
	private final Artista artista;
	private final Album album;
	private GeneroMusical genero;
	private final String audioURL;
	private final boolean explicit;
	private final String ISRC;
	private boolean descargada;

	public Cancion(String titulo, int duracionSegundos, Artista artista, Album album, GeneroMusical genero, String audioURL,
			boolean explicit, String letra, String isrc) throws DuracionInvalidaException {
		super(titulo, duracionSegundos);
		this.artista = artista;
		this.album = album;
		this.genero = genero;
		this.audioURL = audioURL;
		this.explicit = explicit;
		this.letra = letra;
		this.ISRC = (isrc == null || isrc.isBlank()) ? "ISRC-" + getId().substring(0, 8) : isrc;
		this.descargada = false;
	}

	public Artista getArtista() {
		return artista;
	}

	public Album getAlbum() {
		return album;
	}

	public GeneroMusical getGenero() {
		return genero;
	}

	public void cambiarGenero(GeneroMusical nuevoGenero) {
		this.genero = nuevoGenero;
	}

	public boolean esExplicit() {
		return explicit;
	}

	public String obtenerLetra() throws LetraNoDisponibleException {
		if (letra == null || letra.isBlank()) {
			throw new LetraNoDisponibleException("No hay letra para: " + titulo);
		}
		return letra;
	}

	@Override
	public void reproducir() throws Exception {
		play();
	}

	@Override
	public void play() {
		try {
			validarDisponible();
			if (audioURL == null || audioURL.isBlank() || !(audioURL.startsWith("http") || audioURL.startsWith("file:"))) {
				throw new ArchivoAudioNoEncontradoException("URL de audio invalida: " + audioURL);
			}
			aumentarReproducciones();
			System.out.println("♪ Reproduciendo cancion: \"" + titulo + "\" (" + genero + ") - "
					+ (artista != null ? artista.getNombreArtistico() : "(sin artista)"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void pause() {
		System.out.println("⏸ Pausa: \"" + titulo + "\"");
	}

	@Override
	public void stop() {
		System.out.println("⏹ Stop: \"" + titulo + "\"");
	}

	@Override
	public int getDuracion() {
		return getDuracionSegundos();
	}

	@Override
	public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
		if (descargada) {
			throw new ContenidoYaDescargadoException("Ya descargada: " + titulo);
		}
		descargada = true;
		return true;
	}

	@Override
	public boolean eliminarDescarga() {
		boolean antes = descargada;
		descargada = false;
		return antes;
	}

	@Override
	public int espacioRequerido() {
		return Math.max(1, duracionSegundos / 2);
	}
}
