package excepciones.plataforma;

public class UsuarioYaExisteException extends Exception {
	public UsuarioYaExisteException() {
		super();
	}

	public UsuarioYaExisteException(String message) {
		super(message);
	}
}
