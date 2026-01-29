package excepciones.playlist;

public class CancionNoEncontradaException extends Exception {
	public CancionNoEncontradaException() {
		super();
	}

	public CancionNoEncontradaException(String message) {
		super(message);
	}
}
