package excepciones.usuario;

public class PasswordDebilException extends Exception {
	public PasswordDebilException() {
		super();
	}

	public PasswordDebilException(String message) {
		super(message);
	}
}
