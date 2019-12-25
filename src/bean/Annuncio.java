package bean;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Davide Benedetto Strianese
 * Questa classe rappresenta l'annuncio
 */
public class Annuncio {
	private String idAnnuncio;
	private String medico; //CF del medico che ha pubblicato l'annuncio
	private ArrayList<String> pazienti; //CF dei pazienti a cui è rivolto
	private String titolo;
	private String testo;
	private String allegato; //può essere una presentazione pp o un video, tenere traccia tramite path
	private ZonedDateTime data;
	private Boolean visualizzato;
	public Annuncio() {}
	
	public Annuncio(String medico, ArrayList<String> pazienti, String titolo, String testo, String allegato, ZonedDateTime data) {
		this.medico = medico;
		this.pazienti = pazienti;
		this.titolo = titolo;
		this.testo = testo;
		this.allegato = allegato;
		this.data = data;
		this.visualizzato=false;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	public ArrayList<String> getPazienti() {
		return pazienti;
	}

	public void setPazienti(ArrayList<String> pazienti) {
		this.pazienti = pazienti;
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

	public String getAllegato() {
		return allegato;
	}

	public void setAllegato(String allegato) {
		this.allegato = allegato;
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
	
	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}
	public String getIdMessaggio() {
		return this.idAnnuncio;
	}
	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato = visualizzato;
	}
	public Boolean getVisualizzato() {
		return this.visualizzato;
	}

	@Override
	public String toString() {
		return "Annuncio [idAnnuncio=" + idAnnuncio + ", medico=" + medico + ", pazienti=" + pazienti + ", titolo="
				+ titolo + ", testo=" + testo + ", allegato=" + allegato + ", data=" + data + ", visualizzato="
				+ visualizzato + "]";
	}
	
}