package bean;

import java.util.Date;

/**
 * @author matte
 *
 */
public class PianoTerapeutico {
	//variabili
	private String codiceFiscalePaziente;
	private String diagnosi;
	private String farmaco;
	private Date dataFineTerapia;

	//costruttori
	public PianoTerapeutico() {

	}

	public PianoTerapeutico(String codiceFiscalePaziente, String diagnosi, String farmaco, Date dataFineTerapia) {
		super();
		this.codiceFiscalePaziente = codiceFiscalePaziente;
		this.diagnosi = diagnosi;
		this.farmaco = farmaco;
		this.dataFineTerapia = dataFineTerapia;
	}

	//metodi
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

	public Date getDataFineTerapia() {
		return dataFineTerapia;
	}

	public void setDataFineTerapia(Date dataFineTerapia) {
		this.dataFineTerapia = dataFineTerapia;
	}

}
