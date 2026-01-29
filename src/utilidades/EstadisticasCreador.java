package utilidades;

import modelo.artistas.Creador;
import modelo.contenido.Podcast;

public class EstadisticasCreador {
	private final String nombreCanal;
	private final int totalEpisodios;
	private final int suscriptores;
	private final double promedioReproducciones;

	public EstadisticasCreador(Creador creador) {
		this.nombreCanal = creador.getNombreCanal();
		this.totalEpisodios = creador.getEpisodios().size();
		this.suscriptores = creador.getSuscriptores();
		long total = 0;
		for (Podcast p : creador.getEpisodios()) {
			total += p.getReproducciones();
		}
		this.promedioReproducciones = totalEpisodios == 0 ? 0.0 : (total / (double) totalEpisodios);
	}

	@Override
	public String toString() {
		return "EstadisticasCreador{" + "canal='" + nombreCanal + '\'' + ", episodios=" + totalEpisodios
				+ ", suscriptores=" + suscriptores + ", promedioReproducciones=" + String.format("%.2f", promedioReproducciones)
				+ '}';
	}
}
