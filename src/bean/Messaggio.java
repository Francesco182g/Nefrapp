/**
 * 
 */
package bean;

import java.time.LocalDate;

/**
 * @author Luca Esposito
 * Questa classe rappresenta il messaggio
 */
public class Messaggio {
	
	private String codiceFiscaleMittente;
	private String codiceFiscaleDestinatario;
	private String oggetto;
	private String testo;
	private String allegato;
	private String ora;
	private LocalDate data;
	
	public Messaggio() {}

	public Messaggio(String codiceFiscaleMittente, String codiceFiscaleDestinatario, String oggetto, String testo, String allegato, String ora, LocalDate data) {
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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Messaggio [mittente=" + codiceFiscaleMittente + ", destinatario=" + codiceFiscaleDestinatario + ", oggetto=" + oggetto
				+ ", testo=" + testo + ", ora=" + ora + ", data=" + data + "]";
	}	
}