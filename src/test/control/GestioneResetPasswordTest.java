package test.control;

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
import com.mongodb.client.MongoCollection;

import bean.Medico;
import bean.Paziente;
import bean.Utente;
import control.GestioneResetPassword;
import model.DriverConnection;
import utility.CriptazioneUtility;

class GestioneResetPasswordTest {
	private GestioneResetPassword servlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private static Utente medico;
	private static Utente paziente;
	private static final LocalDate dataNascitaMedico = LocalDate.parse("1969-07-24");
	private static final String residenzaMedico = "Via Roma, 22, Salerno, 84132, SA";
	private static final LocalDate dataNascitaPaziente = LocalDate.parse("1965-12-30");
	private static final String residenzaPaziente = "Via Roma, 22, Salerno, 84132, SA";
	private static final ArrayList<String> medici = new ArrayList<String>();

	  @BeforeAll
	  static void setUpBeforeClass() throws Exception {
	    medico = new Medico("M", residenzaMedico, dataNascitaMedico, "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
	    medici.add("GRMBNN67L11B519R");
	    paziente = new Paziente("M", "BNCDNC67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
	  }

	  @AfterAll
	  static void tearDownAfterClass() throws Exception {	}

	  @BeforeEach
	  void setUp() throws Exception {
	    servlet = new GestioneResetPassword();
	    request = new MockHttpServletRequest();
	    response = new MockHttpServletResponse();
	    
	    MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
	    String password = CriptazioneUtility.criptaConMD5("Quadri1234");
	    Document document = 
	        new Document("CodiceFiscale", "GRMBNN67L11B519R").append("Password", password).append("Email", "G.Bernini67@gmail.com");
	    medici.insertOne(document);
	    
	    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
	    password = CriptazioneUtility.criptaConMD5("Pluto12");
	    document = 
	        new Document("CodiceFiscale", "BNCDNC67A01F205I").append("Password", password);
	    pazienti.insertOne(document);
	  }

	  @AfterEach
	  void tearDown() throws Exception {
	    
	    MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
	    BasicDBObject document = new BasicDBObject();
	    document.put("CodiceFiscale", "GRMBNN67L11B519R");
	    medici.deleteOne(document);
	    
	    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
	    document = new BasicDBObject();
	    document.put("CodiceFiscale", "BNCDNC67A01F205I");
	    pazienti.deleteOne(document);
	  }
	  
	  @Test
	  void testNessunaOperazione() throws ServletException, IOException {
		    request.setParameter("operazione", "");
		    servlet.doGet(request, response);
		    assertEquals("./paginaErrore.jsp?notifica=noOperazione", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testRichiedenteMedico() throws ServletException, IOException {
		    request.setParameter("operazione", "identificaRichiedente");
		    request.setParameter("codiceFiscale", "GRMBNN67L11B519R");
		    servlet.doPost(request, response);
		    assertEquals("./dashboard.jsp?notifica=identificazioneSuccesso", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testRichiedentePaziente() throws ServletException, IOException {
		    request.setParameter("operazione", "identificaRichiedente");
		    request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		    servlet.doGet(request, response);
		    assertEquals("./dashboard.jsp?notifica=identificazioneSuccesso", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testRichiedenteInesistente() throws ServletException, IOException {
		    request.setParameter("operazione", "identificaRichiedente");
		    request.setParameter("codiceFiscale", "BNCCCC67A01F205I");
		    servlet.doGet(request, response);
		    assertEquals("./richiestaResetView.jsp?notifica=CFnonPresente", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testResetMedico() throws ServletException, IOException {
		  request.setParameter("operazione", "reset");
		  request.setParameter("email", "G.Bernini67@gmail.com");
		  request.setParameter("codiceFiscale", "GRMBNN67L11B519R");
		  request.setParameter("password", "Quadri12");
		  request.setParameter("confermaPsw", "Quadri12");
		  servlet.doGet(request, response);
		  assertEquals("./dashboard.jsp?notifica=resetSuccesso", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testResetMedicoEmailErrata() throws ServletException, IOException {
		  request.setParameter("operazione", "reset");
		  request.setParameter("email", "G.Xernini67@gmail.com");
		  request.setParameter("codiceFiscale", "GRMBNN67L11B519R");
		  request.setParameter("password", "Quadri12");
		  request.setParameter("confermaPsw", "Quadri12");
		  servlet.doGet(request, response);
		  assertEquals("./resetPasswordView.jsp?notifica=datiErrati", response.getRedirectedUrl());
	  }
	  
	  @Test
	  void testResetMedicoDatiErrati() throws ServletException, IOException {
		  request.setParameter("operazione", "reset");
		  request.setParameter("email", "G.Xernini67gmail.com");
		  request.setParameter("codiceFiscale", "G9MBNN67L11B519R");
		  request.setParameter("password", "Qu");
		  request.setParameter("confermaPsw", "Quadri12");
		  servlet.doGet(request, response);
		  assertEquals("./paginaErrore.jsp?notifica=datiErrati", response.getRedirectedUrl());
	  }
}