package modelo.usuarios;

import java.util.ArrayList;

import enums.TipoSuscripcion;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import interfaces.Descargable;
import modelo.contenido.Contenido;

public class UsuarioPremium extends Usuario {
	private boolean descargasOffline;
	private int maxDescargas;
	private ArrayList<Contenido> descargados;
	private String calidadAudio;

	public UsuarioPremium(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException {
		super(nombre, email, password, TipoSuscripcion.PREMIUM);
		this.descargasOffline = true;
		this.maxDescargas = 100;
		this.descargados = new ArrayList<>();
		this.calidadAudio = "HIGH";
	}

	@Override
	public void reproducir(Contenido contenido) throws Exception {
		if (contenido == null) {
			return;
		}
		contenido.reproducir();
		agregarAlHistorial(contenido);
	}

	public void descargar(Contenido contenido) throws LimiteDescargasException, ContenidoYaDescargadoException {
		if (contenido == null) {
			return;
		}
		if (!(contenido instanceof Descargable)) {
			return;
		}
		if (!verificarEspacioDescarga()) {
			throw new LimiteDescargasException("Limite de descargas alcanzado: " + maxDescargas);
		}
		for (Contenido c : descargados) {
			if (c.getId().equals(contenido.getId())) {
				throw new ContenidoYaDescargadoException("Contenido ya descargado: " + contenido.getTitulo());
			}
		}
		((Descargable) contenido).descargar();
		descargados.add(contenido);
	}

	public boolean eliminarDescarga(Contenido contenido) {
		if (contenido == null) {
			return false;
		}
		if (contenido instanceof Descargable) {
			((Descargable) contenido).eliminarDescarga();
		}
		return descargados.removeIf(c -> c.getId().equals(contenido.getId()));
	}

	public boolean verificarEspacioDescarga() {
		return descargados.size() < maxDescargas;
	}

	public ArrayList<Contenido> getDescargados() {
		return descargados;
	}
}
