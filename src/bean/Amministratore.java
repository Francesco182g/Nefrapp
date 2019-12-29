package bean;

/**
 * 
 * @author Eugenio Corbisiero
 * Questa classe rappresenta l'amministratore
 */
public class Amministratore extends Utente{
	
	
	public Amministratore() {
		super();
	}
	
	public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
		super(codiceFiscale, nome, cognome,email);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}