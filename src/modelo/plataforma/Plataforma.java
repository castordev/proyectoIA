package modelo.plataforma;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import enums.CategoriaPodcast;
import enums.GeneroMusical;
import enums.TipoAnuncio;
import enums.TipoSuscripcion;
import excepciones.artista.AlbumYaExisteException;
import excepciones.artista.ArtistaNoVerificadoException;
import excepciones.plataforma.ArtistaNoEncontradoException;
import excepciones.plataforma.ContenidoNoEncontradoException;
import excepciones.plataforma.UsuarioYaExisteException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.artistas.Album;
import modelo.artistas.Artista;
import modelo.artistas.Creador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.usuarios.Usuario;
import modelo.usuarios.UsuarioGratuito;
import modelo.usuarios.UsuarioPremium;
import utilidades.RecomendadorIA;

public class Plataforma {
	private static Plataforma instancia;

	private final String nombre;
	private final ArrayList<UsuarioPremium> usuariosPremium;
	private final ArrayList<UsuarioGratuito> usuariosGratuitos;
	private final ArrayList<Contenido> catalogo;
	private final ArrayList<Cancion> canciones;
	private final ArrayList<Podcast> podcasts;
	private final ArrayList<Playlist> playlistsPublicas;
	private final HashMap<String, Artista> artistas;
	private final ArrayList<Album> albumes;
	private final HashMap<String, Creador> creadores;
	private final ArrayList<Anuncio> anuncios;
	private final RecomendadorIA recomendador;
	private final Random random;

	private Plataforma(String nombre) {
		this.nombre = (nombre == null || nombre.isBlank()) ? "SoundWave" : nombre;
		this.usuariosPremium = new ArrayList<>();
		this.usuariosGratuitos = new ArrayList<>();
		this.catalogo = new ArrayList<>();
		this.canciones = new ArrayList<>();
		this.podcasts = new ArrayList<>();
		this.playlistsPublicas = new ArrayList<>();
		this.artistas = new HashMap<>();
		this.albumes = new ArrayList<>();
		this.creadores = new HashMap<>();
		this.anuncios = new ArrayList<>();
		this.recomendador = new RecomendadorIA();
		this.random = new Random();
	}

	public static synchronized Plataforma getInstancia(String nombre) {
		if (instancia == null) {
			instancia = new Plataforma(nombre);
		}
		return instancia;
	}

	public static synchronized Plataforma getInstancia() {
		return getInstancia("SoundWave");
	}

	public static synchronized void reiniciarInstancia() {
		instancia = null;
	}

	public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password)
			throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
		if (emailExiste(email)) {
			throw new UsuarioYaExisteException("Email ya registrado: " + email);
		}
		UsuarioPremium u = new UsuarioPremium(nombre, email, password);
		usuariosPremium.add(u);
		return u;
	}

	public UsuarioGratuito registrarUsuarioGratuito(String nombre, String email, String password)
			throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
		if (emailExiste(email)) {
			throw new UsuarioYaExisteException("Email ya registrado: " + email);
		}
		UsuarioGratuito u = new UsuarioGratuito(nombre, email, password);
		usuariosGratuitos.add(u);
		return u;
	}

	private boolean emailExiste(String email) {
		if (email == null) {
			return false;
		}
		for (UsuarioPremium u : usuariosPremium) {
			if (u.getEmail().equalsIgnoreCase(email)) {
				return true;
			}
		}
		for (UsuarioGratuito u : usuariosGratuitos) {
			if (u.getEmail().equalsIgnoreCase(email)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<UsuarioPremium> getUsuariosPremium() {
		return usuariosPremium;
	}

	public ArrayList<UsuarioGratuito> getUsuariosGratuitos() {
		return usuariosGratuitos;
	}

	public ArrayList<Usuario> getTodosLosUsuarios() {
		ArrayList<Usuario> all = new ArrayList<>();
		all.addAll(usuariosPremium);
		all.addAll(usuariosGratuitos);
		return all;
	}

	public Artista registrarArtista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado,
			String biografia) {
		Artista a = new Artista(nombreArtistico, nombreReal, paisOrigen, verificado, biografia);
		artistas.put(a.getId(), a);
		return a;
	}

	public Artista buscarArtista(String nombre) throws ArtistaNoEncontradoException {
		if (nombre == null) {
			throw new ArtistaNoEncontradoException("Nombre nulo");
		}
		for (Artista a : artistas.values()) {
			if (a.getNombreArtistico().equalsIgnoreCase(nombre)) {
				return a;
			}
		}
		throw new ArtistaNoEncontradoException("No existe artista: " + nombre);
	}

	public Album crearAlbum(Artista artista, String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException {
		if (artista == null) {
			throw new ArtistaNoVerificadoException("Artista nulo");
		}
		if (!artista.esVerificado()) {
			throw new ArtistaNoVerificadoException("Artista no verificado no puede crear album: " + artista.getNombreArtistico());
		}
		Album a = artista.crearAlbum(titulo, fecha);
		albumes.add(a);
		return a;
	}

	public Cancion crearCancionEnAlbum(String titulo, int duracion, Artista artista, Album album, GeneroMusical genero) throws Exception {
		Cancion c = album.crearCancion(titulo, duracion, genero);
		canciones.add(c);
		catalogo.add(c);
		return c;
	}

	public Creador registrarCreador(String nombreCanal, String nombre, String descripcion) {
		Creador c = new Creador(nombreCanal, nombre, descripcion);
		creadores.put(c.getId(), c);
		return c;
	}

	public Podcast crearPodcast(String titulo, int duracion, Creador creador, int temporada, int episodio, CategoriaPodcast categoria)
			throws Exception {
		Podcast p = new Podcast(titulo, duracion, creador, temporada, episodio, "Episodio " + episodio, categoria, "");
		podcasts.add(p);
		catalogo.add(p);
		if (creador != null) {
			creador.publicarPodcast(p);
		}
		return p;
	}

	public ArrayList<Contenido> getCatalogo() {
		return catalogo;
	}

	public ArrayList<Cancion> getCanciones() {
		return canciones;
	}

	public ArrayList<Podcast> getPodcasts() {
		return podcasts;
	}

	public ArrayList<Album> getAlbumes() {
		return albumes;
	}

	public ArrayList<Creador> getTodosLosCreadores() {
		return new ArrayList<>(creadores.values());
	}

	public ArrayList<Contenido> buscarContenido(String termino) throws ContenidoNoEncontradoException {
		ArrayList<Contenido> res = new ArrayList<>();
		if (termino == null) {
			throw new ContenidoNoEncontradoException("Termino nulo");
		}
		String t = termino.toLowerCase();
		for (Contenido c : catalogo) {
			if (c.getTitulo().toLowerCase().contains(t)) {
				res.add(c);
			}
		}
		if (res.isEmpty()) {
			throw new ContenidoNoEncontradoException("No se encontro contenido para: " + termino);
		}
		return res;
	}

	public ArrayList<Cancion> buscarPorGenero(GeneroMusical genero) {
		ArrayList<Cancion> res = new ArrayList<>();
		for (Cancion c : canciones) {
			if (c.getGenero() == genero) {
				res.add(c);
			}
		}
		return res;
	}

	public ArrayList<Podcast> buscarPorCategoria(CategoriaPodcast categoria) {
		ArrayList<Podcast> res = new ArrayList<>();
		for (Podcast p : podcasts) {
			if (p.getCategoria() == categoria) {
				res.add(p);
			}
		}
		return res;
	}

	public ArrayList<Contenido> obtenerTopContenidos(int cantidad) {
		ArrayList<Contenido> copia = new ArrayList<>(catalogo);
		copia.sort(Comparator.comparingInt(Contenido::getReproducciones).reversed());
		return new ArrayList<>(copia.subList(0, Math.min(cantidad, copia.size())));
	}

	public Playlist crearPlaylistPublica(String nombre, Usuario creador) {
		Playlist p = new Playlist(nombre, creador);
		p.hacerPublica();
		playlistsPublicas.add(p);
		return p;
	}

	public ArrayList<Playlist> getPlaylistsPublicas() {
		return playlistsPublicas;
	}

	public void agregarAnuncio(Anuncio anuncio) {
		if (anuncio != null) {
			anuncios.add(anuncio);
		}
	}

	public Anuncio obtenerAnuncioAleatorio() {
		ArrayList<Anuncio> activos = new ArrayList<>();
		for (Anuncio a : anuncios) {
			if (a.isActivo()) {
				activos.add(a);
			}
		}
		if (activos.isEmpty()) {
			return null;
		}
		return activos.get(random.nextInt(activos.size()));
	}

	public RecomendadorIA getRecomendador() {
		return recomendador;
	}

	public String obtenerEstadisticasGenerales() {
		int totalUsuarios = usuariosPremium.size() + usuariosGratuitos.size();
		double ingresos = 0.0;
		for (UsuarioPremium u : usuariosPremium) {
			TipoSuscripcion s = u.getSuscripcion();
			ingresos += s.getPrecioMensual();
		}
		int totalAnuncios = 0;
		for (Anuncio a : anuncios) {
			totalAnuncios += a.getImpresiones();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("========================\n");
		sb.append("Total Usuarios: ").append(totalUsuarios).append(" (").append(usuariosPremium.size()).append(" Premium, ")
				.append(usuariosGratuitos.size()).append(" Gratuitos)\n");
		sb.append("Total Contenido: ").append(catalogo.size()).append(" (").append(canciones.size()).append(" canciones, ")
				.append(podcasts.size()).append(" podcasts)\n");
		sb.append("Ingresos mensuales estimados: $").append(String.format("%.2f", ingresos)).append("\n");
		sb.append("Total anuncios reproducidos: ").append(totalAnuncios).append("\n");
		sb.append("========================\n");
		return sb.toString();
	}

	public void seedAnunciosPorDefecto() {
		agregarAnuncio(new Anuncio("Nike", "http://ads/nike", TipoAnuncio.AUDIO, 5.0));
		agregarAnuncio(new Anuncio("Coca-Cola", "http://ads/coke", TipoAnuncio.VIDEO, 10.0));
		agregarAnuncio(new Anuncio("Amazon", "http://ads/amazon", TipoAnuncio.AUDIO, 8.0));
	}
}
