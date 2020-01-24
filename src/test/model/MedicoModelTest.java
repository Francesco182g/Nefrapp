package test.model;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import bean.Medico;
import bean.Paziente;
import model.DriverConnection;
import model.MedicoModel;
import utility.CreaBeanUtility;
import utility.CriptazioneUtility;

class MedicoModelTest {
  private static final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
  private static final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";

  @BeforeEach
  void setUp() {
    Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
        "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
    MedicoModel.addMedico(medico, "Quadri1234");
  }

  @AfterEach
  void tearDown() {
    MedicoModel.removeMedico("GRMBNN67L11B519R");
    MedicoModel.removeMedico("CRLBNN67L11B519R");
  }

  @Test
  void testGetMedicoByCF() {
    Medico medico = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
    assertNotNull(medico);
    assertEquals(medico.getNome(), "Geremia");
    assertEquals(medico.getCognome(), "Bernini");
    assertEquals(medico.getEmail(), "G.Bernini67@gmail.com");
    assertEquals(medico.getCodiceFiscale(), "GRMBNN67L11B519R");
    assertEquals(medico.getSesso(), "M");
    assertEquals(medico.getResidenza(), residenzaMedico);
    assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
    assertEquals(medico.getLuogoDiNascita(), "Campobasso");
  }

  @Test
  void testGetMedicoByCFPassword() {
    Medico medico = MedicoModel.getMedicoByCFPassword("GRMBNN67L11B519R", "Quadri1234");
    assertNotNull(medico);
    assertEquals(medico.getNome(), "Geremia");
    assertEquals(medico.getCognome(), "Bernini");
    assertEquals(medico.getEmail(), "G.Bernini67@gmail.com");
    assertEquals(medico.getCodiceFiscale(), "GRMBNN67L11B519R");
    assertEquals(medico.getSesso(), "M");
    assertEquals(medico.getResidenza(), residenzaMedico);
    assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
    assertEquals(medico.getLuogoDiNascita(), "Campobasso");
  }

  @Test
  void testRemoveMedico() {
    MedicoModel.removeMedico("GRMBNN67L11B519R");
    Medico medico = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
    assertNull(medico);

  }

  @Test
  void testAddMedico() {
    Medico daAggiungere = new Medico("M", residenzaMedico, dataNascitaMedico, 
        "CRLBNN67L11B519R", "Carlo", "Bernini", "C.Bernini67@gmail.com", "Campobasso");
    MedicoModel.addMedico(daAggiungere, "Quadri9999");

    Medico medico = MedicoModel.getMedicoByCFPassword("CRLBNN67L11B519R", "Quadri9999");
    assertNotNull(medico);
    assertEquals(medico.getNome(), "Carlo");
    assertEquals(medico.getCognome(), "Bernini");
    assertEquals(medico.getEmail(), "C.Bernini67@gmail.com");
    assertEquals(medico.getCodiceFiscale(), "CRLBNN67L11B519R");
    assertEquals(medico.getSesso(), "M");
    assertEquals(medico.getResidenza(), residenzaMedico);
    assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
    assertEquals(medico.getLuogoDiNascita(), "Campobasso");

    MedicoModel.removeMedico(daAggiungere.getCodiceFiscale());
  }

  @Test
  void testGetAllMedici() {
    List<Medico> medici = MedicoModel.getAllMedici();
    assertNotNull(medici);
    //TODO: non so se bisogna fare ulteriori controlli sui medici trovati
  }

  @Test
  void testUpdateMedico() {
    Medico daAggiornare = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
    assertNotNull(daAggiornare);
    daAggiornare.setNome("Geremia Luca");
    daAggiornare.setEmail("G.Bernini1967@gmail.com");
    MedicoModel.updateMedico(daAggiornare);
    daAggiornare = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getNome(), "Geremia Luca");
    assertEquals(daAggiornare.getEmail(), "G.Bernini1967@gmail.com");
  }

	@Test
	void testGetMediciByPazienteSeguito() {
		String CfMedico = "GRMBNN67L11B519R";
		String CfPaziente = "BNCLRD67A01F205I";
		Paziente paziente;
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		ArrayList<String> campoMedici = new ArrayList<>();
		campoMedici.add(CfMedico);
		String password1 = CriptazioneUtility.criptaConMD5("Fiori5678");
		Document doc1 = new Document("CodiceFiscale", CfPaziente).append("Password", password1).append("Attivo", true)
				.append("Medici", campoMedici);
		pazienti.insertOne(doc1);
		paziente = CreaBeanUtility.daDocumentAPaziente(doc1);
		paziente.setMedici(campoMedici);

		ArrayList<Medico> medici = MedicoModel.getMediciByPazienteSeguito(paziente);
		assertEquals(medici.get(0).getCodiceFiscale(), CfMedico);
		
	    FindIterable<Document> docs = pazienti.find(eq("CodiceFiscale", paziente.getCodiceFiscale()));
	    for (Document d : docs) {
	      pazienti.deleteOne(d);
	    }
	}

  @Test
  void testGetMedicoByEmail() {
    Medico medico = MedicoModel.getMedicoByEmail("G.Bernini67@gmail.com");
    assertNotNull(medico);
    assertEquals(medico.getNome(), "Geremia");
    assertEquals(medico.getCognome(), "Bernini");
    assertEquals(medico.getEmail(), "G.Bernini67@gmail.com");
    assertEquals(medico.getCodiceFiscale(), "GRMBNN67L11B519R");
    assertEquals(medico.getSesso(), "M");
    assertEquals(medico.getResidenza(), residenzaMedico);
    assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
    assertEquals(medico.getLuogoDiNascita(), "Campobasso");
  }

  @Test
  void testUpdatePasswordMedico() {
    MedicoModel.updatePasswordMedico("GRMBNN67L11B519R", "NonVoglioQuadri12");
    Medico medico = MedicoModel.getMedicoByCFPassword("GRMBNN67L11B519R", "NonVoglioQuadri12");
    assertNotNull(medico);
  }
  
  @Test
  void testCheckEmailSuccess() {
    assertTrue(MedicoModel.checkEmail("G.Bernini67@gmail.com"));
  }
  
  @Test
  void testCheckEmailFail() {
    assertFalse(MedicoModel.checkEmail("G.Bernino67@gmail.com"));
  }
  
}