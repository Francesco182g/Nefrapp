package test.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bean.PianoTerapeutico;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PianoTerapeuticoTest {

  private static final String diagnosi = "Gentilissimo paziente, dopo aver attentamente "
      + "analizzato i suoi andamenti nella scorsa settimana"
      + " ho deciso di apportare delle modifiche al suo piano terapeutico. "
      + "Le modifiche sono le seguenti: Lasix da 2 volte al giorno a 1 volta al giorno. "
      + "Buona giornata.";
  private static final LocalDate dataFineTerapia = LocalDate.parse("2020-03-28");
  private static final LocalDate dataNull = null;

  @Test
  void testPianoTerapeuticoCostruttoreVuoto() {
    PianoTerapeutico piano = new PianoTerapeutico();
    assertNotNull(piano);
  }

  //Test per i metodi di get

  @Test
  void testGetCodiceFiscalePaziente() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals("DWNRRT85E18I483W", piano.getCodiceFiscalePaziente());
  }

  @Test
  void testGetDiagnosi() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals(diagnosi, piano.getDiagnosi());
  }

  @Test
  void testGetFarmaco() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals("Lasix 1x 25g", piano.getFarmaco());
  }

  @Test
  void testGetDataFineTerapia() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals(dataFineTerapia, piano.getDataFineTerapia());
  }

  @Test
  void testGetDataFormattata() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals("28/03/2020", piano.getDataFormattata());
  }

  @Test
  void testGetVisualizzato() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    assertEquals(false, piano.getVisualizzato());
  }

  //Test per i metodi di set

  @Test
  void testSetCodiceFiscalePaziente() {
    PianoTerapeutico piano = new PianoTerapeutico("", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    piano.setCodiceFiscalePaziente("DWNRRT85E18I483W");
    assertEquals("DWNRRT85E18I483W", piano.getCodiceFiscalePaziente());
  }

  @Test
  void testSetDiagnosi() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", "", "Lasix 1x 25g", dataFineTerapia);
    piano.setDiagnosi(diagnosi);
    assertEquals(diagnosi, piano.getDiagnosi());
  }

  @Test
  void testSetFarmaco() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "", dataFineTerapia);
    piano.setFarmaco("Lasix 1x 25g");
    assertEquals("Lasix 1x 25g", piano.getFarmaco());
  }

  @Test
  void testSetDataFineTerapia() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataNull);
    piano.setDataFineTerapia(dataFineTerapia);
    assertEquals(dataFineTerapia, piano.getDataFineTerapia());
  }

  @Test
  void testSetVisualizzato() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    piano.setVisualizzato(true);
    assertEquals(true, piano.getVisualizzato());
  }

  //Test per il toString
  @Test
  void testToString() {
    PianoTerapeutico piano = 
        new PianoTerapeutico("DWNRRT85E18I483W", diagnosi, "Lasix 1x 25g", dataFineTerapia);
    String toStringBean = piano.toString();
    String toStringTest = 
        "PianoTerapeutico [codiceFiscalePaziente=" + piano.getCodiceFiscalePaziente() 
        + ", diagnosi=" + piano.getDiagnosi()
        + ", farmaco=" + piano.getFarmaco() 
        + ", dataFineTerapia=" + piano.getDataFineTerapia() 
        + ", visualizzato=" + piano.getVisualizzato() + "]";
    assertEquals(toStringBean, toStringTest);
  }
}