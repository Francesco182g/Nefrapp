package bean;

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
	private Medico medico; //Riferimento al medico che ha pubblicato l'annuncio
	private List<Paziente> pazienti; //Riferimento ai pazienti a cui � rivolto
	private String titolo;
	private String testo;
	private String allegato; //pu� essere una presentazione pp o un video, tenere traccia tramite path
	private ZonedDateTime data;
	
	public Annuncio() {}
	
	public Annuncio(Medico medico, List<Paziente> pazienti, String titolo, String testo, String allegato, ZonedDateTime data) {
		this.medico = medico;
		this.pazienti = pazienti;
		this.titolo = titolo;
		this.testo = testo;
		this.allegato = allegato;
		this.data = data;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public List<Paziente> getPazienti() {
		return pazienti;
	}

	public void setPazienti(List<Paziente> pazienti) {
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
	
	@Override
	public String toString() {
		String infoPazienti = "";
		for(Paziente p: pazienti)
			infoPazienti = infoPazienti + p.getNome() + p.getCognome() + "\n";
		
		return "Annuncio [Medico=" + medico.getNome() + medico.getCognome() + ", pazienti=" + infoPazienti + ", titolo=" + titolo
				+ ", testo=" + testo + ", data=" + data + "]";
	}
}