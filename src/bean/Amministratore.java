package bean;

/**
 * 
 * @author Eugenio
 * Questa classe rappresenta l'amministratore
 */
public class Amministratore{
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	
	public Amministratore() { }
	
	public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
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
		return "Amministratore [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", email="
				+ email + "]";
	}
}