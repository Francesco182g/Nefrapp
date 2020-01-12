package test.control;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import bean.Medico;
import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.MessaggioProxy;
import bean.Paziente;
import control.GestioneMessaggi;
import model.DriverConnection;
import utility.CreaBeanUtility;
import utility.CriptazioneUtility;

class TestGestioneMessaggi {
	private static Paziente paziente;
	private static Medico medico;
	private static Messaggio daPazAMed;
	private static Messaggio daMedAPaz;
	private static String CfPaziente = "BNCLRD67A01F205I";
	private static String CfMedico = "GRMBNN67L11B516R";
	private String oggetto = "Cambio data appuntamento";
	private String testo = "Gentile signor Alfredo,\n" + 
			"Le comunico che a causa di impegni personali non potrò riceverla domani; \n" + 
			"le invio in allegato una tabella con i giorni e gli orari a cui possiamo rimandare l’appuntamento.";
	private Part allegato;
	//TODO: mettere allegato (in forma accettabile) nel part

	private GestioneMessaggi servlet;
	
	
	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		String password1 = CriptazioneUtility.criptaConMD5("Fiori5678");
		Document doc1 = new Document("CodiceFiscale", CfPaziente).append("Password", password1).append("Attivo", true);
		pazienti.insertOne(doc1);
		paziente = CreaBeanUtility.daDocumentAPaziente(doc1);
		
		
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
		servlet = new GestioneMessaggi();
		request = new MockMultipartHttpServletRequest();
		response = new MockHttpServletResponse();
		
		daPazAMed = costruisciMessaggio(paziente.getCodiceFiscale(), medico.getCodiceFiscale());
		daMedAPaz = costruisciMessaggio(medico.getCodiceFiscale(), paziente.getCodiceFiscale());
	}

	@AfterEach
	void tearDown() throws Exception {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		
		FindIterable<Document> messaggioDoc = messaggi.find(eq("MittenteCodiceFiscale", paziente.getCodiceFiscale()))
				.projection(Projections.include("_id"));
		for (Document d : messaggioDoc) {
			messaggi.deleteOne(d);
		}

		FindIterable <Document> messaggioDoc2 = messaggi.find(eq("MittenteCodiceFiscale", medico.getCodiceFiscale()))
				.projection(Projections.include("_id"));
		for (Document d : messaggioDoc2) {
			messaggi.deleteOne(d);
		}
		
		daPazAMed = null;
		daMedAPaz = null;
		
		request.getSession().invalidate();
	}

	@Test
	void testMessaggioSenzaOperazione() throws ServletException, IOException {
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
	}

	@Test
	void TC_GM_8_1_InvioMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);

		//request.addPart(allegato);
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat. E bla e blabla e blablabla.");
		request.setParameter("testo", testo);
		request.setParameter("selectPaziente", CfPaziente);
		request.setParameter("operazione", "inviaMessaggio");
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
	}

//	@Test
//	void TC_GM_8_2_InvioMessaggi() throws ServletException, IOException {
//		request.getSession().setAttribute("utente", medico);
//		request.getSession().setAttribute("isMedico", true);
//
//		//request.addPart(allegato);
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sollicitudin nisi sit amet nibh congue scelerisque. Donec ornare pharetra erat, at lobortis tellus commodo eu. Integer gravida nulla non risus aliquam, elementum mollis turpis fringilla. Sed vel commodo ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum viverra diam fermentum sapien cursus scelerisque. Praesent porta ac diam eu tempus. Pellentesque pellentesque nisi enim, non pellentesque mauris maximus nec. Nunc et enim eu purus porta molestie eget sed libero. Donec ullamcorper ligula orci, eu molestie lorem pharetra at. Nulla tortor tellus, varius sagittis congue quis, lobortis et metus. Sed elementum varius justo, et sollicitudin lectus porttitor at.\n" + 
//				"Integer porta diam nec commodo consequat. Pellentesque pharetra vel lacus nec condimentum. Vivamus metus tortor, mattis non pulvinar in, mollis quis nibh. Cras ultrices vel orci eu bibendum. In pulvinar, lacus vitae cras amet.\n" + 
//				"");
//		request.setParameter("selectPaziente", CfPaziente);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);
//		  
//		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
//	}
	
//	@Test
//	void TC_GM_8_3_InvioMessaggi() throws ServletException, IOException {
//		
//		request.getSession().setAttribute("utente", medico);
//		request.getSession().setAttribute("isMedico", true);
//
//		final String fileName = "test.txt";
//		final byte[] content = "Hallo Word".getBytes();
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("content", fileName, "image/jpeg", content);
//		
//		request.addFile(mockMultipartFile);
//		
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", testo);
//		request.setParameter("selectPaziente", CfPaziente);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);		 
//		
//		if (request.getAttribute("erroreCaricamento") != null) {
//			assertEquals(request.getAttribute("erroreCaricamento"), true);
//		} else {
//			fail("il caricamento è andato a buon fine");
//		}
//		  
//	}
//	
//	@Test
//	void TC_GM_8_4_InvioMessaggi() throws ServletException, IOException {
//		request.getSession().setAttribute("utente", medico);
//		request.getSession().setAttribute("isMedico", true);
//
//		Part dimensioneErrata = null;
//		//request.addPart(dimensioneErrata);
//		//TODO caricare file nel part
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", testo);
//		request.setParameter("selectPaziente", CfPaziente);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);		 
//		
//		if (request.getAttribute("erroreCaricamento") != null) {
//			assertEquals(request.getAttribute("erroreCaricamento"), true);
//		} else {
//			fail("il caricamento è andato a buon fine");
//		}
//		  
//	}
//	
	@Test
	void TC_GM_8_5_InvioMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
//		
//		final String fileName = "test.jpg";
//		final byte[] content = "Hallo Wordsdkjnfkdsjfndskjfsndkjsndkfjdnfkdsjnf".getBytes();
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, "image/jpeg", content);
//		
//		
//		request.addFile(mockMultipartFile);
//		if (request.getPart("file") == null)
//			System.out.println("nullo");
		
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", oggetto);
		request.setParameter("testo", testo);
		request.setParameter("selectPaziente", CfPaziente);
		request.setParameter("operazione", "inviaMessaggio");
		servlet.doPost(request, response);
		 
		if (request.getAttribute("erroreCaricamento") == null) {
			assertEquals("./dashboard.jsp?notifica=messaggioInviato", response.getRedirectedUrl());
		} else {
			fail("caricamento file non riuscito");
		}
	}
	
	
	@Test
	void TC_GP_9_1_InvioMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);

		//request.addPart(allegato);
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat. E bla e blabla e blablabla.");
		request.setParameter("testo", testo);
		request.setParameter("selectMedico", CfMedico);
		request.setParameter("operazione", "inviaMessaggio");
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
	}

	@Test
	void TC_GP_9_2_InvioMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);

		//request.addPart(allegato);
		request.setParameter("operazione", "caricaAllegato");
		servlet.doPost(request, response);
		
		request.setParameter("oggetto", oggetto);
		request.setParameter("testo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sollicitudin nisi sit amet nibh congue scelerisque. Donec ornare pharetra erat, at lobortis tellus commodo eu. Integer gravida nulla non risus aliquam, elementum mollis turpis fringilla. Sed vel commodo ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum viverra diam fermentum sapien cursus scelerisque. Praesent porta ac diam eu tempus. Pellentesque pellentesque nisi enim, non pellentesque mauris maximus nec. Nunc et enim eu purus porta molestie eget sed libero. Donec ullamcorper ligula orci, eu molestie lorem pharetra at. Nulla tortor tellus, varius sagittis congue quis, lobortis et metus. Sed elementum varius justo, et sollicitudin lectus porttitor at.\n" + 
				"Integer porta diam nec commodo consequat. Pellentesque pharetra vel lacus nec condimentum. Vivamus metus tortor, mattis non pulvinar in, mollis quis nibh. Cras ultrices vel orci eu bibendum. In pulvinar, lacus vitae cras amet.\n" + 
				"");
		request.setParameter("selectMedico", CfMedico);
		request.setParameter("operazione", "inviaMessaggio");
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
	}
	
//	@Test
//	void TC_GP_9_3_InvioMessaggi() throws ServletException, IOException {
//		
//		request.getSession().setAttribute("utente", paziente);
//		request.getSession().setAttribute("isPaziente", true);
//
//		Part estensioneErrata = null;
//		//TODO caricare file nel part
//		//request.addPart(estensioneErrata);
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", testo);
//		request.setParameter("selectMedico", CfMedico);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);		 
//		
//		if (request.getAttribute("erroreCaricamento") != null) {
//			assertEquals(request.getAttribute("erroreCaricamento"), true);
//		} else {
//			fail("il caricamento è andato a buon fine");
//		}
//		  
//	}
//	
//	@Test
//	void TC_GP_9_4_InvioMessaggi() throws ServletException, IOException {
//		request.getSession().setAttribute("utente", paziente);
//		request.getSession().setAttribute("isPaziente", true);
//
//		Part dimensioneErrata = null;
//		//request.addPart(dimensioneErrata);
//		//TODO caricare file nel part
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", testo);
//		request.setParameter("selectMedico", CfMedico);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);		 
//		
//		if (request.getAttribute("erroreCaricamento") != null) {
//			assertEquals(request.getAttribute("erroreCaricamento"), true);
//		} else {
//			fail("il caricamento è andato a buon fine");
//		}
//		  
//	}
//	
//	@Test
//	void TC_GP_9_5_InvioMessaggi() throws ServletException, IOException {
//		request.getSession().setAttribute("utente", paziente);
//		request.getSession().setAttribute("isPaziente", true);
//
//		//request.addPart(allegato);
//		request.setParameter("operazione", "caricaAllegato");
//		servlet.doPost(request, response);
//		
//		request.setParameter("oggetto", oggetto);
//		request.setParameter("testo", testo);
//		request.setParameter("selectPaziente", CfPaziente);
//		request.setParameter("operazione", "inviaMessaggio");
//		servlet.doPost(request, response);
//		  
//		assertEquals("./dashboard.jsp?notifica=messaggioInviato", response.getRedirectedUrl());
//	}
	
	@Test
	void TC_GM_7_RicezioneSingoloMessaggio() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		
		request.setParameter("idMessaggio", daPazAMed.getIdMessaggio());
		request.setParameter("operazione", "visualizzaMessaggio");
		
		servlet.doGet(request, response);
		
		//ci si aspetta che il messaggio ottenuto dalla servlet sia visualizzato
		daPazAMed.setVisualizzato(true);

		assertEquals(daPazAMed.toString(), request.getAttribute("messaggio").toString());
	}
	
	@Test
	void TC_GM_7_RicezioneMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", medico);
		request.getSession().setAttribute("isMedico", true);
		request.getSession().setAttribute("accessDone", true);
		
		ArrayList<MessaggioProxy> messaggi = new ArrayList<>();
		Messaggio secondoMessaggio = costruisciMessaggio(paziente.getCodiceFiscale(), medico.getCodiceFiscale());
		
		MessaggioProxy primo = new MessaggioProxy(daPazAMed.getCodiceFiscaleMittente(), daPazAMed.getOggetto(),
				daPazAMed.getData(), daPazAMed.getDestinatariView());
		MessaggioProxy secondo = new MessaggioProxy(secondoMessaggio.getCodiceFiscaleMittente(), secondoMessaggio.getOggetto(),
				secondoMessaggio.getData(), secondoMessaggio.getDestinatariView());
		primo.setIdMessaggio(daPazAMed.getIdMessaggio());
		secondo.setIdMessaggio(secondoMessaggio.getIdMessaggio());
		//l'ordine di inserimento va invertito rispetto all'ordine di aggiunta al database
		//perché nel mostrare la lista il model sceglie prima i messaggi più recenti
		messaggi.add(secondo);
		messaggi.add(primo);		
		
		request.setParameter("operazione", "visualizzaElencoMessaggio");
		servlet.doGet(request, response);

		assertEquals(messaggi.toString(), request.getAttribute("messaggio").toString());
	}
	
	@Test
	void TC_GP_10_RicezioneSingoloMessaggio() throws ServletException, IOException {
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);
		
		request.setParameter("idMessaggio", daMedAPaz.getIdMessaggio());
		request.setParameter("operazione", "visualizzaMessaggio");
		
		servlet.doGet(request, response);
		
		//ci si aspetta che il messaggio ottenuto dalla servlet sia visualizzato
		daMedAPaz.setVisualizzato(true);
		
		assertEquals(daMedAPaz.toString(), request.getAttribute("messaggio").toString());
	}
	
	@Test
	void TC_GP_10_RicezioneMessaggi() throws ServletException, IOException {
		request.getSession().setAttribute("utente", paziente);
		request.getSession().setAttribute("isPaziente", true);
		request.getSession().setAttribute("accessDone", true);
		
		ArrayList<MessaggioProxy> messaggi = new ArrayList<>();
		Messaggio secondoMessaggio = costruisciMessaggio(medico.getCodiceFiscale(), paziente.getCodiceFiscale());
		
		MessaggioProxy primo = new MessaggioProxy(daMedAPaz.getCodiceFiscaleMittente(), daMedAPaz.getOggetto(),
				daMedAPaz.getData(), daMedAPaz.getDestinatariView());
		MessaggioProxy secondo = new MessaggioProxy(secondoMessaggio.getCodiceFiscaleMittente(), secondoMessaggio.getOggetto(),
				secondoMessaggio.getData(), secondoMessaggio.getDestinatariView());
		primo.setIdMessaggio(daMedAPaz.getIdMessaggio());
		secondo.setIdMessaggio(secondoMessaggio.getIdMessaggio());
		//l'ordine di inserimento va invertito rispetto all'ordine di aggiunta al database
		//perché nel mostrare la lista il model sceglie prima i messaggi più recenti
		messaggi.add(secondo);
		messaggi.add(primo);		
		
		request.setParameter("operazione", "visualizzaElencoMessaggio");
		servlet.doGet(request, response);

		assertEquals(messaggi.toString(), request.getAttribute("messaggio").toString());
	}
	
	

	//metodo di servizio
	private static Messaggio costruisciMessaggio(String mittente, String destinatario) {
		HashMap<String, Boolean> destinatari = new HashMap<String, Boolean>();
		destinatari.put(destinatario, false);
		Messaggio daAggiungere = new MessaggioCompleto(mittente, "oggetto", "testo", null, null,
				ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatari);
		
		MongoCollection<Document> messaggio = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Document> destinatariView = new ArrayList<Document>();
		Iterator it = daAggiungere.getDestinatariView().entrySet().iterator();
		
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

		Document allegato = new Document("NomeAllegato", daAggiungere.getNomeAllegato()).append("CorpoAllegato",
				daAggiungere.getCorpoAllegato());

		Document doc = new Document("MittenteCodiceFiscale", daAggiungere.getCodiceFiscaleMittente())
				.append("Oggetto", daAggiungere.getOggetto()).append("Testo", daAggiungere.getTesto())
				.append("Allegato", allegato).append("Data", daAggiungere.getData().toInstant())
				.append("DestinatariView", destinatariView);
		messaggio.insertOne(doc);
		
		ObjectId idObj = (ObjectId)doc.get("_id");
		daAggiungere.setIdMessaggio(idObj.toString());
		
		return daAggiungere;
	}

}
