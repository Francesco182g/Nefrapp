/**
 * 
 */
package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author Luca Esposito
 * Questa classe rappresenta il messaggio
 */
public class Messaggio {
	
	private String idMessaggio;
	private String codiceFiscaleMittente;
	private ArrayList<String> codiceFiscaleDestinatario;
	private String oggetto;
	private String testo;
	private String allegato;
	private ZonedDateTime data;
	private Boolean visualizzato;
	
	public Messaggio() {}

	public Messaggio(String codiceFiscaleMittente, ArrayList<String> codiceFiscaleDestinatario, String oggetto, String testo, String allegato, ZonedDateTime data) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
		this.codiceFiscaleDestinatario = new ArrayList<String> (codiceFiscaleDestinatario);
		this.oggetto = oggetto;
		this.testo = testo;
		this.allegato = allegato;
		this.data = data;
		this.visualizzato=false;
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
		this.codiceFiscaleDestinatario = new ArrayList<String> (codiceFiscaleDestinatario);
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
	
	public String getOraFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		return data.format(format);
	}

	public ZonedDateTime getData() {
		return data;
	}
	
	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return data.format(format);
	}

	public void setData(ZonedDateTime data) {
		this.data = data;
	}
	
	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato=visualizzato;
	}
	
	public Boolean getVisualizzato() {
		return this.visualizzato;
	}
	
	public void setIdMessaggio(String idMessaggio) {
		this.idMessaggio=idMessaggio;
	}
	public String getIdMessaggio() {
		return this.idMessaggio;
	}
	
	@Override
	public String toString() {
		return "Messaggio [mittente=" + codiceFiscaleMittente + ", destinatario=" + codiceFiscaleDestinatario + ", oggetto=" + oggetto
				+ ", testo=" + testo + ", data=" + data + ", visualizzato="+ visualizzato+"]";
	}	
}