package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import model.MessaggioModel;

public class MessaggioProxy implements Messaggio {
	private String idMessaggio;
	private String codiceFiscaleMittente;
	private String oggetto;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
	
	public MessaggioProxy() {}

	public MessaggioProxy(String codiceFiscaleMittente, String oggetto, ZonedDateTime data, 
			HashMap<String, Boolean> destinatariView) {
		this.codiceFiscaleMittente = codiceFiscaleMittente;
		this.oggetto = oggetto;
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
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		return m.getCodiceFiscaleDestinatario();
	}

	public void setCodiceFiscaleDestinatario(ArrayList<String> codiceFiscaleDestinatario) {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		m.setCodiceFiscaleDestinatario(codiceFiscaleDestinatario);
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTesto() {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		return m.getTesto();		
	}

	public void setTesto(String testo) {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		m.setTesto(testo);		
	}

	public String getCorpoAllegato() {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		return m.getCorpoAllegato();	
	}

	public void setCorpoAllegato(String corpoAllegato) {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		m.setCorpoAllegato(corpoAllegato);	
	}

	public String getNomeAllegato() {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		return m.getNomeAllegato();		
	}

	public void setNomeAllegato(String nomeAllegato) {
		Messaggio m = MessaggioModel.getMessaggioById(idMessaggio);
		m.setNomeAllegato(nomeAllegato);	
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

	public HashMap<String, Boolean> getDestinatariView() {
		return this.destinatariView;
	}

	@Override
	public String toString() {
		return "MessaggioProxy [idMessaggio=" + idMessaggio + ", codiceFiscaleMittente=" + codiceFiscaleMittente
				+ ", oggetto=" + oggetto + ", data=" + data + ", visualizzato=" + visualizzato
				+ ", destinatariView=" + destinatariView + "]";
	}

}
