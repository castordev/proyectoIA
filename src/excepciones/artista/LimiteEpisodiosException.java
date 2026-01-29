package excepciones.artista;

public class LimiteEpisodiosException extends Exception {
	public LimiteEpisodiosException() {
		super();
	}

	public LimiteEpisodiosException(String message) {
		super(message);
	}
}
