import java.util.ArrayList;
import java.util.Date;

import enums.CategoriaPodcast;
import enums.CriterioOrden;
import enums.GeneroMusical;
import excepciones.artista.AlbumCompletoException;
import excepciones.artista.AlbumYaExisteException;
import excepciones.artista.ArtistaNoVerificadoException;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.LetraNoDisponibleException;
import excepciones.contenido.TranscripcionNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import excepciones.playlist.ContenidoDuplicadoException;
import excepciones.playlist.PlaylistLlenaException;
import excepciones.playlist.PlaylistVaciaException;
import excepciones.playlist.CancionNoEncontradaException;
import excepciones.plataforma.ArtistaNoEncontradoException;
import excepciones.plataforma.ContenidoNoEncontradoException;
import excepciones.plataforma.UsuarioYaExisteException;
import excepciones.recomendacion.HistorialVacioException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.artistas.Album;
import modelo.artistas.Artista;
import modelo.artistas.Creador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.plataforma.Plataforma;
import modelo.plataforma.Playlist;
import modelo.usuarios.UsuarioGratuito;
import modelo.usuarios.UsuarioPremium;
import utilidades.EstadisticasCreador;

public class Main {
	public static void main(String[] args) {
		System.out.println("==========================================");
		System.out.println(" SOUNDWAVE - SISTEMA DE STREAMING ");
		System.out.println("==========================================");

		System.out.println("\n[ESCENARIO 1: REGISTRO DE USUARIOS]");
		Plataforma.reiniciarInstancia();
		Plataforma plataforma = Plataforma.getInstancia("SoundWave");
		plataforma.seedAnunciosPorDefecto();
		System.out.println("✓ Plataforma creada exitosamente");

		try {
			plataforma.registrarUsuarioPremium("Juan", "juan@gmail", "password123");
		} catch (EmailInvalidoException e) {
			System.out.println("✗ Error: Email invalido - juan@gmail (EmailInvalidoException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		try {
			plataforma.registrarUsuarioPremium("Ana", "ana@gmail.com", "123");
		} catch (PasswordDebilException e) {
			System.out.println("✗ Error: Contrasena debil - minimo 8 caracteres (PasswordDebilException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		UsuarioPremium uP1;
		UsuarioPremium uP2;
		UsuarioPremium uP3;
		UsuarioGratuito uG1;
		UsuarioGratuito uG2;
		try {
			uP1 = plataforma.registrarUsuarioPremium("Juan Perez", "juan@gmail.com", "password123");
			uP2 = plataforma.registrarUsuarioPremium("Maria Lopez", "maria@hotmail.com", "password123");
			uP3 = plataforma.registrarUsuarioPremium("Luis Gomez", "luis@yahoo.com", "password123");
			uG1 = plataforma.registrarUsuarioGratuito("Pedro Garcia", "pedro@yahoo.com", "password123");
			uG2 = plataforma.registrarUsuarioGratuito("Sofia Diaz", "sofia@gmail.com", "password123");
			System.out.println("✓ 5 usuarios registrados correctamente");
		} catch (UsuarioYaExisteException | EmailInvalidoException | PasswordDebilException e) {
			System.out.println("✗ Error registrando usuarios: " + e.getMessage());
			return;
		}

		System.out.println("\n[ESCENARIO 2: CREACION DE CONTENIDO]");
		Artista a1 = plataforma.registrarArtista("Bad Bunny", "Benito", "PR", true, "Artista urbano");
		Artista a2 = plataforma.registrarArtista("Rosalia", "Rosalia", "ES", true, "Artista pop");
		Artista a3 = plataforma.registrarArtista("Daft Punk", "Guy-Man", "FR", true, "Electronica");
		Artista a4 = plataforma.registrarArtista("Indie Kid", "Anon", "MX", false, "Nuevo");
		Artista a5 = plataforma.registrarArtista("NoVerify", "X", "US", false, "No verificado");
		System.out.println("✓ Artistas creados: 3 verificados, 2 no verificados");

		Album alb1;
		Album alb2;
		Album alb3;
		try {
			alb1 = plataforma.crearAlbum(a1, "Un Verano Sin Ti", new Date());
			alb2 = plataforma.crearAlbum(a2, "Motomami", new Date());
			alb3 = plataforma.crearAlbum(a3, "Discovery", new Date());
			System.out.println("✓ 3 albums creados");
		} catch (Exception e) {
			System.out.println("✗ Error creando albums: " + e.getMessage());
			return;
		}

		try {
			plataforma.crearAlbum(a4, "Album No Verificado", new Date());
		} catch (ArtistaNoVerificadoException e) {
			System.out.println("✗ Error: Artista no verificado no puede crear album (ArtistaNoVerificadoException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		ArrayList<Cancion> poolCanciones = new ArrayList<>();
		try {
			for (int i = 1; i <= 6; i++) {
				poolCanciones.add(plataforma.crearCancionEnAlbum("Love Track " + i, 180 + i, a1, alb1, GeneroMusical.REGGAETON));
			}
			for (int i = 1; i <= 6; i++) {
				poolCanciones.add(plataforma.crearCancionEnAlbum("Pop Song " + i, 170 + i, a2, alb2, GeneroMusical.POP));
			}
			for (int i = 1; i <= 6; i++) {
				poolCanciones.add(plataforma.crearCancionEnAlbum("Electro " + i, 200 + i, a3, alb3, GeneroMusical.ELECTRONICA));
			}
			System.out.println("✓ Canciones creadas y agregadas al catalogo: " + poolCanciones.size());
		} catch (Exception e) {
			System.out.println("✗ Error creando canciones: " + e.getMessage());
			return;
		}

		Creador c1 = plataforma.registrarCreador("TechTalks", "Laura", "Tecnologia y software");
		Creador c2 = plataforma.registrarCreador("ComedyHub", "Pepe", "Comedia semanal");
		c1.setSuscriptores(1200);
		c2.setSuscriptores(900);
		ArrayList<Podcast> poolPodcasts = new ArrayList<>();
		try {
			for (int i = 1; i <= 5; i++) {
				poolPodcasts.add(plataforma.crearPodcast("AI News " + i, 1500 + i * 10, c1, 5, i, CategoriaPodcast.TECNOLOGIA));
			}
			for (int i = 1; i <= 5; i++) {
				poolPodcasts.add(plataforma.crearPodcast("Standup " + i, 1200 + i * 10, c2, 2, i, CategoriaPodcast.COMEDIA));
			}
			System.out.println("✓ Podcasts creados y agregados al catalogo: " + poolPodcasts.size());
		} catch (Exception e) {
			System.out.println("✗ Error creando podcasts: " + e.getMessage());
			return;
		}

		System.out.println("\n[ESCENARIO 3: REPRODUCCION Y LIMITES]");
		try {
			System.out.println("-- Usuario gratuito reproduce 3 canciones consecutivas --");
			for (int i = 0; i < 3; i++) {
				try {
					uG1.reproducir(poolCanciones.get(i));
				} catch (AnuncioRequeridoException e) {
					System.out.println("⚠ [ANUNCIO REQUERIDO] " + e.getMessage());
					uG1.verAnuncio();
				}
			}
		} catch (Exception e) {
			System.out.println("✗ Error reproduciendo: " + e.getMessage());
		}

		try {
			System.out.println("-- Usuario gratuito intenta reproducir 51 canciones en un dia --");
			uG1.reiniciarContadorDiario();
			for (int i = 0; i < 51; i++) {
				try {
					uG1.reproducir(poolCanciones.get(i % poolCanciones.size()));
				} catch (AnuncioRequeridoException e) {
					uG1.verAnuncio();
				} catch (LimiteDiarioAlcanzadoException e) {
					System.out.println("✗ Limite diario alcanzado (LimiteDiarioAlcanzadoException)");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("✗ Error en limite diario: " + e.getMessage());
		}

		try {
			System.out.println("-- Usuario premium reproduce 10 canciones sin interrupciones --");
			for (int i = 0; i < 10; i++) {
				uP1.reproducir(poolCanciones.get(i % poolCanciones.size()));
			}
		} catch (Exception e) {
			System.out.println("✗ Error premium reproduciendo: " + e.getMessage());
		}

		try {
			System.out.println("-- Usuario premium descarga 5 canciones --");
			for (int i = 0; i < 5; i++) {
				uP1.descargar(poolCanciones.get(i));
			}
			System.out.println("✓ Descargas realizadas: " + uP1.getDescargados().size());
			System.out.println("-- Usuario premium intenta descargar la misma cancion --");
			uP1.descargar(poolCanciones.get(0));
		} catch (ContenidoYaDescargadoException e) {
			System.out.println("✗ Error: Contenido ya descargado (ContenidoYaDescargadoException)");
		} catch (LimiteDescargasException e) {
			System.out.println("✗ Error: Limite de descargas (LimiteDescargasException)");
		} catch (Exception e) {
			System.out.println("✗ Error descargas: " + e.getMessage());
		}

		System.out.println("\n[ESCENARIO 4: GESTION DE PLAYLISTS]");
		Playlist priv1 = uP1.crearPlaylist("Privada 1");
		Playlist priv2 = uP1.crearPlaylist("Privada 2");
		Playlist pub1 = plataforma.crearPlaylistPublica("Publica de Gratis", uG2);
		System.out.println("✓ Playlists creadas (2 privadas premium, 1 publica gratuito)");

		try {
			for (int i = 0; i < poolCanciones.size(); i++) {
				pub1.agregarContenido(poolCanciones.get(i));
			}
			pub1.agregarContenido(poolPodcasts.get(0));
			pub1.agregarContenido(poolPodcasts.get(1));
			System.out.println("✓ 20 canciones agregadas a playlist");
		} catch (Exception e) {
			System.out.println("✗ Error agregando 20 canciones: " + e.getMessage());
		}

		try {
			pub1.agregarContenido(poolCanciones.get(0));
		} catch (ContenidoDuplicadoException e) {
			System.out.println("✗ Error: Contenido duplicado (ContenidoDuplicadoException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		try {
			for (int i = pub1.getContenidos().size(); i < 501; i++) {
				Cancion filler = new Cancion("Filler " + i, 120, a1, null, GeneroMusical.INDIE, "http://audio/filler" + i,
						false, "", null);
				pub1.agregarContenido(filler);
			}
		} catch (PlaylistLlenaException e) {
			System.out.println("✗ Error: Playlist llena (PlaylistLlenaException)");
		} catch (ContenidoDuplicadoException e) {
			System.out.println("(Info) Se detecto duplicado durante llenado: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("✗ Error inesperado llenando playlist: " + e.getMessage());
		}

		try {
			pub1.ordenarPor(CriterioOrden.POPULARIDAD);
			System.out.println("✓ Playlist ordenada por popularidad");
			pub1.ordenarPor(CriterioOrden.DURACION);
			System.out.println("✓ Playlist ordenada por duracion");
			pub1.shuffle();
			System.out.println("✓ Playlist en modo shuffle");
		} catch (PlaylistVaciaException e) {
			System.out.println("✗ Error: Playlist vacia (PlaylistVaciaException)");
		}

		uP1.seguirPlaylist(pub1);
		System.out.println("✓ Usuario premium sigue playlist publica. Seguidores: " + pub1.getSeguidores());

		System.out.println("\n[ESCENARIO 5: BUSQUEDAS Y FILTROS]");
		try {
			ArrayList<Contenido> love = plataforma.buscarContenido("love");
			System.out.println("✓ Buscar termino 'love': encontrados " + love.size());
		} catch (ContenidoNoEncontradoException e) {
			System.out.println("✗ No se encontro contenido para 'love' (ContenidoNoEncontradoException)");
		}
		System.out.println("✓ Canciones REGGAETON: " + plataforma.buscarPorGenero(GeneroMusical.REGGAETON).size());
		System.out.println("✓ Podcasts TECNOLOGIA: " + plataforma.buscarPorCategoria(CategoriaPodcast.TECNOLOGIA).size());
		try {
			plataforma.buscarContenido("no-existe-xyz");
		} catch (ContenidoNoEncontradoException e) {
			System.out.println("✗ Contenido inexistente (ContenidoNoEncontradoException)");
		}
		System.out.println("✓ Top 10 contenidos: " + plataforma.obtenerTopContenidos(10).size());
		try {
			Artista found = plataforma.buscarArtista("Bad Bunny");
			System.out.println("✓ Artista encontrado: " + found.getNombreArtistico());
			plataforma.buscarArtista("Artista Inexistente");
		} catch (ArtistaNoEncontradoException e) {
			System.out.println("✗ Artista inexistente (ArtistaNoEncontradoException)");
		}

		System.out.println("\n[ESCENARIO 6: SISTEMA DE RECOMENDACIONES]");
		plataforma.getRecomendador().entrenarModelo(plataforma.getTodosLosUsuarios());
		try {
			System.out.println("✓ Recomendaciones para usuario con historial: " + plataforma.getRecomendador().recomendar(uP1).size());
		} catch (Exception e) {
			System.out.println("✗ Error recomendaciones: " + e.getMessage());
		}
		try {
			UsuarioGratuito sinHist = plataforma.registrarUsuarioGratuito("Nuevo", "nuevo@gmail.com", "password123");
			plataforma.getRecomendador().recomendar(sinHist);
		} catch (HistorialVacioException e) {
			System.out.println("✗ Historial vacio (HistorialVacioException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		System.out.println("\n[ESCENARIO 7: GESTION DE ALBUMES Y ARTISTAS]");
		try {
			Album nuevo = plataforma.crearAlbum(a1, "Nuevo Album", new Date());
			System.out.println("✓ Artista publica nuevo album: " + nuevo.getTitulo());
			try {
				plataforma.crearAlbum(a1, "Nuevo Album", new Date());
			} catch (AlbumYaExisteException e) {
				System.out.println("✗ Album duplicado (AlbumYaExisteException)");
			}
			while (nuevo.getNumCanciones() < 20) {
				nuevo.crearCancion("Fill " + nuevo.getNumCanciones(), 160, GeneroMusical.INDIE);
			}
			System.out.println("✓ Album lleno a 20 canciones");
			try {
				nuevo.crearCancion("Cancion 21", 160, GeneroMusical.INDIE);
			} catch (AlbumCompletoException e) {
				System.out.println("✗ Album completo (AlbumCompletoException)");
			}
			nuevo.eliminarCancion(0);
			System.out.println("✓ Cancion eliminada del album");
			try {
				nuevo.eliminarCancion(999);
			} catch (CancionNoEncontradaException e) {
				System.out.println("✗ Cancion no encontrada (CancionNoEncontradaException)");
			}
			System.out.println("✓ Duracion total album: " + nuevo.getDuracionTotal() + "s");
			System.out.println("✓ Top 5 canciones del artista: " + a1.obtenerTopCanciones(5).size());
		} catch (ArtistaNoVerificadoException e) {
			System.out.println("✗ Artista no verificado (ArtistaNoVerificadoException)");
		} catch (Exception e) {
			System.out.println("✗ Error escenario 7: " + e.getMessage());
		}

		System.out.println("\n[ESCENARIO 8: CREADORES Y PODCASTS]");
		try {
			Podcast nuevoEp = plataforma.crearPodcast("Nuevo Episodio", 1400, c1, 5, 99, CategoriaPodcast.TECNOLOGIA);
			nuevoEp.agregarInvitado("Invitado A");
			nuevoEp.agregarInvitado("Invitado B");
			System.out.println("✓ Creador publica episodio nuevo con invitados");
			EstadisticasCreador stats = c1.obtenerEstadisticas();
			System.out.println("✓ Estadisticas creador: " + stats);
			String idEliminar = c1.getEpisodios().get(0).getId();
			c1.eliminarEpisodio(idEliminar);
			System.out.println("✓ Episodio antiguo eliminado");
			try {
				c1.eliminarEpisodio("no-existe");
			} catch (Exception e) {
				System.out.println("✗ Episodio inexistente (EpisodioNoEncontradoException)");
			}
			System.out.println("✓ Promedio reproducciones creador: " + String.format("%.2f", c1.calcularPromedioReproducciones()));
		} catch (Exception e) {
			System.out.println("✗ Error escenario 8: " + e.getMessage());
		}

		System.out.println("\n[ESCENARIO 9: CONTENIDO NO DISPONIBLE]");
		Contenido cualquiera = poolCanciones.get(0);
		cualquiera.setDisponible(false);
		try {
			uP2.reproducir(cualquiera);
		} catch (Exception e) {
			if (e.getCause() instanceof ContenidoNoDisponibleException) {
				System.out.println("✗ Contenido no disponible (ContenidoNoDisponibleException)");
			} else {
				System.out.println("✗ Error reproduciendo contenido no disponible: " + e.getMessage());
			}
		}

		try {
			Cancion sinLetra = new Cancion("Sin Letra", 200, a2, null, GeneroMusical.POP, "http://audio/sinletra", false, "",
					null);
			sinLetra.obtenerLetra();
		} catch (LetraNoDisponibleException e) {
			System.out.println("✗ Letra no disponible (LetraNoDisponibleException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		try {
			Podcast sinTrans = new Podcast("Sin Transcripcion", 900, c2, 2, 77, "", CategoriaPodcast.COMEDIA, "");
			sinTrans.obtenerTranscripcion();
		} catch (TranscripcionNoDisponibleException e) {
			System.out.println("✗ Transcripcion no disponible (TranscripcionNoDisponibleException)");
		} catch (Exception e) {
			System.out.println("✗ Error inesperado: " + e.getMessage());
		}

		System.out.println("\n[ESCENARIO 10: ESTADISTICAS FINALES]");
		System.out.println(plataforma.obtenerEstadisticasGenerales());
	}
}
