package bean;

/**
 * 
 * @author Eugenio
 *
 */
public class Amministratore {
	//variabili
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;


	//costruttore
	
	/**
	 * 
	 * Amministratore è un oggetto che rappresenta la figura dell'amministratore
	 */
	public Amministratore() {
	
	}
	
	
	/**
	 * Amministratore è un oggetto che rappresenta la figura dell'amministratore 
	 * @param codiceFiscale indica il codice fiscale dell'amministratore 
	 * @param nome indica il nome dell'amministratore 
	 * @param cognome indica il cognome dell'amministratore 
	 * @param email indica la mail dell'amministratore
	 */
	public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	
	//metodi
	/**
	 * Il metodo getCodiceFiscale permette di accedere al campo codice fiscale dell'Amministratore
	 * @return il valore nel campo codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	/**
	 * Il metodo getNome permette di accedere al campo nome dell'Amministratore
	 * @return il valore nel campo nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Il metodo setNome permette di accedere al campo nome dell'Amministratore e di cambiargli il valore
	 * @param nome indica il nome dell'amministratore
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * Il metodo getCognome permette di accedere al campo cognome dell'Amministratore
	 * @return il valore nel campo cognome
	 */
	public String getCognome() {
		return cognome;
	}
	
	
	/**
	 * Il metodo setCognome permette di accedere al campo cognome dell'Amministratore e di cambiargli il valore
	 * @param cognome indica il cognome dell'amministratore
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Il metodo getEmail permette di accedere al campo email dell'Amministratore
	 * @return il valore nel campo email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Il metodo setEmail permette di accedere al campo email dell'Amministratore e di cambiargli il valore
	 * @param email indica la mail dell'amministratore
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Il metodo setCodiceFiscale permette di accedere al campo codiceFiscale dell'Amministratore e di cambiargli il valore
	 * @param codiceFiscale idica il codice fiscale dell'amministratore
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	@Override
	public String toString() {
		return "Amministratore [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", email="
				+ email + "]";
	}

}
