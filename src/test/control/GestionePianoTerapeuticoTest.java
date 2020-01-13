package test.control;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import bean.Medico;
import bean.Paziente;
import bean.PianoTerapeutico;
import control.GestionePianoTerapeutico;
import model.DriverConnection;
import model.PianoTerapeuticoModel;
import utility.CreaBeanUtility;
import utility.CriptazioneUtility;

class GestionePianoTerapeuticoTest {
	private static Paziente paziente;
	private static Medico medico;
	private static String CfPaziente = "BNCLRD67A01F205I";
	private static String CfMedico = "GRMBNN67L11B516R";
	private static PianoTerapeutico piano = new PianoTerapeutico(CfPaziente, "Lasix, 1 volta al giorno", 
			"Lasix 1x 25 mg\n", LocalDate.now().plusDays(100L));
	
	private GestionePianoTerapeutico servlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		ArrayList<String> campoMedici = new ArrayList<>();
		campoMedici.add(CfMedico);
		String password1 = CriptazioneUtility.criptaConMD5("Fiori5678");
		Document doc1 = new Document("CodiceFiscale", CfPaziente).append("Password", password1).append("Attivo", true).append("Medici", campoMedici);;
		pazienti.insertOne(doc1);
		paziente = CreaBeanUtility.daDocumentAPaziente(doc1);
		paziente.setMedici(campoMedici);
		
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		String password2 = CriptazioneUtility.criptaConMD5("Quadri1234");
		Document doc2 = new Document("CodiceFiscale", CfMedico).append("Password", password2).append("Attivo", true);
		medici.insertOne(doc2);
		medico = CreaBeanUtility.daDocumentAMedico(doc2);
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		FindIterable<Document> docs = pazienti.find(eq("CodiceFiscale", paziente.getCodiceFiscale()));
		for (Document d : docs) {
			pazienti.deleteOne(d);
		}

		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		FindIterable<Document> docs2 = medici.find(eq("CodiceFiscale", medico.getCodiceFiscale()));
		for (Document d : docs2) {
			medici.deleteOne(d);
		}
	}
	
	@BeforeEach
	void setUp() throws Exception {
		servlet = new GestionePianoTerapeutico();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		MongoCollection<Document> pianoTerapeutico = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		
		Document doc = new Document("PazienteCodiceFiscale", piano.getCodiceFiscalePaziente())
				.append("Diagnosi", piano.getDiagnosi())
				.append("Farmaco",piano.getFarmaco())
				.append("FineTerapia", piano.getDataFineTerapia())
				.append("Visualizzato", piano.getVisualizzato());
		pianoTerapeutico.insertOne(doc);
	}

	@AfterEach
	void tearDown() throws Exception {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection()
				.getCollection("PianoTerapeutico");
		BasicDBObject searchQuery = new BasicDBObject().append("PazienteCodiceFiscale",
				piano.getCodiceFiscalePaziente());
		pianoTerapeuticoDB.deleteOne(searchQuery);
		
		request.getSession().invalidate();
	}
	
	@Test
	void testNoOperazione() throws Exception {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("CFPaziente", CfPaziente);
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=noOperazione", response.getRedirectedUrl());
		
		tearDown();
		setUp();
		
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("operazione", "non supportata");
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=noOperazione", response.getRedirectedUrl());
	}
	
	@Test
	void testVisualizzaDatiInvalidi() throws Exception {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("operazione", "visualizza");
		request.setParameter("CFPaziente", "INVALIDO");
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=cfNonValido", response.getRedirectedUrl());
		
		tearDown();
		setUp();
		
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("operazione", "visualizza");
		request.setParameter("CFPaziente", "INVALIDO");
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=cfNonValido", response.getRedirectedUrl());
	}
	

	@Test
	void testVisualizzaComePaziente() throws ServletException, IOException {
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);
		request.getSession().setAttribute("accessDone", true);
		
		request.setParameter("CFPaziente", CfPaziente);
		request.setParameter("operazione", "visualizza");
		servlet.doGet(request, response);
		
		assertEquals(piano.toString(), request.getAttribute("pianoTerapeutico").toString());
	}
	
	@Test
	void testVisualizzaComeMedico() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		
		request.setParameter("CFPaziente", CfPaziente);
		request.setParameter("operazione", "visualizza");
		servlet.doGet(request, response);
		
		assertEquals(piano.toString(), request.getAttribute("pianoTerapeutico").toString());
	}

//	Andrebbe testata la visualizzazione con chiamata asincrona ma non saprei cosa asserire
//	@Test
//	void testVisualizzaAsync() throws ServletException, IOException {
//		request.getSession().setAttribute("utente", medico);
//		request.getSession().setAttribute("isMedico", true);
//		request.getSession().setAttribute("accessDone", true);
//		
//		request.setParameter("tipo", "asincrona");
//		request.setParameter("operazione", "visualizza");
//		servlet.doGet(request, response);
//	}
	
	@Test
	void testModificaDatiInvalidi() throws Exception {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("operazione", "modifica");
		request.setParameter("CFPaziente", "INVALIDO");
		request.setParameter("diagnosi", "prova diagnosi");
		request.setParameter("farmaci", "farmaci di prova");
		request.setParameter("data", "09/03/2020");
		servlet.doPost(request, response);
		assertEquals("./paginaErrore.jsp?notifica=datiNonValidi", response.getRedirectedUrl());
		
		tearDown();
		setUp();
		
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		request.setParameter("operazione", "modifica");
		request.setParameter("CFPaziente", CfPaziente);
		request.setParameter("diagnosi", "prova diagnosi");
		request.setParameter("farmaci", "farmaci di prova");
		request.setParameter("data", "09-03-2020");
		servlet.doPost(request, response);
		assertEquals("./paginaErrore.jsp?notifica=datiNonValidi", response.getRedirectedUrl());
	}

	@Test
	void testModifica() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		
		request.setParameter("CFPaziente", CfPaziente);
		request.setParameter("operazione", "modifica");
		request.setParameter("diagnosi", "prova diagnosi");
		request.setParameter("farmaci", "farmaci di prova");
		request.setParameter("data", "09/03/2020");
		servlet.doPost(request, response);
		
		PianoTerapeutico modificato = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CfPaziente);
		assertEquals(modificato.getDiagnosi(), "prova diagnosi");
		assertEquals(modificato.getFarmaco(), "farmaci di prova");
		assertEquals(modificato.getDataFormattata(), "09/03/2020");
	}
}
