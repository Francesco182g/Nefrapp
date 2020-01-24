package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;

class PazienteModelTest {
  private static final LocalDate dataNascitaPaziente = LocalDate.parse("1979-05-16");
  private static final String residenzaPaziente = "Via Mazzini, 22, Bellizzi, 84092, SA";
  private static final ArrayList<String> medici = new ArrayList<String>();

  private static final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
  private static final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";

  @BeforeEach
  void setUp() {
	medici.add("GRMBNN67L11B519R");
    Paziente paziente = new Paziente("M", "RSSGPP79E16I483P", "Giuseppe", "Russo", "giuseppe.russo@live.it", residenzaPaziente, "Scafati", dataNascitaPaziente, false, medici);
    PazienteModel.addPaziente(paziente, "PasswordDifficile");

    Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
        "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
    MedicoModel.addMedico(medico, "Quadri1234");
  }

  @AfterEach
  void tearDown() {
    PazienteModel.removePaziente("RSSGPP79E16I483P");
    PazienteModel.removePaziente("BNCLRD67A01F205I");

    MedicoModel.removeMedico("GRMBNN67L11B519R");
  }

  @Test
  void testGetPazienteByCFPassword() {
    Paziente paziente =
        PazienteModel.getPazienteByCFPassword("RSSGPP79E16I483P", "PasswordDifficile");
    assertNotNull(paziente);
    assertEquals(paziente.getNome(), "Giuseppe");
    assertEquals(paziente.getCognome(), "Russo");
    assertEquals(paziente.getEmail(), "giuseppe.russo@live.it");
    assertEquals(paziente.getCodiceFiscale(), "RSSGPP79E16I483P");
    assertEquals(paziente.getSesso(), "M");
    assertEquals(paziente.getResidenza(), residenzaPaziente);
    assertEquals(paziente.getDataDiNascita(), dataNascitaPaziente);
    assertEquals(paziente.getLuogoDiNascita(), "Scafati");
    assertEquals(paziente.getAttivo(),false);
    assertEquals(paziente.getMedici(), medici);
  }

  @Test
  void testGetPazientiSeguiti() {
    ArrayList<Paziente> pazienti = PazienteModel.getPazientiSeguiti("GRMBNN67L11B519R");
    System.out.println(pazienti.toString());
    assertNotNull(pazienti);	
  }


  @Test
  void testAddPaziente() {
    Paziente daAggiungere = new Paziente("M", "BNCLRD67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
    PazienteModel.addPaziente(daAggiungere, "Fiori5678");

    Paziente paziente = PazienteModel.getPazienteByCFPassword("BNCLRD67A01F205I", "Fiori5678");
    assertNotNull(paziente);
    assertEquals(paziente.getNome(), "Andrea");
    assertEquals(paziente.getCognome(), "Rossi");
    assertEquals(paziente.getEmail(), "");
    assertEquals(paziente.getCodiceFiscale(), "BNCLRD67A01F205I");
    assertEquals(paziente.getSesso(), "M");
    assertEquals(paziente.getResidenza(), residenzaPaziente);
    assertEquals(paziente.getDataDiNascita(), dataNascitaPaziente);
    assertEquals(paziente.getLuogoDiNascita(), "Salerno");
    assertEquals(paziente.getAttivo(),false);
    assertEquals(paziente.getMedici(), medici);
  }

  @Test
  void testGetPazienteByCF() {
    Paziente paziente = PazienteModel.getPazienteByCF("RSSGPP79E16I483P");
    assertNotNull(paziente);
    assertEquals(paziente.getNome(), "Giuseppe");
    assertEquals(paziente.getCognome(), "Russo");
    assertEquals(paziente.getEmail(), "giuseppe.russo@live.it");
    assertEquals(paziente.getCodiceFiscale(), "RSSGPP79E16I483P");
    assertEquals(paziente.getSesso(), "M");
    assertEquals(paziente.getResidenza(), residenzaPaziente);
    assertEquals(paziente.getDataDiNascita(), dataNascitaPaziente);
    assertEquals(paziente.getLuogoDiNascita(), "Scafati");
    assertEquals(paziente.getAttivo(), false);
    assertEquals(paziente.getMedici(), medici);
  }

  @Test
  void testGetIdPazienteByCF() {
    Paziente paziente = new Paziente("M", "BNCLRD67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
    PazienteModel.addPaziente(paziente, "Fiori5678");
    assertNotNull(paziente);
    String id = PazienteModel.getIdPazienteByCF("BNCLRD67A01F205I");
    assertNotNull(id);
  }
  
  @Test
  void testGetIdPazienteByCFNonPresente() {
    Paziente paziente = new Paziente("M", "BNCLRD67A01F205O", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
    PazienteModel.addPaziente(paziente, "Fiori5678");
    assertNotNull(paziente);
    String id = PazienteModel.getIdPazienteByCF("BNCLRD67A01F205I");
    assertNull(id);
  }

  @Test
  void testRemovePaziente() {
    PazienteModel.removePaziente("RSSGPP79E16I483P");
    Paziente paziente = PazienteModel.getPazienteByCF("RSSGPP79E16I483P");
    assertNull(paziente);
  }

  @Test
  void testGetAllPazienti() {
    ArrayList<Paziente> pazienti = PazienteModel.getAllPazienti();
    //anche se non ci sono pazienti, restituisce un arrayList void che è comunque != null, no?
    assertNotNull(pazienti);	
  }

  @Test 
  void testChangePassword() {
    PazienteModel.changePassword("RSSGPP79E16I483P", "NuovaPasswordBella");
    Paziente paziente = 
        PazienteModel.getPazienteByCFPassword("RSSGPP79E16I483P", "NuovaPasswordBella");
    assertNotNull(paziente);
  }

  @Test 
  void testUpdatePaziente() {
    Paziente daAggiornare = PazienteModel.getPazienteByCF("RSSGPP79E16I483P");
    assertNotNull(daAggiornare);
    daAggiornare.setNome("Ettore");
    daAggiornare.setCognome("Anzano");
    PazienteModel.updatePaziente(daAggiornare);
    daAggiornare = PazienteModel.getPazienteByCF("RSSGPP79E16I483P");
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getNome(), "Ettore");
    assertEquals(daAggiornare.getCognome(), "Anzano");
  }	
}