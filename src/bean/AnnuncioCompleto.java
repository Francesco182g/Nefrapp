package bean;

import java.util.HashMap;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Davide Benedetto Strianese 
 * Questa classe rappresenta l'annuncio
 */
public class AnnuncioCompleto implements Annuncio{
	private String idAnnuncio;
	private String medico; // CF del medico che ha pubblicato l'annuncio
	private String titolo;
	private String testo;
	private String corpoAllegato; // può essere una presentazione pp o un video, tenere traccia tramite path
	private String nomeAllegato;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> pazientiView = new HashMap<String, Boolean>(); // coppia di CF dei destinatari e il campo visualizzato.

	public AnnuncioCompleto() {}

	public AnnuncioCompleto(String medico, String titolo, String testo, String corpoAllegato, String nomeAllegato, ZonedDateTime data, HashMap<String, Boolean> pazientiView) {
		this.medico = medico;
		this.titolo = titolo;
		this.testo = testo;
		this.setCorpoAllegato(corpoAllegato);
		this.setNomeAllegato(nomeAllegato);
		this.data = data;
		this.pazientiView.putAll(pazientiView);
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getCorpoAllegato() {
		return corpoAllegato;
	}

	public void setCorpoAllegato(String corpoAllegato) {
		this.corpoAllegato = corpoAllegato;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}

	public ZonedDateTime getData() {
		return data;
	}

	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return data.format(format);
	}
	
	public String getOraFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		return data.format(format);
	}

	public void setData(ZonedDateTime data) {
		this.data = data;
	}
	
	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}

	public String getIdAnnuncio() {
		return this.idAnnuncio;
	}
	
	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato = visualizzato;
	}

	public Boolean getVisualizzato() {
		return this.visualizzato;
	}

	public void setPazientiView(HashMap<String, Boolean> pazientiView) {
		pazientiView.putAll(pazientiView);
	}

	public HashMap<String, Boolean> getPazientiView() {
		return this.pazientiView;
	}

	@Override
	public String toString() {
		return "Annuncio [idAnnuncio=" + idAnnuncio + ", medico=" + medico + ", pazienti=" + ", titolo="
				+ titolo + ", testo=" + testo + ", allegato=" + nomeAllegato + ", data=" + data + ", visualizzato=" + "]";
	}
}