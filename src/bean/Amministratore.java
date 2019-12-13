package bean;

/**
 * 
 * @author Eugenio
 *
 */
public class Amministratore extends Utente{
	//variabili

	//costruttore
	
	/**
	 * 
	 * Amministratore è un oggetto che rappresenta la figura dell'amministratore
	 */
	public Amministratore() {
		super();
	}
	
	
	/**
	 * Amministratore è un oggetto che rappresenta la figura dell'amministratore 
	 * @param codiceFiscale indica il codice fiscale dell'amministratore 
	 * @param nome indica il nome dell'amministratore 
	 * @param cognome indica il cognome dell'amministratore 
	 * @param email indica la mail dell'amministratore
	 */
	public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
		super(codiceFiscale,nome,cognome,email);
	}

	
	//metodi
		
	@Override
	public String toString() {
		return super.toString();
	}

}
