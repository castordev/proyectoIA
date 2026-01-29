package modelo.artistas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import excepciones.artista.AlbumYaExisteException;
import modelo.contenido.Cancion;

public class Artista {
	private final String id;
	private final String nombreArtistico;
	private final String nombreReal;
	private final String paisOrigen;
	private final ArrayList<Cancion> discografia;
	private final ArrayList<Album> albumes;
	private int oyentesMensuales;
	private final boolean verificado;
	private final String biografia;

	public Artista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado, String biografia) {
		this.id = UUID.randomUUID().toString();
		this.nombreArtistico = (nombreArtistico == null || nombreArtistico.isBlank()) ? "(sin nombre)" : nombreArtistico;
		this.nombreReal = (nombreReal == null) ? "" : nombreReal;
		this.paisOrigen = (paisOrigen == null) ? "" : paisOrigen;
		this.discografia = new ArrayList<>();
		this.albumes = new ArrayList<>();
		this.oyentesMensuales = 0;
		this.verificado = verificado;
		this.biografia = (biografia == null) ? "" : biografia;
	}

	public String getId() {
		return id;
	}

	public String getNombreArtistico() {
		return nombreArtistico;
	}

	public boolean esVerificado() {
		return verificado;
	}

	public ArrayList<Cancion> getDiscografia() {
		return discografia;
	}

	public ArrayList<Album> getAlbumes() {
		return albumes;
	}

	public void publicarCancion(Cancion cancion) {
		if (cancion != null) {
			discografia.add(cancion);
		}
	}

	public Album crearAlbum(String titulo, Date fecha) throws AlbumYaExisteException {
		for (Album a : albumes) {
			if (a.getTitulo().equalsIgnoreCase(titulo)) {
				throw new AlbumYaExisteException("Album duplicado: " + titulo);
			}
		}
		Album nuevo = new Album(titulo, this, fecha);
		albumes.add(nuevo);
		return nuevo;
	}

	public ArrayList<Cancion> obtenerTopCanciones(int cantidad) {
		ArrayList<Cancion> copia = new ArrayList<>(discografia);
		copia.sort(Comparator.comparingInt(Cancion::getReproducciones).reversed());
		return new ArrayList<>(copia.subList(0, Math.min(cantidad, copia.size())));
	}

	public long getTotalReproducciones() {
		long total = 0;
		for (Cancion c : discografia) {
			total += c.getReproducciones();
		}
		return total;
	}
}
