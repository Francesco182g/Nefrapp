package bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Sara Corrente
 * Questa classe rappresenta il paziente
 */
public class Paziente{
	private String sesso;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	private String residenza;
	private Date dataDiNascita;
	private Boolean attivo;
	private ArrayList<String> medici;

	public Paziente() {}

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
