package bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Antonio Donnarumma
 * Questa classe rappresenta il medico
 */
public class Medico{ 
	private String sesso; 
	private String residenza;
	private LocalDate dataDiNascita;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	private String luogoDiNascita;
	
	public Medico() {}

	public Medico(String sesso, String residenza, LocalDate dataDiNascita, String codiceFiscale, String nome, String cognome,String email,String luogoDiNascita) {
		this.sesso = sesso;
		this.residenza = residenza;
		this.dataDiNascita = dataDiNascita;
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.luogoDiNascita=luogoDiNascita;
	}

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
	
	public String getLuogoDiNascita() {
		return luogoDiNascita;
	}
	
	public void setLuogoDiNascita(String luogoDiNascita) {
		this.luogoDiNascita=luogoDiNascita;
	}

	@Override
	public String toString() {
		return "Medico [sesso=" + sesso + ", residenza=" + residenza + ", dataDiNascita=" + dataDiNascita
				+ ", codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
				+ ", luogoDiNascita=" + luogoDiNascita + "]";
	}
}