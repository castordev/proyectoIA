package excepciones.descarga;

public class LimiteDescargasException extends Exception {
	public LimiteDescargasException() {
		super();
	}

	public LimiteDescargasException(String message) {
		super(message);
	}
}
