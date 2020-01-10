package bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Matteo Falco
 * Questa classe rappresenta il piano terapeutico
 */
public class PianoTerapeutico {
	private String codiceFiscalePaziente;
	private String diagnosi;
	private String farmaco;
	private LocalDate dataFineTerapia;
	private Boolean visualizzato;

	public PianoTerapeutico() {}

	public PianoTerapeutico(String codiceFiscalePaziente, String diagnosi, String farmaco, LocalDate dataFineTerapia) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
		this.diagnosi = diagnosi;
		this.farmaco = farmaco;
		this.dataFineTerapia = dataFineTerapia;
		this.visualizzato=false;
	}

	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}

	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}

	public String getDiagnosi() {
		return diagnosi;
	}

	public void setDiagnosi(String diagnosi) {
		this.diagnosi = diagnosi;
	}

	public String getFarmaco() {
		return farmaco;
	}

	public void setFarmaco(String farmaco) {
		this.farmaco = farmaco;
	}

	public LocalDate getDataFineTerapia() {
		return dataFineTerapia;
	}
	
	public String getDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dataFineTerapia.format(format);
	}

	public void setDataFineTerapia(LocalDate dataFineTerapia) {
		this.dataFineTerapia = dataFineTerapia;
	}
	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato = visualizzato;
	}
	public Boolean getVisualizzato() {
		return this.visualizzato;
	}
	@Override
	public String toString() {
		return "PianoTerapeutico [codiceFiscalePaziente=" + codiceFiscalePaziente + ", diagnosi=" + diagnosi + ", farmaco=" + farmaco + ", dataFineTerapia=" + dataFineTerapia + ", visualizzato=" + visualizzato+"]";
	}
}