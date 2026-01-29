package excepciones.usuario;

public class LimiteDiarioAlcanzadoException extends Exception {
	public LimiteDiarioAlcanzadoException() {
		super();
	}

	public LimiteDiarioAlcanzadoException(String message) {
		super(message);
	}
}
