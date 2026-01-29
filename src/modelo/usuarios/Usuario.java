package modelo.usuarios;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import enums.TipoSuscripcion;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Playlist;

public abstract class Usuario {
	protected final String id;
	protected String nombre;
	protected String email;
	protected String password;
	protected TipoSuscripcion suscripcion;
	protected ArrayList<Playlist> misPlaylists;
	protected ArrayList<Contenido> historial;
	protected Date fechaRegistro;

	protected Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion)
			throws EmailInvalidoException, PasswordDebilException {
		this.id = UUID.randomUUID().toString();
		this.nombre = (nombre == null || nombre.isBlank()) ? "(sin nombre)" : nombre;
		this.email = (email == null) ? "" : email.trim();
		this.password = (password == null) ? "" : password;
		this.suscripcion = suscripcion;
		this.misPlaylists = new ArrayList<>();
		this.historial = new ArrayList<>();
		this.fechaRegistro = new Date();
		validarEmail();
		validarPassword();
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getEmail() {
		return email;
	}

	public TipoSuscripcion getSuscripcion() {
		return suscripcion;
	}

	public ArrayList<Playlist> getMisPlaylists() {
		return misPlaylists;
	}

	public ArrayList<Contenido> getHistorial() {
		return historial;
	}

	public abstract void reproducir(Contenido contenido) throws Exception;

	public Playlist crearPlaylist(String nombre) {
		Playlist p = new Playlist(nombre, this);
		misPlaylists.add(p);
		return p;
	}

	public void seguirPlaylist(Playlist playlist) {
		if (playlist != null && playlist.isPublica()) {
			playlist.incrementarSeguidores();
		}
	}

	public void darLike(Contenido contenido) {
		if (contenido != null) {
			contenido.agregarLike();
		}
	}

	public void validarEmail() throws EmailInvalidoException {
		Pattern p = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
		if (email == null || !p.matcher(email).matches()) {
			throw new EmailInvalidoException("Email invalido: " + email);
		}
	}

	public void validarPassword() throws PasswordDebilException {
		if (password == null || password.length() < 8) {
			throw new PasswordDebilException("Contrasena debil: minimo 8 caracteres");
		}
	}

	protected void agregarAlHistorial(Contenido contenido) {
		if (contenido != null) {
			historial.add(contenido);
		}
	}
}
