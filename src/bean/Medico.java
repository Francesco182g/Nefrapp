package bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Antonio Donnarumma
 * Questa classe rappresenta il medico
 */
public class Medico extends Utente { 
  private String sesso; 
  private String residenza;
  private LocalDate dataDiNascita;
  private String luogoDiNascita;

  public Medico() {
  }

  public Medico(String sesso, String residenza, LocalDate dataDiNascita, String codiceFiscale,
      String nome, String cognome,String email,String luogoDiNascita) {
    super(codiceFiscale, nome, cognome,email);
    this.sesso = sesso;
    this.residenza = residenza;
    this.dataDiNascita = dataDiNascita;
    this.luogoDiNascita = luogoDiNascita;
  }

  public String getSesso() {
    return sesso;
  }

  public void setSesso(String sesso) {
    this.sesso = sesso;
  }

  public String getResidenza() {
    return residenza;
  }

  public void setResidenza(String residenza) {
    this.residenza = residenza;
  }

  public LocalDate getDataDiNascita() {
    return dataDiNascita;
  }

  public String getDataFormattata() {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return dataDiNascita.format(format);
  }

  public void setDataDiNascita(LocalDate dataDiNascita) {
    this.dataDiNascita = dataDiNascita;
  }

  @Override
  public String toString() {
    return "Medico [sesso=" + sesso + ", residenza=" + residenza + ","
        + " dataDiNascita=" + dataDiNascita + ", email=" + getEmail() 
        + ", luogoDiNascita=" + luogoDiNascita + ", codiceFiscale=" + getCodiceFiscale()
        + ", nome=" + getNome() + ", cognome=" + getCognome() + "]";
  }

  public String getLuogoDiNascita() {
    return luogoDiNascita;
  }

  public void setLuogoDiNascita(String luogoDiNascita) {
    this.luogoDiNascita = luogoDiNascita;
  }
}