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
import org.bson.internal.Base64;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mongodb.BasicDBObject;
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
import model.MessaggioModel;
import utility.CreaBeanUtility;
import utility.CriptazioneUtility;

@WebAppConfiguration


//NB: mancano casi di test sull'inserimento di allegati.
//L'unico modo che ho trovato per simulare l'inserimento di un file in un multipart form richiede una configurazione Spring completa.
//L'inserimento di allegati sarà testato in testing di sistema.

class GestioneMessaggiTest {	
  private static Paziente paziente;
  private static Medico medico;
  private static Messaggio daPazAMed;
  private static Messaggio daMedAPaz;
  private static String CfPaziente = "BNCLRD67A01F205I";
  private static String CfMedico = "GRMBNN67L11B516R";
  private String oggetto = "Cambio data appuntamento";
  private String testo = "Gentile signor Alfredo,\n" + 
      "Le comunico che a causa di impegni personali non potrò riceverla domani; \n" 
      + "le invio in allegato una tabella con i giorni e gli orari a "
      + "cui possiamo rimandare l’appuntamento.";

  private GestioneMessaggi servlet;
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
    servlet = new GestioneMessaggi();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    daPazAMed = costruisciMessaggio(paziente.getCodiceFiscale(), medico.getCodiceFiscale());
    daMedAPaz = costruisciMessaggio(medico.getCodiceFiscale(), paziente.getCodiceFiscale());
  }

  @AfterEach
  void tearDown() throws Exception {
    MongoCollection<Document> messaggi =
        DriverConnection.getConnection().getCollection("Messaggio");

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

  @Test
  void TC_GM_8_2_InvioMessaggi() throws ServletException, IOException {
    request.getSession().setAttribute("utente", medico);
    request.getSession().setAttribute("isMedico", true);

    //request.addPart(allegato);
    request.setParameter("operazione", "caricaAllegato");
    servlet.doPost(request, response);

    request.setParameter("oggetto", oggetto);
    request.setParameter("testo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sollicitudin nisi sit amet nibh congue scelerisque. Donec ornare pharetra erat, at lobortis tellus commodo eu. Integer gravida nulla non risus aliquam, elementum mollis turpis fringilla. Sed vel commodo ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum viverra diam fermentum sapien cursus scelerisque. Praesent porta ac diam eu tempus. Pellentesque pellentesque nisi enim, non pellentesque mauris maximus nec. Nunc et enim eu purus porta molestie eget sed libero. Donec ullamcorper ligula orci, eu molestie lorem pharetra at. Nulla tortor tellus, varius sagittis congue quis, lobortis et metus. Sed elementum varius justo, et sollicitudin lectus porttitor at.\n" + 
        "Integer porta diam nec commodo consequat. Pellentesque pharetra vel lacus nec condimentum. Vivamus metus tortor, mattis non pulvinar in, mollis quis nibh. Cras ultrices vel orci eu bibendum. In pulvinar, lacus vitae cras amet.\n" + 
        "");
    request.setParameter("selectPaziente", CfPaziente);
    request.setParameter("operazione", "inviaMessaggio");
    servlet.doPost(request, response);

    assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
  }

  @Test
  void TC_GM_8_5_InvioMessaggi() throws ServletException, IOException {
    request.getSession().setAttribute("utente", medico);
    request.getSession().setAttribute("isMedico", true);

    request.setParameter("operazione", "caricaAllegato");
    servlet.doPost(request, response);

    request.setParameter("oggetto", oggetto);
    request.setParameter("testo", testo);
    request.setParameter("selectPaziente", "");
    request.setParameter("operazione", "inviaMessaggio");
    servlet.doPost(request, response);

    assertEquals("./dashboard.jsp?notifica=messaggioInviato", response.getRedirectedUrl());
  }

  @Test
  void TC_GM_8_6_InvioMessaggi() throws ServletException, IOException {
    request.getSession().setAttribute("utente", medico);
    request.getSession().setAttribute("isMedico", true);

    request.setParameter("operazione", "caricaAllegato");
    servlet.doPost(request, response);

    request.setParameter("oggetto", oggetto);
    request.setParameter("testo", testo);
    request.setParameter("selectPaziente", CfPaziente);
    request.setParameter("operazione", "inviaMessaggio");
    servlet.doPost(request, response);

    assertEquals("./dashboard.jsp?notifica=messaggioInviato", response.getRedirectedUrl());
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

  @Test
  void TC_GP_9_5_InvioMessaggi() throws ServletException, IOException {
    request.getSession().setAttribute("utente", paziente);
    request.getSession().setAttribute("isPaziente", true);

    //request.addPart(allegato);
    request.setParameter("operazione", "caricaAllegato");
    servlet.doPost(request, response);

    request.setParameter("oggetto", oggetto);
    request.setParameter("testo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sollicitudin nisi sit amet nibh congue scelerisque. Donec ornare pharetra erat, at lobortis tellus commodo eu. Integer gravida nulla non risus aliquam, elementum mollis turpis fringilla. Sed vel commodo ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum viverra diam fermentum sapien cursus scelerisque. Praesent porta ac diam eu tempus. Pellentesque pellentesque nisi enim, non pellentesque mauris maximus nec. Nunc et enim eu purus porta molestie eget sed libero. Donec ullamcorper ligula orci, eu molestie lorem pharetra at. Nulla tortor tellus, varius sagittis congue quis, lobortis et metus. Sed elementum varius justo, et sollicitudin lectus porttitor at.\n" + 
        "Integer porta diam nec commodo consequat. Pellentesque pharetra vel lacus nec condimentum. Vivamus metus tortor, mattis non pulvinar in, mollis quis nibh. Cras ultrices vel orci eu bibendum. In pulvinar, lacus vitae cras amet.\n" + 
        "");
    request.setParameter("selectMedico", "");
    request.setParameter("operazione", "inviaMessaggio");
    servlet.doPost(request, response);

    assertEquals("./dashboard.jsp?notifica=comunicazioneNonInviata", response.getRedirectedUrl());
  }

  @Test
  void TC_GM_9_6_InvioMessaggi() throws ServletException, IOException {
    request.getSession().setAttribute("utente", paziente);
    request.getSession().setAttribute("isPaziente", true);

    //request.addPart(allegato);
    request.setParameter("operazione", "caricaAllegato");
    servlet.doPost(request, response);

    request.setParameter("oggetto", oggetto);
    request.setParameter("testo", testo);
    request.setParameter("selectMedico", CfMedico);
    request.setParameter("operazione", "inviaMessaggio");
    servlet.doPost(request, response);

    assertEquals("./dashboard.jsp?notifica=messaggioInviato", response.getRedirectedUrl());
  }


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
    Messaggio secondoMessaggio = 
        costruisciMessaggio(paziente.getCodiceFiscale(), medico.getCodiceFiscale());

    MessaggioProxy primo = 
        new MessaggioProxy(daPazAMed.getCodiceFiscaleMittente(), daPazAMed.getOggetto(),
        daPazAMed.getData(), daPazAMed.getDestinatariView());
    MessaggioProxy secondo = new MessaggioProxy(secondoMessaggio.getCodiceFiscaleMittente(), secondoMessaggio.getOggetto(),
        secondoMessaggio.getData(), secondoMessaggio.getDestinatariView());
    primo.setIdMessaggio(daPazAMed.getIdMessaggio());
    secondo.setIdMessaggio(secondoMessaggio.getIdMessaggio());
    //a questo punto non ha ancora settato la visualizzazione
    primo.setVisualizzato(null);
    secondo.setVisualizzato(null);
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
    Messaggio secondoMessaggio = 
        costruisciMessaggio(medico.getCodiceFiscale(), paziente.getCodiceFiscale());

    MessaggioProxy primo = 
        new MessaggioProxy(daMedAPaz.getCodiceFiscaleMittente(), daMedAPaz.getOggetto(),
        daMedAPaz.getData(), daMedAPaz.getDestinatariView());
    MessaggioProxy secondo = new MessaggioProxy(secondoMessaggio.getCodiceFiscaleMittente(), secondoMessaggio.getOggetto(),
        secondoMessaggio.getData(), secondoMessaggio.getDestinatariView());
    primo.setIdMessaggio(daMedAPaz.getIdMessaggio());
    secondo.setIdMessaggio(secondoMessaggio.getIdMessaggio());
    //a questo punto non ha ancora settato la visualizzazione
    primo.setVisualizzato(null);
    secondo.setVisualizzato(null);
    //l'ordine di inserimento va invertito rispetto all'ordine di aggiunta al database
    //perché nel mostrare la lista il model sceglie prima i messaggi più recenti
    messaggi.add(secondo);
    messaggi.add(primo);	

    request.setParameter("operazione", "visualizzaElencoMessaggio");
    servlet.doGet(request, response);

    assertEquals(messaggi.toString(), request.getAttribute("messaggio").toString());
  }

  @Test
  void testCaricaDestinatariPerPaziente() throws ServletException, IOException {
    ArrayList<Medico> medici = new ArrayList<Medico>();
    medici.add(medico);

    request.getSession().setAttribute("utente", paziente);
    request.getSession().setAttribute("isPaziente", true);
    request.getSession().setAttribute("accessDone", true);

    request.setParameter("operazione", "caricaDestinatariMessaggio");
    servlet.doGet(request, response);

    assertEquals(medici.toString(), request.getAttribute("mediciCuranti").toString());
  }

  @Test
  void testCaricaDestinatariPerMedico() throws ServletException, IOException {
    ArrayList<Paziente> pazienti = new ArrayList<Paziente>();
    pazienti.add(paziente);

    request.getSession().setAttribute("utente", medico);
    request.getSession().setAttribute("isMedico", true);
    request.getSession().setAttribute("accessDone", true);

    request.setParameter("operazione", "caricaDestinatariMessaggio");
    servlet.doGet(request, response);

    System.out.println(request.getAttribute("pazientiSeguiti").toString());

    assertEquals(pazienti.toString(), request.getAttribute("pazientiSeguiti").toString());
  }

  @Test
  void testRimuoviAllegato() throws ServletException, IOException {
    request.getSession().setAttribute("utente", medico);
    request.getSession().setAttribute("isMedico", true);
    request.getSession().setAttribute("accessDone", true);
    request.getSession().setAttribute("id", daPazAMed.getIdMessaggio());

    request.setParameter("operazione", "rimuoviAllegato");
    servlet.doGet(request, response);

    assertNull(MessaggioModel.getMessaggioById(daPazAMed.getIdMessaggio()));
  }

  @Test
  void testRimuoviIncompleta() throws ServletException, IOException {

    MongoCollection<Document> messaggi = 
        DriverConnection.getConnection().getCollection("Messaggio");
    request.getSession().setAttribute("utente", paziente);
    request.getSession().setAttribute("isPaziente", true);
    request.getSession().setAttribute("accessDone", true);
    request.getSession().setAttribute("id", daMedAPaz.getIdMessaggio());

    //inserisco un messaggio senza destinatari, condizione di rimozione
    Document d = 
        messaggi.find(eq("_id", new ObjectId(daMedAPaz.getIdMessaggio())))
        .projection(Projections.exclude("_id")).
        first().append("DestinatariView", null);
    messaggi.insertOne(d);
    ObjectId id = (ObjectId)d.get("_id");

    request.setParameter("operazione", "rimuoviMessaggioIncompleto");
    servlet.doGet(request, response);

    assertNull(MessaggioModel.getMessaggioById(id.toString()));
  }


  //metodo di servizio
  private static Messaggio costruisciMessaggio(String mittente, String destinatario) {
    HashMap<String, Boolean> destinatari = new HashMap<String, Boolean>();
    destinatari.put(destinatario, false);
    Messaggio daAggiungere = new MessaggioCompleto(mittente, "oggetto", "testo", null, null,
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatari);

    MongoCollection<Document> messaggio = 
        DriverConnection.getConnection().getCollection("Messaggio");
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

    Document allegato = 
        new Document("NomeAllegato", daAggiungere.getNomeAllegato()).append("CorpoAllegato",
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
