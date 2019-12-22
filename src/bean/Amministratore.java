package bean;

/**
 * 
 * @author Eugenio Corbisiero
 * Questa classe rappresenta l'amministratore
 */
public class Amministratore extends Utente{
	private String email;
	
	public Amministratore() {
		super();
	}
	
	public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
		super(codiceFiscale, nome, cognome);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return super.toString() + ", email="
				+ email + "]";
	}
}