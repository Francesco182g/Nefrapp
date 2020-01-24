package test.control;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
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

import control.GestioneAccesso;
import model.DriverConnection;
import utility.CriptazioneUtility;

class GestioneAccessoTest {

  private GestioneAccesso servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    MongoCollection<Document> amministratore = 
        DriverConnection.getConnection().getCollection("Amministratore");
    String password = CriptazioneUtility.criptaConMD5("Pippo1234");
    Document doc = new Document("CodiceFiscale", "FLPBRZ61A45F234F").append("Password", password);
    amministratore.insertOne(doc);

    MongoCollection<Document> paziente1 = 
        DriverConnection.getConnection().getCollection("Paziente");
    String password2 = CriptazioneUtility.criptaConMD5("Fiori5678");
    Document doc2 = new Document("CodiceFiscale", "BNCLRD67A01F205I").append("Password", password2).append("Attivo", true);
    paziente1.insertOne(doc2);

    String password3 = CriptazioneUtility.criptaConMD5("password");
    Document doc3 = new Document("CodiceFiscale", "CFFSRA90A50A091Q").append("Password", password3).append("Attivo", false);
    paziente1.insertOne(doc3);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
    BasicDBObject document = new BasicDBObject();
    document.put("CodiceFiscale", "BNCLRD67A01F205I");
    pazienti.deleteOne(document);
    BasicDBObject document2 = new BasicDBObject();
    document2.put("CodiceFiscale", "CFFSRA90A50A091Q");
    pazienti.deleteOne(document2);

    MongoCollection<Document> amministratore = 
        DriverConnection.getConnection().getCollection("Amministratore");
    BasicDBObject document3 = new BasicDBObject();
    document3.put("CodiceFiscale", "FLPBRZ61A45F234F");
    amministratore.deleteOne(document3);
    pazienti.deleteOne(document2);
  }

  @BeforeEach
  void setUp() throws Exception {
    servlet = new GestioneAccesso();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @AfterEach
  void tearDown() throws Exception {
  }
  
  @Test
  void testAccessoSenzaOperazione() throws ServletException, IOException {
    servlet.doGet(request, response);
    assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_1_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F254F");
    request.setParameter("password", "Pippo1234");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_2_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FF123FA45FF23334F");
    request.setParameter("password", "Pippo1234");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_3_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FF123FA45FF23*4F");
    request.setParameter("password", "Pippo1234");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_4_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F254F");
    request.setParameter("password", "Pippo1234");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_5_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
    request.setParameter("password", "Pipo");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_6_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
    request.setParameter("password", "SperoCheQuestaSiaUnaBuonaPassword");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_7_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
    request.setParameter("password", "Pippo12__90");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_8_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
    request.setParameter("password", "Pippo1290");
    servlet.doGet(request, response);

    assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_1_9_LoginAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "loginAdmin");
    request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
    request.setParameter("password", "Pippo1234");
    servlet.doGet(request, response);

    assertEquals("./dashboard.jsp", response.getRedirectedUrl());
  }

  @Test
  void TC_GA_2_1_LogoutAmministratore() throws ServletException, IOException {
    request.setParameter("operazione", "logout");
    servlet.doGet(request, response);

    assertEquals("./dashboard.jsp", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_1_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205");
    request.setParameter("password", "Fiori5678");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_2_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205IFD");
    request.setParameter("password", "Fiori5678");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_3_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "N0TV4L1DF0RM4TM8");
    request.setParameter("password", "Fiori5678");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_4_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCDNC67A02F205I");
    request.setParameter("password", "Fiori5678");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_Gp_1_5_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
    request.setParameter("password", "Ave");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_6_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
    request.setParameter("password", "SperoCheQuestaSiaUnaBuonaPassword");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_7_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
    request.setParameter("password", "Password12__90");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_8_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
    request.setParameter("password", "Fiori5690");
    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
  }

  @Test
  void TC_GP_1_9_LoginPaziente() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
    request.setParameter("password", "Fiori5678");
    servlet.doGet(request, response);

    assertEquals("./dashboard.jsp", response.getRedirectedUrl());
  }

  @Test
  void testLoginPazienteAccountDisattivato() throws ServletException, IOException {
    request.setParameter("operazione", "login");
    request.setParameter("codiceFiscale", "CFFSRA90A50A091Q");
    request.setParameter("password", "password");

    servlet.doGet(request, response);

    assertEquals("./login.jsp?notifica=accountDisattivo", response.getRedirectedUrl());
  }
}
