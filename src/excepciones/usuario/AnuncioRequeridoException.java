package excepciones.usuario;

public class AnuncioRequeridoException extends Exception {
	public AnuncioRequeridoException() {
		super();
	}

	public AnuncioRequeridoException(String message) {
		super(message);
	}
}
