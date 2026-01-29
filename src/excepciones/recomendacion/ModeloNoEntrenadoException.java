package excepciones.recomendacion;

public class ModeloNoEntrenadoException extends RecomendacionException {
	public ModeloNoEntrenadoException() {
		super();
	}

	public ModeloNoEntrenadoException(String message) {
		super(message);
	}
}
