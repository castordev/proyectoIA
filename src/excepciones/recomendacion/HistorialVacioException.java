package excepciones.recomendacion;

public class HistorialVacioException extends RecomendacionException {
	public HistorialVacioException() {
		super();
	}

	public HistorialVacioException(String message) {
		super(message);
	}
}
