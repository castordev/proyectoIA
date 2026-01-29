package excepciones.descarga;

public class ContenidoYaDescargadoException extends Exception {
	public ContenidoYaDescargadoException() {
		super();
	}

	public ContenidoYaDescargadoException(String message) {
		super(message);
	}
}
