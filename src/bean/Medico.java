package bean;

import java.util.Date;

public class Medico extends Utente{ 
	
	//variabili
	private String sesso; 
	private String residenza;
	private Date dataDiNascita;
	
	//costruttori
	/**
	 *  Medico è un oggetto che rappresenta la figura del medico
	 */
	public Medico() {
		super();
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
	public Medico(String codiceFiscale, String nome, String cognome, String sesso, String email, String residenza, Date dataDiNascita) {
		super(codiceFiscale, nome, cognome, email);
		this.sesso = sesso;
		this.residenza = residenza;
		this.dataDiNascita = dataDiNascita;
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

	@Override
	public String toString() {
		return super.toString()+"[sesso=" + sesso + ", residenza=" + residenza + ", dataDiNascita=" + dataDiNascita + "]";
	}
	
	
}