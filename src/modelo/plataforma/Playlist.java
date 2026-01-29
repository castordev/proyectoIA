package modelo.plataforma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import enums.CriterioOrden;
import excepciones.playlist.ContenidoDuplicadoException;
import excepciones.playlist.PlaylistLlenaException;
import excepciones.playlist.PlaylistVaciaException;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.usuarios.Usuario;

public class Playlist {
	private final String id;
	private String nombre;
	private final Usuario creador;
	private final ArrayList<Contenido> contenidos;
	private boolean esPublica;
	private int seguidores;
	private String descripcion;
	private String portadaURL;
	private final Date fechaCreacion;
	private final int maxContenidos;

	public Playlist(String nombre, Usuario creador) {
		this.id = UUID.randomUUID().toString();
		this.nombre = (nombre == null || nombre.isBlank()) ? "(sin nombre)" : nombre;
		this.creador = creador;
		this.contenidos = new ArrayList<>();
		this.esPublica = false;
		this.seguidores = 0;
		this.descripcion = "";
		this.portadaURL = "";
		this.fechaCreacion = new Date();
		this.maxContenidos = 500;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isPublica() {
		return esPublica;
	}

	public int getSeguidores() {
		return seguidores;
	}

	public ArrayList<Contenido> getContenidos() {
		return contenidos;
	}

	public void incrementarSeguidores() {
		seguidores++;
	}

	public void agregarContenido(Contenido contenido) throws PlaylistLlenaException, ContenidoDuplicadoException {
		if (contenido == null) {
			return;
		}
		if (contenidos.size() >= maxContenidos) {
			throw new PlaylistLlenaException("Playlist llena (max " + maxContenidos + ")");
		}
		for (Contenido c : contenidos) {
			if (c.getId().equals(contenido.getId())) {
				throw new ContenidoDuplicadoException("Contenido duplicado: " + contenido.getTitulo());
			}
		}
		contenidos.add(contenido);
	}

	public void eliminarContenido(String idContenido) {
		if (idContenido == null) {
			return;
		}
		contenidos.removeIf(c -> c.getId().equals(idContenido));
	}

	public void ordenarPor(CriterioOrden criterio) throws PlaylistVaciaException {
		if (contenidos.isEmpty()) {
			throw new PlaylistVaciaException("Playlist vacia");
		}
		if (criterio == null || criterio == CriterioOrden.FECHA_AGREGADO) {
			return;
		}
		if (criterio == CriterioOrden.ALEATORIO) {
			shuffle();
			return;
		}
		Comparator<Contenido> comp;
		switch (criterio) {
			case POPULARIDAD:
				comp = Comparator.comparingInt(Contenido::getReproducciones).reversed();
				break;
			case DURACION:
				comp = Comparator.comparingInt(Contenido::getDuracionSegundos);
				break;
			case ALFABETICO:
				comp = Comparator.comparing(Contenido::getTitulo, String.CASE_INSENSITIVE_ORDER);
				break;
			case ARTISTA:
				comp = (a, b) -> getNombreArtista(a).compareToIgnoreCase(getNombreArtista(b));
				break;
			default:
				comp = Comparator.comparing(Contenido::getTitulo, String.CASE_INSENSITIVE_ORDER);
		}
		contenidos.sort(comp);
	}

	private String getNombreArtista(Contenido c) {
		if (c instanceof Cancion) {
			Cancion ca = (Cancion) c;
			return ca.getArtista() != null ? ca.getArtista().getNombreArtistico() : "";
		}
		return "";
	}

	public int getDuracionTotal() {
		int total = 0;
		for (Contenido c : contenidos) {
			total += c.getDuracionSegundos();
		}
		return total;
	}

	public void shuffle() {
		Collections.shuffle(contenidos);
	}

	public void hacerPublica() {
		esPublica = true;
	}

	public void hacerPrivada() {
		esPublica = false;
	}
}
