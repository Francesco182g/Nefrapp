package bean;

import java.util.Date;

public class Paziente {
	
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String sesso;
	private String email;
	private String residenza;
	private Date dataDiNascita;
	private Boolean attivo;
	
	public Paziente() {
		
	}
	
	public Paziente(String codiceFiscale, String nome, String cognome, String sesso, String email, String residenza,
			String password, Date dataDiNascita, Boolean attivo) {
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.sesso = sesso;
		this.email = email;
		this.residenza = residenza;
		this.dataDiNascita = dataDiNascita;
		this.attivo = attivo;
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
}
