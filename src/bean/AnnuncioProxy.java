package bean;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import model.AnnuncioModel;
import model.MessaggioModel;


/**Implementazione concreta lightweight di un Messaggio (proxy design pattern)
 * @author nico
 *
 */
public class AnnuncioProxy implements Annuncio {
	private Annuncio buffer = null;
	private String idAnnuncio;
	private String medico; // CF del medico che ha pubblicato l'annuncio
	private ArrayList<String> pazienti; // CF dei pazienti a cui è rivolto
	private String titolo;
	private String testo;
	private ZonedDateTime data;
	private Boolean visualizzato;
	private HashMap<String, Boolean> pazientiView = new HashMap<String, Boolean>();
	
	public AnnuncioProxy() {}

	public AnnuncioProxy(String medico, String titolo, ZonedDateTime data, 
			HashMap<String, Boolean> pazientiView) {
		this.medico = medico;
		this.titolo = titolo;
		this.data = data;
		this.visualizzato = false;
		this.pazientiView.putAll(pazientiView);
	}

	public String getMedico() {
		return this.medico;
	}

	public void setMedico(String medico) {
		this.medico= medico;
	}

	public ArrayList<String> getCodiceFiscaleDestinatario() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnunioById(idAnnuncio);
		}
		
		return buffer.getCodiceFiscaleDestinatario();
	}

	public void setCodiceFiscaleDestinatario(ArrayList<String> codiceFiscaleDestinatario) {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		buffer.setCodiceFiscaleDestinatario(codiceFiscaleDestinatario);
	}

	public String getTitolo() {
		return this.titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo= titolo;
	}

	public String getTesto() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		return buffer.getTesto();	
	}

	public void setTesto(String testo) {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		buffer.setTesto(testo);
	}

	public String getCorpoAllegato() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		return buffer.getCorpoAllegato();
	}

	public void setCorpoAllegato(String corpoAllegato) {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		buffer.setCorpoAllegato(corpoAllegato);
	}

	public String getNomeAllegato() {
		if (buffer == null) {
			buffer = AnnuncioModel.getAnnuncioById(idAnnuncio);
		}
		
		return buffer.getNomeAllegato();	
	}

	public void setNomeAllegato(String nomeAllegato) {
		if (buffer == null) {
			buffer =AnnuncioModel.getAnnuncioById(idAnnuncio);
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
		this.idAnnuncio = idMessaggio;
	}

	public String getIdMessaggio() {
		return this.idAnnuncio;
	}

	public void setPazientiView(String CFPaziente, Boolean visualizzato) {
		pazientiView.put(CFPaziente, false);
	}

	public HashMap<String, Boolean> getPazientiView() {
		return this.pazientiView;
	}

	@Override
	public String toString() {
		return "MessaggioProxy [idMessaggio=" + idAnnuncio + ", medico=" + medico
				+ ", titolo=" + titolo + ", data=" + data + ", visualizzato=" + visualizzato
				+ ", pazientiView=" + pazientiView + "]";
	}

}
