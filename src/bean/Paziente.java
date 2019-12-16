package bean;

import java.util.ArrayList;
import java.util.Date;

public class Paziente{
	//variabili
	private String sesso;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	private String residenza;
	private Date dataDiNascita;
	private Boolean attivo;
	private ArrayList<String> medici;
	
	//costruttori
	/**
	 * Paziente è un oggetto che rappresenta la figura dell'paziente
	 */
	public Paziente() {
	
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
	public Paziente(String sesso, String codiceFiscale, String nome, String cognome, String email, String residenza,Date dataDiNascita, Boolean attivo, ArrayList<String> medici) {
		this.sesso = sesso;
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
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

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getAttivo() {
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

	public void addMedico(String medicoCodiceFiscale) {
		this.medici.add(medicoCodiceFiscale);
	}
	
	@Override
	public String toString() {
		return "Paziente [sesso=" + sesso + ", codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome="
				+ cognome + ", email=" + email + ", residenza=" + residenza + ", dataDiNascita=" + dataDiNascita
				+ ", attivo=" + attivo + ", medici=" + medici + "]";
	}
	
}
