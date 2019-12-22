package bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * 
 * @author Sara Corrente
 * Questa classe rappresenta il paziente
 */
public class Paziente extends Utente{
	private String sesso;
	private String email;
	private String residenza;
	private String luogoDiNascita;
	private LocalDate dataDiNascita;
	private Boolean attivo;
	private ArrayList<String> medici;

	public Paziente() {}

	public Paziente(String sesso, String codiceFiscale, String nome, String cognome, String email, String residenza, String luogoDiNascita, LocalDate dataDiNascita, Boolean attivo, ArrayList<String> medici) {
		super(codiceFiscale, nome, cognome);
		this.sesso = sesso;
		this.email = email;
		this.residenza = residenza;
		this.luogoDiNascita=luogoDiNascita;
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
	
	public String getLuogoDiNascita() {
		return luogoDiNascita;
	}
	
	public void setLuogoDiNascita(String luogoDiNascita) {
		this.luogoDiNascita=luogoDiNascita;
	}

	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}
	
	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dataDiNascita.format(format);
	}

	public void setDataDiNascita(LocalDate dataDiNascita) {
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
		return "Paziente [sesso=" + sesso + ", email=" + email + ", residenza=" + residenza + ", luogoDiNascita="
				+ luogoDiNascita + ", dataDiNascita=" + dataDiNascita + ", attivo=" + attivo + ", medici=" + medici
				+ ", codiceFiscale=" + getCodiceFiscale() + ", nome=" + getNome() + ", cognome="
				+ getCognome() + "]";
	}
	
}
