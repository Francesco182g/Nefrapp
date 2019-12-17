package bean;

import java.time.LocalDate;

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

	public PianoTerapeutico() {}

	public PianoTerapeutico(String codiceFiscalePaziente, String diagnosi, String farmaco, LocalDate dataFineTerapia) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
		this.diagnosi = diagnosi;
		this.farmaco = farmaco;
		this.dataFineTerapia = dataFineTerapia;
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

	public void setDataFineTerapia(LocalDate dataFineTerapia) {
		this.dataFineTerapia = dataFineTerapia;
	}
	
	@Override
	public String toString() {
		return "PianoTerapeutico [codiceFiscalePaziente=" + codiceFiscalePaziente + ", diagnosi=" + diagnosi + ", farmaco=" + farmaco + ", dataFineTerapia=" + dataFineTerapia + "]";
	}
}
