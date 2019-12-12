/**
 * 
 */
package bean;

import java.util.Date;

/**
 * @author Davide Benedetto Strianese
 *
 */
public class Messaggio {
	
	private String codiceFiscaleMittente;
	private String codiceFiscaleDestinatario;
	private String oggetto;
	private String testo;
	private String allegato;
	private String ora;
	private Date data;
	
	public Messaggio() {
		
	}

	/**
	 * Messaggio è un oggetto che rappresenta un messaggio
	 * @param codiceFiscaleMittente indica il codice fiscale del mittente che può essere un medico o un paziente
	 * @param codiceFiscaleDestinatario indica il codice fiscale del destinatario che può essere un medico o un paziente
	 * @param oggetto indica l'oggetto del messaggio
	 * @param testo indica il testo del messaggio
	 * @param allegato indica un allegato nel messaggio
	 * @param ora indica l'ora in cui è stato spedito il messaggio
	 * @param data indica la data in cui è stato spedito il messaggio
	 */
	public Messaggio(String codiceFiscaleMittente, String codiceFiscaleDestinatario, String oggetto, String testo, String allegato, String ora, Date data) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
		this.codiceFiscaleDestinatario = codiceFiscaleDestinatario;
		this.oggetto = oggetto;
		this.testo = testo;
		this.allegato = allegato;
		this.ora = ora;
		this.data = data;
	}

	public String getCodiceFiscaleMittente() {
		return codiceFiscaleMittente;
	}

	public void setCodiceFiscaleMittente(String codiceFiscaleMittente) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
	}

	public String getCodiceFiscaleDestinatario() {
		return codiceFiscaleDestinatario;
	}

	public void setCodiceFiscaleDestinatario(String codiceFiscaleDestinatario) {
		this.codiceFiscaleDestinatario = codiceFiscaleDestinatario;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getAllegato() {
		return allegato;
	}

	public void setAllegato(String allegato) {
		this.allegato = allegato;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}