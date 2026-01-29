package modelo.plataforma;

import java.util.UUID;

import enums.TipoAnuncio;

public class Anuncio {
	private final String id;
	private final String empresa;
	private final int duracionSegundos;
	private final String audioURL;
	private final TipoAnuncio tipo;
	private int impresiones;
	private double presupuesto;
	private boolean activo;

	public Anuncio(String empresa, String audioURL, TipoAnuncio tipo, double presupuesto) {
		this.id = UUID.randomUUID().toString();
		this.empresa = (empresa == null || empresa.isBlank()) ? "(sin empresa)" : empresa;
		this.audioURL = (audioURL == null) ? "" : audioURL;
		this.tipo = (tipo == null) ? TipoAnuncio.AUDIO : tipo;
		this.duracionSegundos = this.tipo.getDuracionSegundos();
		this.impresiones = 0;
		this.presupuesto = Math.max(0.0, presupuesto);
		this.activo = true;
	}

	public String getId() {
		return id;
	}

	public String getEmpresa() {
		return empresa;
	}

	public int getImpresiones() {
		return impresiones;
	}

	public boolean isActivo() {
		return activo;
	}

	public void reproducir() {
		if (!activo) {
			return;
		}
		System.out.println("⚠ [ANUNCIO] " + empresa + " (" + tipo + ") - " + duracionSegundos + "s");
		registrarImpresion();
	}

	public void registrarImpresion() {
		impresiones++;
		presupuesto = Math.max(0.0, presupuesto - calcularCostoPorImpresion());
		if (presupuesto <= 0.0) {
			desactivar();
		}
	}

	public double calcularCostoPorImpresion() {
		return tipo.getCostoPorImpresion();
	}

	public void desactivar() {
		activo = false;
	}
}
