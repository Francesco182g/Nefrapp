package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.springframework.mock.web.MockHttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Paziente;
import bean.SchedaParametri;
import bean.Utente;
import control.GestioneParametri;
import model.DriverConnection;
import model.SchedaParametriModel;
import utility.CriptazioneUtility;

class GestioneParametriTest {
  private GestioneParametri servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private static Utente paziente;
  private static SchedaParametri schedaParametri;
  private MockHttpSession session;
  private static final LocalDate dataNascitaPaziente = LocalDate.parse("1965-12-30");
  private static final String residenzaPaziente = "Via Roma, 22, Salerno, 84132, SA";
  private static final ArrayList<String> medici = new ArrayList<String>();

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    paziente = new Paziente("M", "BNCDNC67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
    schedaParametri = new SchedaParametri("BNCDNC67A01F205I", new BigDecimal(75), 120, 80, 1550, 700, 1, 1000, 1500,LocalDate.now());
  }

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		MongoCollection<Document> schedaParametri = DriverConnection.getConnection().getCollection("SchedaParametri");
		BasicDBObject document2 = new BasicDBObject();
		document2.put("PazienteCodiceFiscale", "BNCDNC67A01F205I");
		schedaParametri.deleteOne(document2);
	}

  @BeforeEach
  void setUp() throws Exception {
    servlet = new GestioneParametri();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session  = new  MockHttpSession();
    session.setAttribute("utente", paziente);
    request.setSession(session);
    MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
    String password = CriptazioneUtility.criptaConMD5("Pluto12");
    Document document = 
        new Document("CodiceFiscale", "BNCDNC67A01F205I").append("Password", password);
    paziente.insertOne(document);

    MongoCollection<Document> scheda = 
        DriverConnection.getConnection().getCollection("SchedaParametri");

    Document doc = new Document("PazienteCodiceFiscale", schedaParametri.getPazienteCodiceFiscale())
        .append("Peso", schedaParametri.getPeso())
        .append("PaMin", schedaParametri.getPaMin())
        .append("PaMax", schedaParametri.getPaMax())
        .append("ScaricoIniziale", schedaParametri.getScaricoIniziale())
        .append("UF", schedaParametri.getUF())
        .append("TempoSosta", schedaParametri.getTempoSosta())
        .append("Carico", schedaParametri.getCarico())
        .append("Scarico", schedaParametri.getScarico())
        .append("Data", schedaParametri.getData());
    scheda.insertOne(doc);	
  }

  @AfterEach
  void tearDown() throws Exception {
    session.invalidate();
    MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
    BasicDBObject document = new BasicDBObject();
    document.put("CodiceFiscale", "BNCDNC67A01F205I");
    paziente.deleteOne(document);
    MongoCollection<Document> schedaParametri = 
        DriverConnection.getConnection().getCollection("SchedaParametri");
    BasicDBObject document2 = new BasicDBObject();
    document2.put("PazienteCodiceFiscale", "BNCDNC67A01F205I");
    schedaParametri.deleteOne(document2);
  }

  @Test
  void testAccessoSenzaOperazione() throws ServletException, IOException {
    servlet.doPost(request, response);
    assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_1_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "40g");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_2_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "10");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_3_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "999");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_4_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "1e3");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_5_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "10");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_6_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "999");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_7_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "1e3");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_8_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "39");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_9_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "131");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_10_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1,5lt");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_11_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "-1001");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_12_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "3001");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_13_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "12e4");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TC_GP_6_14_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "-1001");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_15_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "1501");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_16_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1,5");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_17_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "0");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_18_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "25");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_19_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "11lt");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_20_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "0");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_21_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "3501");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_22_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "15lt");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_23_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "499");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_24_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "3001");
    servlet.doPost(request, response);

    assertEquals("./inserimentoParametriView.jsp?notifica=schedaNonInserita", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_6_25_InserimentoParametriPersonali() throws ServletException, IOException {
    request.setParameter("operazione", "inserisciScheda");
    request.setParameter("Peso", "75");
    request.setParameter("PaMax", "120");
    request.setParameter("PaMin", "80");
    request.setParameter("ScaricoIniziale", "1550");
    request.setParameter("UF", "700");
    request.setParameter("TempoSosta", "1");
    request.setParameter("Scarico", "1000");
    request.setParameter("Carico", "1500");
    servlet.doPost(request, response);

    assertEquals("./parametri?operazione=visualizzaScheda&notifica=schedaInserita", response.getRedirectedUrl());
  }
  
  @Test
  void TestDownloadReport() throws ServletException, IOException {
    request.setParameter("operazione", "download");
    request.setParameter("dataInizio", "10/10/2010");
    request.setParameter("dataFine", "10/10/2030");
    request.setParameter("CFPaziente", "BNCDNC67A01F205I");

    servlet.doPost(request, response);

    assertEquals("application/vnd.ms-excel", response.getContentType());
  }
  
  @Test
  void TestVisualizzaSchedaParametriDaMedico() throws ServletException, IOException {
    request.setParameter("operazione", "visualizzaScheda");
    request.setParameter("CFPaziente", paziente.getCodiceFiscale());
    session.setAttribute("isMedico", true);
    request.setSession(session);
    servlet.doPost(request, response);
    System.out.println(request.getAttribute("schedaParametri"));
    assertEquals(request.getAttribute("schedaParametri").toString(), SchedaParametriModel.getSchedeParametriByCF(paziente.getCodiceFiscale()).toString());
  }
  
  @Test
  void TestVisualizzaSchedaParametriDaPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "visualizzaScheda");
    session.setAttribute("isPaziente", true);
    request.setSession(session);
    servlet.doPost(request, response);
    System.out.println(request.getAttribute("schedaParametri"));
    assertEquals(request.getAttribute("schedaParametri").toString(), SchedaParametriModel.getSchedeParametriByCF(paziente.getCodiceFiscale()).toString());
  }
}
