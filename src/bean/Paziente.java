package bean;

import java.util.ArrayList;
import java.util.Date;

public class Paziente extends Utente{
	//variabili
	private String sesso;
	private String residenza;
	private Date dataDiNascita;
	private Boolean attivo;
	private ArrayList<String> medici;
	
	//costruttori
	/**
	 * Paziente è un oggetto che rappresenta la figura dell'paziente
	 */
	public Paziente() {
		super();
	}
	
	/**
	 * Paziente è un oggetto che rappresenta la figura del paziente
	 * @param codiceFiscale rappresenta il codice fiscale del paziente
	 * @param nome rappresenta il nome del paziente
	 * @param cognome rappresenta il cognome del paziente
	 * @param sesso rappresenta il sesso del paziente
	 * @param email rappresenta l'email del paziente
	 * @param residenza rappresenta la residenza del paziente
	 * @param dataDiNascita rappresenta la data di nascita del paziente
	 * @param attivo rappresenta se l'account del paziente � stato disabilitato o meno
	 * @param medici rappresenta i medici che seguono paziente
	 */
	public Paziente(String codiceFiscale, String nome, String cognome, String sesso, String email, String residenza,Date dataDiNascita, Boolean attivo, ArrayList<String> medici) {
		super(codiceFiscale, nome, cognome, email);
		this.sesso = sesso;
		this.residenza = residenza;
		this.dataDiNascita = dataDiNascita;
		this.attivo = attivo;
		this.medici = medici;
	}
	
	//metodi
	
	public String getSesso() {
		return sesso;
	}
	
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	
	
	public String getResidenza() {
		return residenza;
	}
	
	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}
	
	public Date getDataDiNascita() {
		return dataDiNascita;
	}
	
	public void setDataDiNascita(Date dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public Boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}
	
	public ArrayList<String> getMedici() {
		return medici;
	}

	public void setMedici(ArrayList<String> medici) {
		this.medici = medici;
	}

	@Override
	public String toString() {
		return super.toString()+"[sesso=" + sesso + ", residenza=" + residenza + ", dataDiNascita=" + dataDiNascita
				+ ", attivo=" + attivo + ", medici=" + medici + "]";
	}
	
	
}
