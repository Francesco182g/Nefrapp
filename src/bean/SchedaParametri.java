package bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Questa classe rappresenta la scheda parametri.
 * @author Silvio Di Martino 
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

  public SchedaParametri(String pazienteCodiceFiscale, BigDecimal peso, int paMin, int paMax, 
      int scaricoIniziale, int UF, int tempoSosta, int carico, int scarico, LocalDate data) {
    this.pazienteCodiceFiscale = pazienteCodiceFiscale;
    this.peso = peso;
    this.paMin = paMin;
    this.paMax = paMax;
    this.scaricoIniziale = scaricoIniziale;
    this.UF = UF;
    this.tempoSosta = tempoSosta;
    this.carico = carico;
    this.scarico = scarico;
    this.data = data;
  }

  public String getPazienteCodiceFiscale() {
    return pazienteCodiceFiscale;
  }

  public void setPazienteCodiceFiscale(String pazienteCodiceFiscale) {
    this.pazienteCodiceFiscale = pazienteCodiceFiscale;
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

  public String getDataFormattata() {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return data.format(format);
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "SchedaParametri [codiceFiscalePaziente=" + pazienteCodiceFiscale + ", peso=" + peso 
        + ", paMin=" + paMin + ", paMax=" + paMax + ", scaricoIniziale=" + scaricoIniziale 
        + ", UF=" + UF + ", tempoSosta=" + tempoSosta + ", carico=" + carico + ", scarico=" 
        + scarico + ", ora=" + ", data=" + data + "]";
  }
}