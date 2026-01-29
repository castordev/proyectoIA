package modelo.usuarios;

import java.util.Date;

import enums.TipoSuscripcion;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Anuncio;
import modelo.plataforma.Plataforma;

public class UsuarioGratuito extends Usuario {
	private int anunciosEscuchados;
	private Date ultimoAnuncio;
	private int reproduccionesHoy;
	private int limiteReproducciones;
	private boolean anuncioPendiente;

	public UsuarioGratuito(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException {
		super(nombre, email, password, TipoSuscripcion.GRATUITO);
		this.anunciosEscuchados = 0;
		this.ultimoAnuncio = null;
		this.reproduccionesHoy = 0;
		this.limiteReproducciones = 50;
		this.anuncioPendiente = false;
	}

	@Override
	public void reproducir(Contenido contenido) throws Exception {
		if (contenido == null) {
			return;
		}
		if (!puedeReproducir()) {
			throw new LimiteDiarioAlcanzadoException("Limite diario alcanzado: " + limiteReproducciones);
		}
		if (anuncioPendiente) {
			throw new AnuncioRequeridoException("Debe ver un anuncio antes de continuar");
		}
		contenido.reproducir();
		reproduccionesHoy++;
		agregarAlHistorial(contenido);
		if (reproduccionesHoy % 3 == 0) {
			anuncioPendiente = true;
			throw new AnuncioRequeridoException("ANUNCIO REQUERIDO tras 3 reproducciones");
		}
	}

	public void verAnuncio() {
		Anuncio a = Plataforma.getInstancia().obtenerAnuncioAleatorio();
		if (a != null) {
			a.reproducir();
			anunciosEscuchados++;
			ultimoAnuncio = new Date();
			anuncioPendiente = false;
		}
	}

	public boolean puedeReproducir() {
		return reproduccionesHoy < limiteReproducciones;
	}

	public void reiniciarContadorDiario() {
		reproduccionesHoy = 0;
		anuncioPendiente = false;
	}

	public int getAnunciosEscuchados() {
		return anunciosEscuchados;
	}
}
