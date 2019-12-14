package bean;

import java.util.Date;

public class Medico{ 
	
	//variabili
	private String sesso; 
	private String residenza;
	private Date dataDiNascita;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	
	//costruttori
	/**
	 *  Medico è un oggetto che rappresenta la figura del medico
	 */
	public Medico() {
	}

	
	
	/**
	 *  Medico è un oggetto che rappresenta la figura del medico
	 * @param codiceFiscale rappresenta il codice fiscale del medico
	 * @param nome rappresenta il nome del medico
	 * @param cognome rappresenta il cognome del medico
	 * @param sesso rappresenta il sesso del medico
	 * @param email rappresenta l'email del medico
	 * @param residenza rappresenta la residenza del medico
	 * @param dataDiNascita rappresenta la data di nascita del medico
	 */
	public Medico(String sesso, String residenza, Date dataDiNascita, String codiceFiscale, String nome, String cognome,String email) {
		this.sesso = sesso;
		this.residenza = residenza;
		this.dataDiNascita = dataDiNascita;
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
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



	@Override
	public String toString() {
		return "Medico [sesso=" + sesso + ", residenza=" + residenza + ", dataDiNascita=" + dataDiNascita
				+ ", codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
				+ "]";
	}	
	
}