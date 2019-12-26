/**
 * 
 */
package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Luca Esposito Questa classe rappresenta il messaggio
 */
public class Messaggio {

	private String idMessaggio;
	private String codiceFiscaleMittente;
	private ArrayList<String> codiceFiscaleDestinatario;
	private String oggetto;
	private String testo;
	private String corpoAllegato;
	private String nomeAllegato;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>(); // coppia di CF dei destinatari e
																						// il campo visualizzato.

	public Messaggio() {
	}

	public Messaggio(String codiceFiscaleMittente, ArrayList<String> codiceFiscaleDestinatario, String oggetto,
			String testo, String corpoAllegato, String nomeAllegato, ZonedDateTime data,
			HashMap<String, Boolean> destinatariView) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
		this.codiceFiscaleDestinatario = new ArrayList<String>(codiceFiscaleDestinatario);
		this.oggetto = oggetto;
		this.testo = testo;
		this.corpoAllegato = corpoAllegato;
		this.nomeAllegato = nomeAllegato;
		this.data = data;
		this.visualizzato = false;
		this.destinatariView.putAll(destinatariView);

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
		this.codiceFiscaleDestinatario = new ArrayList<String>(codiceFiscaleDestinatario);
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

	public String getCorpoAllegato() {
		return corpoAllegato;
	}

	public void setCorpoAllegato(String corpoAllegato) {
		this.corpoAllegato = corpoAllegato;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
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
		this.visualizzato = visualizzato;
	}

	public Boolean getVisualizzato() {
		return this.visualizzato;
	}

	public void setIdMessaggio(String idMessaggio) {
		this.idMessaggio = idMessaggio;
	}

	public String getIdMessaggio() {
		return this.idMessaggio;
	}
	public void setDestinatariView(String CFPaziente, Boolean visualizzato) {
		destinatariView.put(CFPaziente, false);
	}
	
	public HashMap<String,Boolean> getDestinatariView() {
		return this.destinatariView;
	}
	@Override
	public String toString() {
		return "Messaggio [mittente=" + codiceFiscaleMittente + ", destinatario=" + codiceFiscaleDestinatario
				+ ", oggetto=" + oggetto + ", testo=" + testo + ", data=" + data + ", visualizzato=" + visualizzato
				+ "]";
	}
}