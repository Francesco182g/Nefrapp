package bean;
/**
 * 
 * @author Eugenio
 *
 */
public class Utente {
	//variabili
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	
	//costruttori
	/**
	 * Utente è un oggetto che rappresenta la generalizzazione di Amministratore, Medico e Paziente
	 */
	public Utente() {
	}
	
	/**
	 * Utente è un oggetto che rappresenta la generalizzazione di Amministratore, Medico e Paziente
	 * @param codiceFiscale rappresenta il codice fiscale dell'utente
	 * @param nome rappresenta il nome dell'utente
	 * @param cognome rappresenta il cognome dell'utente 
	 * @param email rappresenta la mail dell'utente
	 */
	public Utente(String codiceFiscale, String nome, String cognome, String email) {
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	//metodi	
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
		return getClass().getName()+" [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
				+ "]";
	}
	
	
}
