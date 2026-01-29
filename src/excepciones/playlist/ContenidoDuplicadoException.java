package excepciones.playlist;

public class ContenidoDuplicadoException extends Exception {
	public ContenidoDuplicadoException() {
		super();
	}

	public ContenidoDuplicadoException(String message) {
		super(message);
	}
}
