package test.control;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.Medico;
import bean.Paziente;
import control.GestioneAnnunci;
import control.GestioneMessaggi;
import model.DriverConnection;
import utility.CreaBeanUtility;
import utility.CriptazioneUtility;

public class GestioneAnnunciTest {
	private static Annuncio annuncio;
	private static Medico medico;
	private static Paziente paziente;
	private static String titolo="Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.";
	private static String testo="Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.";
	private static String nomeAllegato="dialisi-peritoneale.pdf";
	private static String CFMedico = "GRMBNN67L11B516R";
	private static String CFPaziente1="BNCLRD67A01F205I";
	private static HashMap<String, Boolean> destinatari;
	private GestioneAnnunci servlet;
	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		String password1 = CriptazioneUtility.criptaConMD5("Fiori5678");
		Document doc1 = new Document("CodiceFiscale", CFPaziente1).append("Password", password1).append("Attivo", true);
		pazienti.insertOne(doc1);
		paziente = CreaBeanUtility.daDocumentAPaziente(doc1);
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		String password2 = CriptazioneUtility.criptaConMD5("Quadri1234");
		Document doc2 = new Document("CodiceFiscale", CFMedico).append("Password", password2).append("Attivo", true);
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
		servlet = new GestioneAnnunci();
		request = new MockMultipartHttpServletRequest();
		response = new MockHttpServletResponse();
		
		destinatari = new HashMap<String, Boolean>();
		destinatari.put(CFPaziente1, false);
		annuncio=new AnnuncioCompleto(CFMedico,titolo,testo,null,null,ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatari);
		
		MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
		ArrayList<Document> destinatariView = new ArrayList<Document>();
		Iterator it = annuncio.getPazientiView().entrySet().iterator();
		
		if (!it.hasNext()) {
			Document coppia = new Document();
			coppia.append("CFDestinatario", null).append("Visualizzazione", false);
			destinatariView.add(coppia);
		} else {
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Document coppia = new Document();
				coppia.append("CFDestinatario", pair.getKey()).append("Visualizzazione", pair.getValue());
				destinatariView.add(coppia);
			}
		}
		
		Document allegato = new Document("NomeAllegato", annuncio.getNomeAllegato()).append("CorpoAllegato",
				annuncio.getCorpoAllegato());

		Document doc = new Document("MedicoCodiceFiscale", annuncio.getMedico())
				.append("Titolo", annuncio.getTitolo()).append("Testo", annuncio.getTesto())
				.append("Allegato", allegato).append("Data", annuncio.getData().toInstant())
				.append("PazientiView", destinatariView);
		annunci.insertOne(doc);
		
		ObjectId idObj = (ObjectId)doc.get("_id");
		annuncio.setIdAnnuncio(idObj.toString());
	}
	
	@AfterEach
	void tearDown() throws Exception {
		MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
		FindIterable <Document> messaggioDoc2 = annunci.find(eq("MedicoCodiceFiscale", CFMedico))
				.projection(Projections.include("_id"));
		for (Document d : messaggioDoc2) {
			annunci.deleteOne(d);
		}
		annuncio=null;
		request.getSession().invalidate();
	}
	
	@Test
	void testAnnuncioSenzaOperazione() throws ServletException, IOException {
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GM_9_1_InvioAnnunci() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.");
		request.setParameter("testo", testo);
		request.setParameter("operazione", "inviaAnnuncio");
		servlet.doPost(request, response);
		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GM_9_2_InvioAnnunci() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", titolo);
		request.setParameter("testo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sollicitudin nisi sit amet nibh congue scelerisque. Donec ornare pharetra erat, at lobortis tellus commodo eu. Integer gravida nulla non risus aliquam, elementum mollis turpis fringilla. Sed vel commodo ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum viverra diam fermentum sapien cursus scelerisque. Praesent porta ac diam eu tempus. Pellentesque pellentesque nisi enim, non pellentesque mauris maximus nec. Nunc et enim eu purus porta molestie eget sed libero. Donec ullamcorper ligula orci, eu molestie lorem pharetra at. Nulla tortor tellus, varius sagittis congue quis, lobortis et metus. Sed elementum varius justo, et sollicitudin lectus porttitor at.\r\n" + 
				"Integer porta diam nec commodo consequat. Pellentesque pharetra vel lacus nec condimentum. Vivamus metus tortor, mattis non pulvinar in, mollis quis nibh. Cras ultrices vel orci eu bibendum. In pulvinar, lacus vitae cras amet.\r\n" + 
				"");
		request.setParameter("operazione", "inviaAnnuncio");
		servlet.doPost(request, response);
		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GM_9_3_InvioAnnunci() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		final String fileName = "test.txt";
		final byte[] content = "Hallo Word".getBytes();
		MockMultipartFile mockMultipartFile = new MockMultipartFile("content", fileName, "image/jpeg", content);
		
		request.addFile(mockMultipartFile);
		
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", titolo);
		request.setParameter("testo", testo);
		request.setParameter("operazione", "inviaAnnuncio");
		servlet.doPost(request, response);
		if (request.getAttribute("erroreCaricamento") != null) {
			assertEquals(request.getAttribute("erroreCaricamento"), true);
		} else {
			fail("il caricamento è andato a buon fine");
		}
	}
	
	@Test
	void TC_GM_9_4_InvioAnnunci() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		Part dimensioneErrata = null;

		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", titolo);
		request.setParameter("testo", testo);
		request.setParameter("operazione", "inviaAnnuncio");
		servlet.doPost(request, response);
		if (request.getAttribute("erroreCaricamento") != null) {
			assertEquals(request.getAttribute("erroreCaricamento"), true);
		} else {
			fail("il caricamento è andato a buon fine");
		}
	}
	
	
}
