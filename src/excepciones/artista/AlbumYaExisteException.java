package excepciones.artista;

public class AlbumYaExisteException extends Exception {
	public AlbumYaExisteException() {
		super();
	}

	public AlbumYaExisteException(String message) {
		super(message);
	}
}
