package test.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bean.Medico;
import bean.Paziente;
import bean.Utente;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import control.GestioneMedico;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import model.DriverConnection;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import utility.CriptazioneUtility;

class GestioneMedicoTest {
  private GestioneMedico servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private static Utente medico;
  private static Utente paziente;
  private static final LocalDate dataNascitaMedico = LocalDate.parse("1969-07-24");
  private static final String residenzaMedico = "Via Roma, 22, Salerno, 84132, SA";
  private static final LocalDate dataNascitaPaziente = LocalDate.parse("1965-12-30");
  private static final String residenzaPaziente = "Via Roma, 22, Salerno, 84132, SA";
  private static final ArrayList<String> medici = new ArrayList<String>();
  private MockHttpSession session;
  private RequestDispatcher dispatcher;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    medico = new Medico("M", residenzaMedico, dataNascitaMedico, "GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
    medici.add("GRMBNN67L11B519R");
    paziente = new Paziente("M", "BNCDNC67A01F205I", "Andrea", "Rossi", "", residenzaPaziente, "Salerno", dataNascitaPaziente, false, medici);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }

  @BeforeEach
  void setUp() throws Exception {
    servlet = new GestioneMedico();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session  = new  MockHttpSession();
    session.setAttribute("utente", medico);
    request.setSession(session);

    MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
    String password = CriptazioneUtility.criptaConMD5("Quadri1234");
    Document document = 
        new Document("CodiceFiscale", "GRMBNN67L11B519R").append("Password", password);
    medici.insertOne(document);

    ArrayList<String> mediciCF = new ArrayList<>();
    mediciCF.add(medico.getCodiceFiscale());
    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
    password = CriptazioneUtility.criptaConMD5("Pluto12");
    document = 
        new Document("CodiceFiscale", "BNCDNC67A01F205I").append("Password", password).append("Medici", mediciCF);
    pazienti.insertOne(document);
  }

  @AfterEach
  void tearDown() throws Exception {
    session.invalidate();

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
  void testVisualizzaProfilo() throws ServletException, IOException {
    request.setParameter("operazione", "visualizzaProfilo");
    servlet.doGet(request, response);
    assertEquals("./profilo.jsp", response.getForwardedUrl());
  }

  @Test
  void testEliminaAccountNonPossibile() throws ServletException, IOException {
    request.setParameter("operazione", "eliminaAccount");
    servlet.doGet(request, response);
    assertEquals("./login.jsp?notifica=accountDisattivato", response.getRedirectedUrl());
  }

  @Test
  void testEliminaAccount() throws ServletException, IOException {
    BasicDBObject document;
    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
    document = new BasicDBObject();
    document.put("CodiceFiscale", "BNCDNC67A01F205I");
    pazienti.deleteOne(document);

    request.setParameter("operazione", "eliminaAccount");
    servlet.doGet(request, response);
    assertEquals("./login.jsp?notifica=accountDisattivato", response.getRedirectedUrl());
    assertEquals(session.getAttribute("accessDone"), false);
  }


  @Test
  void testVisualizzaPazientiSeguiti() throws ServletException, IOException {
    request.setParameter("operazione", "VisualizzaPazientiSeguiti");
    servlet.doGet(request, response);
    ArrayList<Paziente> seguiti = new ArrayList<>();
    ArrayList<Paziente> seguitiRequest = new ArrayList<>();
    seguitiRequest = (ArrayList<Paziente>) request.getAttribute("pazientiSeguiti");

    seguiti.add((Paziente) paziente);
    assertEquals(seguiti.get(0).getCodiceFiscale(), seguitiRequest.get(0).getCodiceFiscale());
  }

  @Test
  void testVisualizzaPazientiSeguitiAsincrona() throws ServletException, IOException {
    request.setParameter("operazione", "VisualizzaPazientiSeguiti");
    request.setParameter("tipo", "asincrona");
    servlet.doGet(request, response);

    PrintWriter pw = response.getWriter();
    assertNotNull(pw);
  }

  @Test
  void testOperazioneNonValida() throws ServletException, IOException {
    request.setParameter("operazione", "nessuna");
    servlet.doGet(request, response);
    assertEquals("/paginaErrore.jsp", response.getForwardedUrl());
  }

  @Test
  void testModificaProfiloMedico() throws ServletException, IOException {
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN67L11B519R");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Bernini@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals("./profilo.jsp?notifica=modificaEffettuata", response.getRedirectedUrl());
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMB60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_2() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremiaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa34345");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_3() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Berniniiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii2342344324iiiiiiii");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_4() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","Maschio");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_5() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","2427-0709-1969983");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_6() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "R0M4");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_7() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_8() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_9() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadr");
    request.setParameter("confermaPsw", "Quadri1234");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testModificaProfiloMedicoDatiInvalidi_10() throws ServletException, IOException {
    medico.setCodiceFiscale("GRMBNN60L11B519Q");
    session.setAttribute("utente", medico);
    request.setParameter("operazione", "modifica");
    request.setParameter("codiceFiscale", "GRMBNN60L11B519Q");
    request.setParameter("nome", "Geremia");
    request.setParameter("cognome","Bernini");
    request.setParameter("sesso","M");
    request.setParameter("dataDiNascita","24/07/1969");
    request.setParameter("luogoDiNascita", "Salerno");
    request.setParameter("email","G.Berninetti@gmail.com");
    request.setParameter("residenza","Via Roma, 22, Salerno, 84132, SA");
    request.setParameter("password", "Quadri1234");
    request.setParameter("confermaPsw", "Quadri123");

    servlet.doPost(request, response);

    assertEquals(request.getAttribute("notifica"), "Uno o piu' parametri del medico non sono validi.");
  }

  @Test
  void testNessunUtente() throws ServletException, IOException {
    request.getSession().setAttribute("utente", null);
    request.setSession(session);
    servlet.doGet(request, response);
    assertEquals("/paginaErrore.jsp", response.getForwardedUrl());
  }
}