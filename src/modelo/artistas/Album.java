package modelo.artistas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import enums.GeneroMusical;
import excepciones.artista.AlbumCompletoException;
import excepciones.playlist.CancionNoEncontradaException;
import modelo.contenido.Cancion;

public class Album {
	private final String id;
	private final String titulo;
	private final Artista artista;
	private final Date fechaLanzamiento;
	private final ArrayList<Cancion> canciones;
	private final int maxCanciones;

	public Album(String titulo, Artista artista, Date fechaLanzamiento) {
		this.id = UUID.randomUUID().toString();
		this.titulo = (titulo == null || titulo.isBlank()) ? "(sin titulo)" : titulo;
		this.artista = artista;
		this.fechaLanzamiento = (fechaLanzamiento == null) ? new Date() : fechaLanzamiento;
		this.canciones = new ArrayList<>();
		this.maxCanciones = 20;
	}

	public String getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public Artista getArtista() {
		return artista;
	}

	public ArrayList<Cancion> getCanciones() {
		return canciones;
	}

	public int getNumCanciones() {
		return canciones.size();
	}

	public Cancion crearCancion(String titulo, int duracion, GeneroMusical genero) throws AlbumCompletoException, Exception {
		if (canciones.size() >= maxCanciones) {
			throw new AlbumCompletoException("Album completo (max " + maxCanciones + ")");
		}
		Cancion c = new Cancion(titulo, duracion, artista, this, genero, "http://audio/" + titulo.replace(' ', '_'), false,
				"(letra) " + titulo, null);
		canciones.add(c);
		if (artista != null) {
			artista.publicarCancion(c);
		}
		return c;
	}

	public void eliminarCancion(int posicion) throws CancionNoEncontradaException {
		if (posicion < 0 || posicion >= canciones.size()) {
			throw new CancionNoEncontradaException("Posicion invalida: " + posicion);
		}
		canciones.remove(posicion);
	}

	public int getDuracionTotal() {
		int total = 0;
		for (Cancion c : canciones) {
			total += c.getDuracionSegundos();
		}
		return total;
	}

	public void ordenarPorPopularidad() {
		canciones.sort(Comparator.comparingInt(Cancion::getReproducciones).reversed());
	}

	public void shuffle() {
		Collections.shuffle(canciones);
	}
}
