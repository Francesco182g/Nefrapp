package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Amministratore;
import bean.Utente;
import control.GestioneAccesso;
import control.GestioneAmministratore;
import model.DriverConnection;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

class TestGestioneAmministratore {
	private GestioneAmministratore servlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private static Utente admin;
	private MockHttpSession session;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		
		
		
		admin = new Amministratore("FLPBRZ61A45F234F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	
		
	}

	@BeforeEach
	void setUp() throws Exception {
		servlet = new GestioneAmministratore();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session  = new  MockHttpSession();
		session.setAttribute("utente", admin);
		request.setSession(session);
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		String password3 = CriptazioneUtility.criptaConMD5("Pippo1234");
		Document doc3 = new Document("CodiceFiscale", "FLPBRZ61A45F234F").append("Password", password3);
		amministratore.insertOne(doc3);
		
		MongoCollection<Document> medico = DriverConnection.getConnection().getCollection("Medico");
		String password = CriptazioneUtility.criptaConMD5("Quadri1234");
		Document doc = new Document("CodiceFiscale", "GRMBNN67L11B516R").append("Password", password);
		medico.insertOne(doc);

		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		String password2 = CriptazioneUtility.criptaConMD5("Fiori5678");
		Document doc2 = new Document("CodiceFiscale", "BNCDNC67A01F205I").append("Password", password2).append("Attivo", true);
		paziente.insertOne(doc2);
		
		
	}

	@AfterEach
	void tearDown() throws Exception {
		session.invalidate();
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", "FLPBRZ61A45F234F");
		amministratore.deleteOne(document);
		MongoCollection<Document> medico = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject document2 = new BasicDBObject();
		document2.put("CodiceFiscale", "GRMBNN67L11B516R");
		medico.deleteOne(document2);
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		BasicDBObject document3 = new BasicDBObject();
		document3.put("CodiceFiscale", "BNCDNC67A01F205I");
		paziente.deleteOne(document3);
	}

	@Test
	void testAccessoSenzaOperazione() throws ServletException, IOException {
		servlet.doPost(request, response);
		assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_5_1_RimozioneAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "rimuoviAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipo", "paziente");
		
		servlet.doPost(request, response);
		
		assertEquals(null,PazienteModel.getPazienteByCF("BNCDNC67A01F205I"));
	}
	@Test
	void TC_GA_6_1_RimozioneAccountMedico() throws ServletException, IOException {
		request.setParameter("operazione", "rimuoviAccount");
		request.setParameter("codiceFiscale", "GRMBNN67L11B516R");
		request.setParameter("tipo", "medico");
		servlet.doPost(request, response);
		assertEquals(null,PazienteModel.getPazienteByCF("GRMBNN67L11B516R"));
	}
	@Test
	void TestCaricamentoDatiPazientiEMediciInDashboard() throws ServletException, IOException {
		request.setParameter("operazione", "caricaMedPaz");
		servlet.doPost(request, response);
		Gson gg = new Gson();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(MedicoModel.getAllMedici());
		list.add(PazienteModel.getAllPazienti());
		String jsonList=gg.toJson(list);
		System.out.println(response.getContentAsString());
		assertEquals(response.getContentAsString(),jsonList);
	}
	@Test
	void TC_GA_3_1_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pipo");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_2_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "SperoCheQuestaSiaUnaBuonaPassword");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_3_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo12__34");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_4_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1244");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_5_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1234");
		request.setParameter("nuovaPassword", "Pipo");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_6_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1234");
		request.setParameter("nuovaPassword", "SperoCheQuestaSiaUnaBuonaPassword");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_7_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1234");
		request.setParameter("nuovaPassword", "Pippo56__78");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_8_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1234");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5978");
		servlet.doPost(request, response);
		  
		assertEquals("./resetPasswordAmministratoreView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_3_9_ModificaPasswordAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("tipoUtente", "amministratore");
		request.setParameter("vecchiaPassword", "Pippo1234");
		request.setParameter("nuovaPassword", "Pippo5678");
		request.setParameter("confermaPassword", "Pippo5678");
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=ModificaAdmnRiuscita",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_1_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "A");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_2_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "AlfredoAlfredoAlfredoAlfredoAlfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_3_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo34");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_4_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","R");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_5_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","RossiRossiRossiQuantiRossiRossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_6_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi12");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_7_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_8_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/196");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_9_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/19658");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_10_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/lu/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_11_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Eh");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_12_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","NomeDiUnBelLuogoDoveNascereBellioDoveNascereBellioDoveNascereBelli");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_13_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno1");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_14_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","a@g.c");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_15_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","ho.scritto.una.bella.email.che.a.tutti.piace@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_16_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossigmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_17_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Sa");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_18_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Salernosalernosalernosalernosalernosalernosalernosalerno");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_19_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Sal3rn0, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_20_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Plu");
		request.setParameter("confermaPsw","Plu");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_21_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","SperoCheQuestaSiaUnaBuonaPassword");
		request.setParameter("confermaPsw","SperoCheQuestaSiaUnaBuonaPassword");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}

	@Test
	void TC_GA_7_22_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto1__2");
		request.setParameter("confermaPsw","Pluto1__2");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_23_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto1");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_7_24_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","BNCDNC67A01F205I");
		request.setParameter("tipoUtente", "paziente");
		request.setParameter("nome", "Alfredo");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","A.Rossi16@gmail.com");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=ModificaPazRiuscita",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_1_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "G");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_2_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Giiiiiiaaaaaaaaaaaaaaaaaaaaaaanniiiiiiii");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_3_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni&%$");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_4_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","B");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_5_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brrrrrrrrrrrrrrraaaaaaaaazzzzzzzzooooooooooooooooooooo");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_6_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Bra%&z$of");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_7_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_8_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/52");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_9_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/199522");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_10_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_11_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Mi");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_12_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Mmmmmmmmmmmmmmmmmmmmiiiiiiiilllllaaaaaanooooooooooo");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_13_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Mila$$no");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_14_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_15_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","gianni.brazofmailmiglioredisemprecomepuddipuddi@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_16_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","g.brazo$&&f@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_17_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","g.brazof@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_18_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Sa");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_19_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Sssssssssssssaaaaaaaaaaaaaaaaaaaaaaaalllllllllllllllllllllllllllllllllllleeeeeeeeeeeeeerrrrrrrrrrnnnnnnnnnnnooooo");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_20_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Saler&&no");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=ParamErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_21_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","g");
		request.setParameter("confermaPsw","g");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_22_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","giiiiiiiiiiiiiiiiiiiiaaaaaaaaaannnnnnniiiiii");
		request.setParameter("confermaPsw","giiiiiiiiiiiiiiiiiiiiaaaaaaaaaannnnnnniiiiii");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_23_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gian%$n/&i");
		request.setParameter("confermaPsw","gian%$n/&i");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_24_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","giammi");
		
		servlet.doPost(request, response);
		  
		assertEquals("./ModificaAccountMedicoView.jsp?notifica=PassErr",response.getRedirectedUrl());
	}
	@Test
	void TC_GA_8_25_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modifica");
		request.setParameter("codiceFiscale","GRMBNN67L11B516R");
		request.setParameter("tipoUtente", "medico");
		request.setParameter("nome", "Gianni");
		request.setParameter("cognome","Brazof");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","10/10/1952");
		request.setParameter("luogoDiNascita","Milano");
		request.setParameter("email","b.gianni@gmail.com");
		request.setParameter("residenza","Via Rafastia, 22, Salerno, 84132, SA");
		request.setParameter("password","gianni");
		request.setParameter("confermaPsw","gianni");
		
		servlet.doPost(request, response);
		  
		assertEquals("./dashboard.jsp?notifica=ModificaMedRiuscita",response.getRedirectedUrl());
	}
}
