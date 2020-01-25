package test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bean.PianoTerapeutico;
import java.time.LocalDate;
import model.PianoTerapeuticoModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PianoTerapeuticoModelTest {
  private static final String CODICE_FISCALE_PAZIENTE = "CRRSRA90A50A091Q";
  private static final String DIAGNOSI = "Devi prendere i farmaci sottostanti due volte al giorno";
  private static final String FARMACO = "Lasix 30mm";
  private static final LocalDate FINE_TERAPIA = LocalDate.parse("2020-07-11");
  private static final Boolean VISUALIZZATO = false;

  @BeforeEach
  void setUp() {
    PianoTerapeutico pianoTerapeutico = 
        new PianoTerapeutico(CODICE_FISCALE_PAZIENTE, DIAGNOSI, FARMACO, FINE_TERAPIA);
    PianoTerapeuticoModel.addPianoTerapeutico(pianoTerapeutico);
  }

  @AfterEach
  void tearDown() {
    PianoTerapeuticoModel.removePianoTerapeutico(CODICE_FISCALE_PAZIENTE);
  }

  @Test
  void testGetPianoTerapeuticoByPaziente() {
    PianoTerapeutico pianoTerapeutico = 
        PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
    assertNotNull(pianoTerapeutico);
    assertEquals(pianoTerapeutico.getCodiceFiscalePaziente(), CODICE_FISCALE_PAZIENTE);
    assertEquals(pianoTerapeutico.getDiagnosi(), DIAGNOSI);
    assertEquals(pianoTerapeutico.getFarmaco(), FARMACO);
    assertEquals(pianoTerapeutico.getDataFineTerapia(), FINE_TERAPIA);
    assertEquals(pianoTerapeutico.getVisualizzato(), VISUALIZZATO);
  }

  @Test
  void testUpdatePianoTerapeutico() {
    PianoTerapeutico daAggiornare = 
        PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
    assertNotNull(daAggiornare);
    daAggiornare.setDiagnosi("Devi prendere i farmaci sottostanti una volta al giorno");
    daAggiornare.setFarmaco("Lasix 50mm");
    PianoTerapeuticoModel.updatePianoTerapeutico(daAggiornare);
    daAggiornare = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getDiagnosi(), "Devi prendere i farmaci sottostanti una volta al giorno");
    assertEquals(daAggiornare.getFarmaco(), "Lasix 50mm");
  }

  @Test
  void testIsPianoTerapeuticoVisualizzato() {
    Boolean isVisualizzato = 
        PianoTerapeuticoModel.isPianoTerapeuticoVisualizzato(CODICE_FISCALE_PAZIENTE);
    assertEquals(isVisualizzato, false);
  }

  @Test
  void testSetVisualizzatoPianoTerapeutico() {
    PianoTerapeuticoModel.setVisualizzatoPianoTerapeutico(CODICE_FISCALE_PAZIENTE, true);
    PianoTerapeutico pianoTerapeutico = 
        PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
    assertEquals(pianoTerapeutico.getVisualizzato(), true);
  }

}