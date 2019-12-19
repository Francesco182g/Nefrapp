/**
 * 
 */
package bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author Luca Esposito
 * Questa classe rappresenta il messaggio
 */
public class Messaggio {
	
	private String codiceFiscaleMittente;
	private ArrayList<String> codiceFiscaleDestinatario;
	private String oggetto;
	private String testo;
	private String allegato;
	private LocalTime ora;
	private LocalDate data;
	
	public Messaggio() {}

	public Messaggio(String codiceFiscaleMittente, ArrayList<String> codiceFiscaleDestinatario, String oggetto, String testo, String allegato, LocalTime ora, LocalDate data) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
		this.codiceFiscaleDestinatario.addAll(codiceFiscaleDestinatario);
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

	public ArrayList<String> getCodiceFiscaleDestinatario() {
		return codiceFiscaleDestinatario;
	}

	public void setCodiceFiscaleDestinatario(ArrayList<String> codiceFiscaleDestinatario) {
		this.codiceFiscaleDestinatario.addAll(codiceFiscaleDestinatario);
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

	public LocalTime getOra() {
		return ora;
	}

	public void setOra(LocalTime ora) {
		this.ora = ora;
	}

	public LocalDate getData() {
		return data;
	}
	
	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return data.format(format);
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