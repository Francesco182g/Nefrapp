package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bean.Medico;
import bean.Paziente;
import bean.Utente;
import model.MedicoModel;
import model.PazienteModel;
import model.UtenteModel;

class UtenteModelTest {
  private static final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
  private static final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";
  private static final String residenzaPaziente = "Via Mazzini, 22, Bellizzi, 84092, SA";
  private static final LocalDate dataNascitaPaziente = LocalDate.parse("1979-05-16");

  @BeforeEach
  void setUp() {
    Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
        "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
    MedicoModel.addMedico(medico, "Quadri1234");

    Paziente paziente = new Paziente("M", "RSSGPP79E16I483", "Giuseppe", "Russo", "russo.giuseppe@live.it", residenzaPaziente, 
        "Scafati", dataNascitaPaziente, true, new ArrayList<String>(Arrays.asList("GRMBNN67L11B519R")));
    PazienteModel.addPaziente(paziente, "PasswordDifficile");
  }

  @AfterEach
  void tearDown() {
    MedicoModel.removeMedico("GRMBNN67L11B519R");
    PazienteModel.removePaziente("RSSGPP79E16I483");
  }

  @Test
  void testGetUtenteByCFMedico() {
    Utente utente = UtenteModel.getUtenteByCF("RSSGPP79E16I483");
    assertNotNull(utente);
    assertEquals(utente.getNome(), "Giuseppe");
    assertEquals(utente.getCognome(), "Russo");
    assertEquals(utente.getEmail(), "russo.giuseppe@live.it");
    assertEquals(utente.getCodiceFiscale(), "RSSGPP79E16I483");
  }

  @Test
  void testGetUtenteByCFPaziente() {
    Utente utente = UtenteModel.getUtenteByCF("GRMBNN67L11B519R");
    assertNotNull(utente);
    assertEquals(utente.getNome(), "Geremia");
    assertEquals(utente.getCognome(), "Bernini");
    assertEquals(utente.getEmail(), "G.Bernini67@gmail.com");
    assertEquals(utente.getCodiceFiscale(), "GRMBNN67L11B519R");
  }
}