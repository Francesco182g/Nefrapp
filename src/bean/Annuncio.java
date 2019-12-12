package bean;

import java.util.List;
import java.util.Date;

/**
 * @author Davide Benedetto Strianese
 *
 */
public class Annuncio {
	
	private Medico medico; //Riferimento al medico che ha pubblicato l'annuncio
	private List<Paziente> pazienti; //Riferimento ai pazienti a cui è rivolto
	private String titolo;
	private String testo;
	private String allegato; //può essere una presentazione pp o un video, tenere traccia tramite path
	private String ora;
	private Date data;
	
	public Annuncio() {
		
	}
	
	/**
	 * Annuncio è un oggetto che rappresenta l'annuncio 
	 * @param medico indica il medico che ha pubblicato l'annuncio 
	 * @param pazienti indica i pazienti a cui è rivolto l'annuncio
	 * @param titolo indica il titolo dell'annuncio
	 * @param testo indica il contenuto dell'annuncio
	 * @param allegato indica l'allegato all'annuncio
	 * @param ora indica l'ora in cui è stato inviato l'annuncio
	 * @param data indica il giorno in cui è stato inviato l'annuncio
	 */
	public Annuncio(Medico medico, List<Paziente> pazienti, String titolo, String testo, String allegato, String ora, Date data) {
		this.medico = medico;
		this.pazienti = pazienti;
		this.titolo = titolo;
		this.testo = testo;
		this.allegato = allegato;
		this.ora = ora;
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

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
