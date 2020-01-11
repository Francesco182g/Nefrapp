package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * 
 * @author NefrappTeam
 * Implementazione concreta completa di un Messaggio
 */
public class MessaggioCompleto implements Messaggio {

	private String idMessaggio;
	private String codiceFiscaleMittente;
	private String oggetto;
	private String testo;
	private String corpoAllegato;
	private String nomeAllegato;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>(); // coppia di CF dei destinatari e il campo visualizzato.

	public MessaggioCompleto() {}

	public MessaggioCompleto(String codiceFiscaleMittente, String oggetto, String testo, String corpoAllegato, String nomeAllegato, ZonedDateTime data, HashMap<String, Boolean> destinatariView) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
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
	
	public void setDestinatariView(HashMap<String , Boolean > destinatariView) {
		this.destinatariView.putAll(destinatariView);
	}
	
	public HashMap<String,Boolean> getDestinatariView() {
		return this.destinatariView;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formatOra = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return "bean.MessaggioCompleto [idMessaggio=" + idMessaggio 
		+ ", codiceFiscaleMittente=" + codiceFiscaleMittente + ", oggetto="
		+ oggetto + ", testo=" + testo + ", corpo allegato="
		+ corpoAllegato + ", nome allegato=" + nomeAllegato
		+ ", data=" + data + ", data formattata=" + data.format(formatData)
		+ ", ora formattata=" + data.format(formatOra) + ", visualizzato="
		+ visualizzato + ", destinatari=" + destinatariView.toString() + "]";
	}
}