package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
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

import bean.Medico;
import bean.Utente;
import bean.Amministratore;
import control.GestioneRegistrazione;
import model.DriverConnection;
import model.PazienteModel;

class GestioneRegistrazioneTest {

  private GestioneRegistrazione servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockHttpSession session;
  private static Utente medico;
  private static Utente amministratore;

  @BeforeEach
  void setUpBeforeClass() throws Exception {
    medico = new Medico("M", "Via Roma, 22, Scafati, 80030, NA", LocalDate.parse("1967-07-11"), 
        "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
    
    amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "nico.sansone@live.it");
    
    ArrayList<String> medici = new ArrayList<String>(Arrays.asList("SPSGPP67S09I483T"));

    MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");

    Document doc = new Document("CodiceFiscale", "RSSGPP67S09I483T")
        .append("Nome", "Giuseppe")
        .append("Cognome", "Russo")
        .append("Password", "BellaPassword12")
        .append("DataDiNascita", LocalDate.parse("1967-09-11"))
        .append("Sesso", "M")
        .append("Residenza", "Via Italia, 22, Battipaglia, 84091, SA")
        .append("LuogoDiNascita", "Scafati")
        .append("Email", "G.Russo67@gmail.com")
        .append("Attivo", true)
        .append("Medici", medici);
    paziente.insertOne(doc);
    
    MongoCollection<Document> medicoo = DriverConnection.getConnection().getCollection("Medico");

    doc = new Document("CodiceFiscale", "XXMBNN67L11B519R")
        .append("Nome", "Geremia")
        .append("Cognome", "Bernini")
        .append("Password", "Quadri1234")
        .append("DataDiNascita", LocalDate.parse("1967-09-11"))
        .append("Sesso", "M")
        .append("Residenza", "Via Italia, 22, Battipaglia, 84091, SA")
        .append("LuogoDiNascita", "Scafati")
        .append("Email", "G.Bernini67@gmail.com");
    medicoo.insertOne(doc);	

  }

  @BeforeEach
  void setUp() throws Exception {
    servlet = new GestioneRegistrazione();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session  = new  MockHttpSession();
    session.setAttribute("utente", medico);
    request.setSession(session);
  }

  @AfterEach
  void tearDown() throws Exception {
    //eliminazione account registrato
    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
    MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
    
    BasicDBObject document = new BasicDBObject();

    document.put("CodiceFiscale", "RSSGPP67S09I483T");
    pazienti.deleteOne(document);

    document.put("CodiceFiscale", "RSSGPP79E16I483P");
    pazienti.deleteOne(document);
    
    document.put("CodiceFiscale", "GRMBNN67L11B519R");
    medici.deleteOne(document);
    
    document.put("CodiceFiscale", "XXMBNN67L11B519R");
    medici.deleteOne(document);
  }

  @Test
  void testAccessoSenzaOperazione() throws ServletException, IOException {
    servlet.doGet(request, response);
    assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
  }

  @Test
  void TC_GM_10_1_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483");
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_2_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483PP");
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_3_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483");
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_4_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP67S09I483T"); //CF gi� esistente
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_5_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "G");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_6_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe Antonio Francesco Gianmarco");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_7_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe9");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_8_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "R");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_9_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo Francesco Marcantonio Maria");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_10_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo*");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_11_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_12_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/199");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_13_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/19799");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_14_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "1979/05/16");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_15_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Sc");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    //assertEquals("./dashboard.jsp?regPazOk", response.getRedirectedUrl());
    //assertNotNull(PazienteModel.getPazienteByCF("RSSGPP79E16I483P"));
    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_16_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Citt� di Scafati in provincia di Salerno");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_17_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati*");
    request.setParameter("email", "russo.giuseppe@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_18_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "r@g.a");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_19_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "eccola.questa.e.la.mail.di.giuseppe.russo@outlook.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    //assertNotNull(PazienteModel.getPazienteByCF("RSSGPP79E16I483P"));
    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_20_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russolive.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_21_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Vi");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_22_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Abito a via Napoli numero civico 44 nella citta' di Scafati in provincia di Salerno");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_23_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, SA, 84092");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_24_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "psw");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_25_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "QuestaPasswordLaFaccioDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_26_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "Password Difficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_28_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("/registraPazienteMedico.jsp", response.getForwardedUrl());
  }

  @Test
  void TC_GM_10_29_RegistrazionePazienteDaSeguire() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "No");
    request.setParameter("codiceFiscale", "RSSGPP79E16I483P"); 
    request.setParameter("nome", "Giuseppe");
    request.setParameter("cognome", "Russo");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "16/05/1979");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "giuseppe.russo@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "PasswordDifficile");

    servlet.doGet(request, response);

    assertEquals("./dashboard.jsp?regPazOk", response.getRedirectedUrl());
  }
  
  @Test
  void TestRegistrazioneMedico() throws ServletException, IOException{
    request.setParameter("operazione", "registraMedico");
    request.setParameter("codiceFiscale", "GRMBNN67L11B519R"); 
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome", "Bernini");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "11/07/1967");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "G.Bernini67@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "Quadri1234");
    
    session.setAttribute("utente", amministratore);
    request.setSession(session);

    servlet.doGet(request, response);

    assertEquals("./registraMedico.jsp?notifica=registrato", response.getRedirectedUrl());
  }
  
  @Test
  void TestRegistrazioneMedicoPresente() throws ServletException, IOException{
    request.setParameter("operazione", "registraMedico");
    request.setParameter("codiceFiscale", "XXMBNN67L11B519R"); 
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome", "Bernini");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "11/07/1967");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "G.Bernini67@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "Quadri1234");
    
    session.setAttribute("utente", amministratore);
    request.setSession(session);

    servlet.doGet(request, response);

    assertEquals("./registraMedico.jsp?notifica=presente", response.getRedirectedUrl());
  }
  
  @Test
  void TestRegistrazioneMedicoDatiErrati() throws ServletException, IOException{
    request.setParameter("operazione", "registraMedico");
    request.setParameter("codiceFiscale", "99MBNN67L11B519R"); 
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome", "Bernini");
    request.setParameter("sesso", "M");
    request.setParameter("dataDiNascita", "11/07/1967");
    request.setParameter("luogoDiNascita", "Scafati");
    request.setParameter("email", "G.Bernini67@live.it");
    request.setParameter("residenza", "Via Mazzini, 22, Bellizzi, 84092, SA");
    request.setParameter("password", "Quadri1234");
    
    session.setAttribute("utente", amministratore);
    request.setSession(session);

    servlet.doGet(request, response);

    assertEquals("./registraMedico.jsp?notifica=ParamErr", response.getRedirectedUrl());
  }
  
  @Test
  void TestRegistrazionePazientePresente() throws ServletException, IOException{
    request.setParameter("operazione", "registraPazienteMedico");
    request.setParameter("registrato", "Si");
    request.setParameter("codiceFiscale", "RSSGPP67S09I483T"); 
    servlet.doGet(request, response);
    assertEquals("./dashboard.jsp", response.getRedirectedUrl());
  }
  
  
}