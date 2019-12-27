package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import model.MessaggioModel;


/**Implementazione concreta lightweight di un Messaggio (proxy design pattern)
 * @author nico
 *
 */
public class MessaggioProxy implements Messaggio {
	private Messaggio buffer = null;
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
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		return buffer.getCodiceFiscaleDestinatario();
	}

	public void setCodiceFiscaleDestinatario(ArrayList<String> codiceFiscaleDestinatario) {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		buffer.setCodiceFiscaleDestinatario(codiceFiscaleDestinatario);
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTesto() {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		return buffer.getTesto();	
	}

	public void setTesto(String testo) {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		buffer.setTesto(testo);
	}

	public String getCorpoAllegato() {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		return buffer.getCorpoAllegato();
	}

	public void setCorpoAllegato(String corpoAllegato) {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		buffer.setCorpoAllegato(corpoAllegato);
	}

	public String getNomeAllegato() {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		return buffer.getNomeAllegato();	
	}

	public void setNomeAllegato(String nomeAllegato) {
		if (buffer == null) {
			buffer = MessaggioModel.getMessaggioById(idMessaggio);
		}
		
		buffer.setNomeAllegato(nomeAllegato);	
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
