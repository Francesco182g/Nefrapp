package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Paziente;
import bean.Utente;
import control.GestionePaziente;
import model.DriverConnection;
import utility.CriptazioneUtility;

class GestionePazienteTest {
	private GestionePaziente servlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private static Utente paziente;
	private static final LocalDate dataNascitaPaziente = LocalDate.parse("1965-12-30");
	private static final String residenzaPaziente = "Via Roma, 22, Salerno, 84132, SA";
	private static final ArrayList<String> medici = new ArrayList<String>();
	private MockHttpSession session;
	private RequestDispatcher dispatcher;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		paziente = new Paziente("M", "BNCDNC67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {	}

	@BeforeEach
	void setUp() throws Exception {
		servlet = new GestionePaziente();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session  = new  MockHttpSession();
		session.setAttribute("utente", paziente);
		request.setSession(session);
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		String password = CriptazioneUtility.criptaConMD5("Pluto12");
		Document document = new Document("CodiceFiscale", "BNCDNC67A01F205I").append("Password", password);
		paziente.insertOne(document);
	}

	@AfterEach
	void tearDown() throws Exception {
		session.invalidate();
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", "BNCDNC67A01F205I");
		paziente.deleteOne(document);
	}

	@Test
	void testVisualizzaProfilo() throws ServletException, IOException {
		request.setParameter("operazione", "visualizzaProfilo");
		servlet.doGet(request, response);
		assertEquals("./profilo.jsp", response.getForwardedUrl());
	}
	
	@Test
	void testDisattivaAccount() throws ServletException, IOException {
		request.setParameter("operazione", "disattivaAccount");
		servlet.doGet(request, response);
		assertEquals("./login.jsp?notifica=accountDisattivato",response.getRedirectedUrl());	
	}
	
	@Test
	void TC_GP_4_1_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "A");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_2_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "AndreaAndreaAndreaAndreaAndreaAndreaAndrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_3_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andr34");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_4_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","R");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_5_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","RossiRossiRossiRossiRossiRossiRossiRossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_6_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Ro55i");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_7_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_8_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/65");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_9_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/19965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_10_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_11_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Sa");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_12_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Saaaaaaaaaaalllllllllllleeeeeeeeeeeerrrrrrrrnnnnnnoooooo");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_13_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Sal3erno0");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_14_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","f");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_15_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","alfredorossitrentadicembremillenovecentosessantacinque@libero.it");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_16_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","12libero.it");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_17_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Sa");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_18_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Saaaaaaaaaaallllllllllllllleeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrnnooooooooo");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_19_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Sal3rn0, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_20_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","pass");
		request.setParameter("confermaPsw","pass");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_21_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","PlutoTopolinoPippo122");
		request.setParameter("confermaPsw","PlutoTopolinoPippo122");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_22_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Plut/$11");
		request.setParameter("confermaPsw","Plut/$11");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_23_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto104");
		
		servlet.doGet(request, response);
		
		assertEquals("./ModificaAccountPazienteView.jsp?notifica=ParamErr", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_4_24_ModificaAccountPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "modificaAccount");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("nome", "Andrea");
		request.setParameter("cognome","Rossi");
		request.setParameter("sesso","M");
		request.setParameter("dataDiNascita","30/12/1965");
		request.setParameter("luogoDiNascita","Salerno");
		request.setParameter("email","");
		request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
		request.setParameter("password","Pluto12");
		request.setParameter("confermaPsw","Pluto12");
		
		servlet.doGet(request, response);
		
		assertEquals("./profilo.jsp?notifica=modificaEffettuata", response.getRedirectedUrl());
	}
	
}
