package enums;

public enum AlgoritmoRecomendacion {
	COLABORATIVO("Basado en usuarios similares"),
	CONTENIDO("Basado en caracteristicas del contenido"),
	HIBRIDO("Combinacion de ambos");

	private final String descripcion;

	AlgoritmoRecomendacion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
