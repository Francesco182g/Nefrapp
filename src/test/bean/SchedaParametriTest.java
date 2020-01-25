package test.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bean.SchedaParametri;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SchedaParametriTest {

  private static final LocalDate dataInserimento = LocalDate.parse("2020-01-14");
  private static final LocalDate dataNull = null;
  private static final BigDecimal pesoPaziente = new BigDecimal(70);
  private static final BigDecimal pesoNull = new BigDecimal(0);

  @Test
  void testSchedaParametriCostruttoreVuoto() {
    SchedaParametri scheda = new SchedaParametri();
    assertNotNull(scheda);
  }

  //Test per i metodi di get

  @Test
  void testGetPazienteCodiceFiscale() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals("DWNRRT85E18I483W", scheda.getPazienteCodiceFiscale());
  }

  @Test
  void testGetPeso() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(pesoPaziente, scheda.getPeso());
  }

  @Test
  void testGetPaMin() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(80, scheda.getPaMin());
  }

  @Test
  void testGetPaMax() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(120, scheda.getPaMax());
  }

  @Test
  void testGetScaricoIniziale() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(1550, scheda.getScaricoIniziale());
  }

  @Test
  void testGetUF() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(700, scheda.getUF());
  }

  @Test
  void testGetTempoSosta() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(1, scheda.getTempoSosta());
  }

  @Test
  void testGetCarico() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(1500, scheda.getCarico());
  }

  @Test
  void testGetScarico() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(1000, scheda.getScarico());
  }

  @Test
  void testGetData() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals(dataInserimento, scheda.getData());
  }

  @Test
  void testGetDataFormattata() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    assertEquals("14/01/2020", scheda.getDataFormattata());
  }

  //Test per i metodi di set

  @Test
  void testSetPazienteCodiceFiscale() {
    SchedaParametri scheda = new SchedaParametri("", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    scheda.setPazienteCodiceFiscale("DWNRRT85E18I483W");
    assertEquals("DWNRRT85E18I483W", scheda.getPazienteCodiceFiscale());
  }

  @Test
  void testSetPeso() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoNull, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    scheda.setPeso(pesoPaziente);
    assertEquals(pesoPaziente, scheda.getPeso());
  }

  @Test
  void testSetPaMin() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 0, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    scheda.setPaMin(80);
    assertEquals(80, scheda.getPaMin());
  }

  @Test
  void testSetPaMax() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 0, 1550, 700, 1, 1500, 1000, dataInserimento);
    scheda.setPaMax(120);
    assertEquals(120, scheda.getPaMax());
  }

  @Test
  void testSetScaricoIniziale() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 0, 700, 1, 1500, 1000, dataInserimento);
    scheda.setScaricoIniziale(1550);
    assertEquals(1550, scheda.getScaricoIniziale());
  }

  @Test
  void testSetUF() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 0, 1, 1500, 1000, dataInserimento);
    scheda.setUF(700);
    assertEquals(700, scheda.getUF());
  }

  @Test
  void testSetTempoSosta() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 0, 1500, 1000, dataInserimento);
    scheda.setTempoSosta(1);
    assertEquals(1, scheda.getTempoSosta());
  }

  @Test
  void testSetCarico() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 0, 1000, dataInserimento);
    scheda.setCarico(1500);
    assertEquals(1500, scheda.getCarico());
  }

  @Test
  void testSetScarico() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 0, dataInserimento);
    scheda.setScarico(1000);
    assertEquals(1000, scheda.getScarico());
  }

  @Test
  void testSetData() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataNull);
    scheda.setData(dataInserimento);
    assertEquals(dataInserimento, scheda.getData());
  }

  //Test per il toString
  @Test
  void testToString() {
    SchedaParametri scheda = new SchedaParametri("DWNRRT85E18I483W", pesoPaziente, 80, 120, 1550, 700, 1, 1500, 1000, dataInserimento);
    String toStringBean = scheda.toString();
    String toStringTest = "SchedaParametri [codiceFiscalePaziente=" 
        + scheda.getPazienteCodiceFiscale() 
        + ", peso=" + scheda.getPeso() 
        + ", paMin=" + scheda.getPaMin() 
        + ", paMax=" + scheda.getPaMax() 
        + ", scaricoIniziale=" + scheda.getScaricoIniziale() 
        + ", UF=" + scheda.getUF() 
        + ", tempoSosta=" + scheda.getTempoSosta() 
        + ", carico=" + scheda.getCarico()
        + ", scarico=" + scheda.getScarico() 
        + ", ora=" 
        + ", data=" + scheda.getData() + "]";
    assertEquals(toStringBean, toStringTest);
  }
}