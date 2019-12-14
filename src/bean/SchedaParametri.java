package bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Silvio 
 *
 */

public class SchedaParametri {
	
	private String pazienteCodiceFiscale;
	private BigDecimal peso;
	private int paMin;
	private int paMax;
	private int scaricoIniziale;
	private int UF;
	private int tempoSosta;
	private int carico; 
	private int scarico;
	private LocalDate data;	
	
	public SchedaParametri() {
		
	}
	
	/**
	 * SchedaParametri � un oggetto che rappresenta la scheda dei parametri inseriti dal paziente 
	 * @param codiceFiscalePaziente indica il paziente cui appartiene la scheda dei parametri 
	 * @param paMin indica la pressione arteriosa minima
	 * @param paMax indica la pressione arteriosa massima
	 * @param scaricoIniziale indica il liquido scaricato inizialmente 
	 * @param UF indica il liquido ultrafiltrato
	 * @param tempoSosta indica il tempo di sosta 
	 * @param carico indica il liquido caricato
	 * @param scarico indica il liquido scaricato
	 * @param ora indica l'ora di inserimento dei parametri
	 * @param data indica la data di inserimento dei parametri
	 */	
	public SchedaParametri(String pazienteCodiceFiscale, BigDecimal peso, int paMin, int paMax, int scaricoIniziale, int UF, int tempoSosta, int carico, int scarico, LocalDate data) {
		this.pazienteCodiceFiscale=pazienteCodiceFiscale;
		this.peso=peso;
		this.paMin=paMin;
		this.paMax=paMax;
		this.scaricoIniziale=scaricoIniziale;
		this.UF=UF;
		this.tempoSosta=tempoSosta;
		this.carico=carico;
		this.scarico=scarico;
		this.data=data;
	}

	public String getPazienteCodiceFiscale() {
		return pazienteCodiceFiscale;
	}

	public void setPazienteCodiceFiscale(String pazienteCodiceFiscale) {
		this.pazienteCodiceFiscale= pazienteCodiceFiscale;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public int getPaMin() {
		return paMin;
	}

	public void setPaMin(int paMin) {
		this.paMin = paMin;
	}

	public int getPaMax() {
		return paMax;
	}

	public void setPaMax(int paMax) {
		this.paMax = paMax;
	}

	public int getScaricoIniziale() {
		return scaricoIniziale;
	}

	public void setScaricoIniziale(int scaricoIniziale) {
		this.scaricoIniziale = scaricoIniziale;
	}

	public int getUF() {
		return UF;
	}

	public void setUF(int uF) {
		UF = uF;
	}

	public int getTempoSosta() {
		return tempoSosta;
	}

	public void setTempoSosta(int tempoSosta) {
		this.tempoSosta = tempoSosta;
	}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
	}

	public int getScarico() {
		return scarico;
	}

	public void setScarico(int scarico) {
		this.scarico = scarico;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + UF;
		result = prime * result + carico;
		result = prime * result + ((pazienteCodiceFiscale== null) ? 0 : pazienteCodiceFiscale.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + paMax;
		result = prime * result + paMin;
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		result = prime * result + scarico;
		result = prime * result + scaricoIniziale;
		result = prime * result + tempoSosta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedaParametri other = (SchedaParametri) obj;
		if (UF != other.UF)
			return false;
		if (carico != other.carico)
			return false;
		if (pazienteCodiceFiscale== null) {
			if (other.pazienteCodiceFiscale!= null)
				return false;
		} else if (!pazienteCodiceFiscale.equals(other.pazienteCodiceFiscale))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (paMax != other.paMax)
			return false;
		if (paMin != other.paMin)
			return false;
		if (peso == null) {
			if (other.peso != null)
				return false;
		} else if (!peso.equals(other.peso))
			return false;
		if (scarico != other.scarico)
			return false;
		if (scaricoIniziale != other.scaricoIniziale)
			return false;
		if (tempoSosta != other.tempoSosta)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SchedaParametri [codiceFiscalePaziente=" + pazienteCodiceFiscale+ ", peso=" + peso + ", paMin=" + paMin
				+ ", paMax=" + paMax + ", scaricoIniziale=" + scaricoIniziale + ", UF=" + UF + ", tempoSosta="
				+ tempoSosta + ", carico=" + carico + ", scarico=" + scarico + ", ora=" + ", data=" + data + "]";
	}
	

}
