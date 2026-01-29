package excepciones.playlist;

public class PlaylistVaciaException extends Exception {
	public PlaylistVaciaException() {
		super();
	}

	public PlaylistVaciaException(String message) {
		super(message);
	}
}
