package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import model.AnnuncioModel;

/**
 * 
 * @author Sara Corrente
 * Implementazione concreta lightweight di un Annuncio (proxy design pattern)
 */
public class AnnuncioProxy implements Annuncio {
	private Annuncio buffer = null;
	private String idAnnuncio;
	private String medico; // CF del medico che ha pubblicato l'annuncio
	private String titolo;
	private String testo;
	private String nomeAllegato;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> pazientiView = new HashMap<String, Boolean>();
	
	public AnnuncioProxy() {}

	public AnnuncioProxy(String medico, String titolo, String testo, String nomeAllegato, ZonedDateTime data, HashMap<String, Boolean> pazientiView) {
		this.medico = medico;
		this.titolo = titolo;
		this.testo = testo;
		this.data = data;
		this.nomeAllegato = nomeAllegato;
		this.visualizzato = false;
		this.pazientiView.putAll(pazientiView);
	}

	public String getMedico() {
		return this.medico;
	}

	public void setMedico(String medico) {
		this.medico= medico;
	}

	public HashMap<String, Boolean> getPazientiView() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		return buffer.getPazientiView();
	}

	public void setPazientiView( HashMap<String, Boolean> pazientiView) {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		buffer.setPazientiView(pazientiView);
	}

	public String getTitolo() {
		return this.titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo= titolo;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getCorpoAllegato() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		return buffer.getCorpoAllegato();
	}

	public void setCorpoAllegato(String corpoAllegato) {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		buffer.setCorpoAllegato(corpoAllegato);
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}

	public String getOraFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		return data.format(format);
	}

	public ZonedDateTime getData() {
		return data;
	}

	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return data.format(format);
	}

	public void setData(ZonedDateTime data) {
		this.data = data;
	}

	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato = visualizzato;
	}

	public Boolean getVisualizzato() {
		return this.visualizzato;
	}

	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}

	public String getIdAnnuncio() {
		return this.idAnnuncio;
	}

	@Override
	public String toString() {
		return "AnnuncioProxy [idAnnuncio=" + idAnnuncio + ", medico=" + medico
				+ ", titolo=" + titolo + ", testo=" + testo + ", data=" + data + ", visualizzato=" + visualizzato
				+ ", pazientiView=" + pazientiView + "]";
	}
}