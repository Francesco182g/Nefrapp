package bean;

import java.time.ZonedDateTime;
import java.util.HashMap;

/**
 * Interfaccia che determina le operazioni che possono essere 
 * compiute da un'implementazione concreta di Annuncio.
 * 
 * @author NefrappTeam
 */
public interface Annuncio {

  public String getMedico();

  public void setMedico(String medico);

  public String getTitolo();

  public void setTitolo(String titolo);

  public String getTesto();

  public void setTesto(String testo);

  public String getCorpoAllegato();

  public void setCorpoAllegato(String corpoAllegato);

  public String getNomeAllegato();

  public void setNomeAllegato(String nomeAllegato);

  public ZonedDateTime getData();

  public String getDataFormattata();

  public void setData(ZonedDateTime data);

  public void setIdAnnuncio(String idMessaggio);

  public String getIdAnnuncio();

  public void setPazientiView(HashMap<String, Boolean> pazientiView);

  public HashMap<String,Boolean> getPazientiView();

  public String toString();
}