package utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import enums.CategoriaPodcast;
import enums.GeneroMusical;
import excepciones.recomendacion.HistorialVacioException;
import excepciones.recomendacion.ModeloNoEntrenadoException;
import excepciones.recomendacion.RecomendacionException;
import interfaces.Recomendador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.plataforma.Plataforma;
import modelo.usuarios.Usuario;

public class RecomendadorIA implements Recomendador {
	private final HashMap<String, ArrayList<String>> matrizPreferencias;
	private final HashMap<String, ArrayList<Contenido>> historialCompleto;
	private boolean entrenado;

	public RecomendadorIA() {
		this.matrizPreferencias = new HashMap<>();
		this.historialCompleto = new HashMap<>();
		this.entrenado = false;
	}

	public void entrenarModelo(ArrayList<Usuario> usuarios) {
		if (usuarios == null) {
			entrenado = true;
			return;
		}
		for (Usuario u : usuarios) {
			if (u == null) {
				continue;
			}
			historialCompleto.put(u.getId(), new ArrayList<>(u.getHistorial()));
			actualizarPreferencias(u);
		}
		entrenado = true;
	}

	public void actualizarPreferencias(Usuario usuario) {
		if (usuario == null) {
			return;
		}
		ArrayList<String> prefs = new ArrayList<>();
		for (Contenido c : usuario.getHistorial()) {
			if (c instanceof Cancion) {
				GeneroMusical g = ((Cancion) c).getGenero();
				if (g != null) {
					prefs.add("GENERO:" + g.name());
				}
			}
			if (c instanceof Podcast) {
				CategoriaPodcast cat = ((Podcast) c).getCategoria();
				if (cat != null) {
					prefs.add("CATEGORIA:" + cat.name());
				}
			}
		}
		matrizPreferencias.put(usuario.getId(), prefs);
	}

	@Override
	public ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException {
		if (!entrenado) {
			throw new ModeloNoEntrenadoException("Modelo no entrenado");
		}
		if (usuario == null) {
			throw new RecomendacionException("Usuario nulo");
		}
		if (usuario.getHistorial().isEmpty()) {
			throw new HistorialVacioException("Historial vacio para recomendar");
		}
		actualizarPreferencias(usuario);
		ArrayList<String> prefs = matrizPreferencias.getOrDefault(usuario.getId(), new ArrayList<>());
		Set<String> vistos = new HashSet<>();
		for (Contenido c : usuario.getHistorial()) {
			vistos.add(c.getId());
		}
		ArrayList<Contenido> recs = new ArrayList<>();
		for (Contenido c : Plataforma.getInstancia().getCatalogo()) {
			if (vistos.contains(c.getId())) {
				continue;
			}
			if (c instanceof Cancion) {
				GeneroMusical g = ((Cancion) c).getGenero();
				if (g != null && prefs.contains("GENERO:" + g.name())) {
					recs.add(c);
				}
			}
			if (c instanceof Podcast) {
				CategoriaPodcast cat = ((Podcast) c).getCategoria();
				if (cat != null && prefs.contains("CATEGORIA:" + cat.name())) {
					recs.add(c);
				}
			}
			if (recs.size() >= 10) {
				break;
			}
		}
		return recs;
	}

	@Override
	public ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException {
		if (!entrenado) {
			throw new ModeloNoEntrenadoException("Modelo no entrenado");
		}
		if (contenido == null) {
			throw new RecomendacionException("Contenido nulo");
		}
		ArrayList<Contenido> res = new ArrayList<>();
		if (contenido instanceof Cancion) {
			GeneroMusical g = ((Cancion) contenido).getGenero();
			for (Contenido c : Plataforma.getInstancia().getCatalogo()) {
				if (c instanceof Cancion && ((Cancion) c).getGenero() == g && !c.getId().equals(contenido.getId())) {
					res.add(c);
				}
			}
		}
		if (contenido instanceof Podcast) {
			CategoriaPodcast cat = ((Podcast) contenido).getCategoria();
			for (Contenido c : Plataforma.getInstancia().getCatalogo()) {
				if (c instanceof Podcast && ((Podcast) c).getCategoria() == cat && !c.getId().equals(contenido.getId())) {
					res.add(c);
				}
			}
		}
		return res;
	}
}
