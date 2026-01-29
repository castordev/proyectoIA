package modelo.artistas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import enums.CategoriaPodcast;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.EpisodioNoEncontradoException;
import modelo.contenido.Podcast;
import utilidades.EstadisticasCreador;

public class Creador {
	private final String id;
	private final String nombreCanal;
	private final String nombre;
	private final ArrayList<Podcast> episodios;
	private int suscriptores;
	private final String descripcion;
	private final HashMap<String, String> redesSociales;
	private final ArrayList<CategoriaPodcast> categoriasPrincipales;
	private final int maxEpisodios;

	public Creador(String nombreCanal, String nombre, String descripcion) {
		this.id = UUID.randomUUID().toString();
		this.nombreCanal = (nombreCanal == null || nombreCanal.isBlank()) ? "(sin canal)" : nombreCanal;
		this.nombre = (nombre == null || nombre.isBlank()) ? "(sin nombre)" : nombre;
		this.episodios = new ArrayList<>();
		this.suscriptores = 0;
		this.descripcion = (descripcion == null) ? "" : descripcion;
		this.redesSociales = new HashMap<>();
		this.categoriasPrincipales = new ArrayList<>();
		this.maxEpisodios = 500;
	}

	public String getId() {
		return id;
	}

	public String getNombreCanal() {
		return nombreCanal;
	}

	public int getSuscriptores() {
		return suscriptores;
	}

	public void setSuscriptores(int suscriptores) {
		this.suscriptores = Math.max(0, suscriptores);
	}

	public ArrayList<Podcast> getEpisodios() {
		return episodios;
	}

	public void publicarPodcast(Podcast episodio) throws LimiteEpisodiosException {
		if (episodios.size() >= maxEpisodios) {
			throw new LimiteEpisodiosException("Limite de episodios alcanzado: " + maxEpisodios);
		}
		if (episodio != null) {
			episodios.add(episodio);
			if (episodio.getCategoria() != null && !categoriasPrincipales.contains(episodio.getCategoria())) {
				categoriasPrincipales.add(episodio.getCategoria());
			}
		}
	}

	public EstadisticasCreador obtenerEstadisticas() {
		return new EstadisticasCreador(this);
	}

	public void eliminarEpisodio(String idEpisodio) throws EpisodioNoEncontradoException {
		boolean removed = episodios.removeIf(p -> p.getId().equals(idEpisodio));
		if (!removed) {
			throw new EpisodioNoEncontradoException("No existe episodio con id: " + idEpisodio);
		}
	}

	public double calcularPromedioReproducciones() {
		if (episodios.isEmpty()) {
			return 0.0;
		}
		long total = 0;
		for (Podcast p : episodios) {
			total += p.getReproducciones();
		}
		return total / (double) episodios.size();
	}
}
