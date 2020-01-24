package test.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.Messaggio;
import bean.MessaggioCompleto;
import model.AnnuncioModel;
import model.MessaggioModel;


public class MessaggioModelTest {
  private final static String CFDestinatario = "BNCLRD67A01F205I";
  private final static String CFMittente = "GRMBNN67L11B516R";
  private final static String oggetto = "visita";
  private final static String testo = "Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”";
  private final static String corpoAllegato = "codiceAllegato";
  private final static String nomeAllegato = "ilpazienteirriverente.pdf";
  private final static ZonedDateTime data = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
  private final static HashMap<String,Boolean> destinatariView = new HashMap<String,Boolean>();
  private final static DateTimeFormatter formatOra = DateTimeFormatter.ofPattern("HH:mm");
  private final static DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  static MessaggioCompleto messaggio = new MessaggioCompleto(CFMittente, oggetto, testo,corpoAllegato,nomeAllegato,data, destinatariView);
  String id = "";

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    destinatariView.put(CFDestinatario, false);
    messaggio.setDestinatariView(destinatariView);
  }

  @BeforeEach
  void setUp() {
    messaggio.setVisualizzato(null);
    id = MessaggioModel.addMessaggio(messaggio);
    messaggio.setIdMessaggio(id);
  }

  @AfterEach
  void tearDown() {
    MessaggioModel.deleteMessaggioById(id);
  }

  @Test
  void testAddMessaggio() {
	HashMap<String, Boolean> hm = new HashMap<>();
    MessaggioCompleto messaggioDaAggiungere = new MessaggioCompleto(CFMittente, oggetto, testo,corpoAllegato,nomeAllegato,data, hm);
    String idDaTestare = MessaggioModel.addMessaggio(messaggioDaAggiungere);
    MessaggioCompleto messaggioDaTestare =
        (MessaggioCompleto) MessaggioModel.getMessaggioById(idDaTestare);
    assertNotNull(messaggioDaTestare);
    assertEquals(messaggioDaTestare.getCodiceFiscaleMittente(),CFMittente);
    assertEquals(messaggioDaTestare.getCorpoAllegato(),corpoAllegato);
    assertEquals(messaggioDaTestare.getData(), data);
    assertEquals(messaggioDaTestare.getDataFormattata(), data.format(formatData));
    hm.put(null, false);
    messaggioDaTestare.setDestinatariView(hm);
    assertEquals(messaggioDaTestare.getDestinatariView(), hm);
    assertEquals(messaggioDaTestare.getNomeAllegato(),nomeAllegato);
    assertEquals(messaggioDaTestare.getOggetto(),oggetto);
    assertEquals(messaggioDaTestare.getOraFormattata(),data.format(formatOra));
    assertEquals(messaggioDaTestare.getTesto(), testo);
    MessaggioModel.deleteMessaggioById(idDaTestare);
  }
  
  @Test
  void testAddMessaggioNoDestinatari() {
    MessaggioCompleto messaggioDaAggiungere = new MessaggioCompleto(CFMittente, oggetto, testo,corpoAllegato,nomeAllegato,data, destinatariView);
    String idDaTestare = MessaggioModel.addMessaggio(messaggioDaAggiungere);
    MessaggioCompleto messaggioDaTestare =
        (MessaggioCompleto) MessaggioModel.getMessaggioById(idDaTestare);
    assertNotNull(messaggioDaTestare);
    assertEquals(messaggioDaTestare.getCodiceFiscaleMittente(),CFMittente);
    assertEquals(messaggioDaTestare.getCorpoAllegato(),corpoAllegato);
    assertEquals(messaggioDaTestare.getData(), data);
    assertEquals(messaggioDaTestare.getDataFormattata(), data.format(formatData));
    System.out.println(messaggioDaTestare.getDestinatariView());
    assertEquals(messaggioDaTestare.getDestinatariView(), destinatariView);
    assertEquals(messaggioDaTestare.getNomeAllegato(),nomeAllegato);
    assertEquals(messaggioDaTestare.getOggetto(),oggetto);
    assertEquals(messaggioDaTestare.getOraFormattata(),data.format(formatOra));
    assertEquals(messaggioDaTestare.getTesto(), testo);
    MessaggioModel.deleteMessaggioById(idDaTestare);
  }

  @Test
  void testDeleteMessaggioById() {
    MessaggioModel.deleteMessaggioById(id);
    MessaggioCompleto messaggioDaTestare = (MessaggioCompleto) MessaggioModel.getMessaggioById(id);
    assertNull(messaggioDaTestare);
  }

  @Test
  void testGetMessaggiByDestinatario() {
    ArrayList<Messaggio> messaggiDaTestare =
        MessaggioModel.getMessaggiByDestinatario(CFDestinatario);
    assertNotNull(messaggiDaTestare);
  }

  @Test
  void testUpdateMessaggio() {
    Messaggio daAggiornare = MessaggioModel.getMessaggioById(id);
    assertNotNull(daAggiornare);
    daAggiornare.setOggetto("Nuovo annuncio");
    daAggiornare.setTesto("Nuove medicine in commercio");
    MessaggioModel.updateMessaggio(daAggiornare);

    daAggiornare = MessaggioModel.getMessaggioById(id);
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getOggetto(),"Nuovo annuncio");
    assertEquals(daAggiornare.getTesto(),"Nuove medicine in commercio");
  }
  
  @Test
  void testUpdateMessaggioNoId() {
    Messaggio daAggiornare = MessaggioModel.getMessaggioById(id);
    assertNotNull(daAggiornare);
    daAggiornare.setOggetto("Nuovo annuncio");
    daAggiornare.setTesto("Nuove medicine in commercio");
    daAggiornare.setIdMessaggio(null);
    MessaggioModel.updateMessaggio(daAggiornare);

    daAggiornare = MessaggioModel.getMessaggioById("5de95046be6d62154c81eabe");
    assertNull(daAggiornare);
  }


  @Test
  void testGetMessaggioById() {
    MessaggioCompleto messaggioDaTestare = (MessaggioCompleto) MessaggioModel.getMessaggioById(id);
    assertNotNull(messaggioDaTestare);

    assertEquals(messaggioDaTestare.getCodiceFiscaleMittente(),messaggio.getCodiceFiscaleMittente());
    assertEquals(messaggioDaTestare.getCorpoAllegato(),messaggio.getCorpoAllegato());
    assertEquals(messaggioDaTestare.getData(), messaggio.getData());
    assertEquals(messaggioDaTestare.getDataFormattata(), messaggio.getDataFormattata());
    assertEquals(messaggioDaTestare.getDestinatariView(), messaggio.getDestinatariView());
    assertEquals(messaggioDaTestare.getNomeAllegato(),messaggio.getNomeAllegato());
    assertEquals(messaggioDaTestare.getOggetto(),messaggio.getOggetto());
    assertEquals(messaggioDaTestare.getOraFormattata(),messaggio.getOraFormattata());
    assertEquals(messaggioDaTestare.getTesto(), messaggio.getTesto());
    System.out.println(messaggioDaTestare.getVisualizzato() + " " + messaggio.getVisualizzato());
    assertEquals(messaggioDaTestare.getVisualizzato(), messaggio.getVisualizzato());
    assertEquals(messaggioDaTestare.toString(), messaggio.toString()); 
  }

  @Test
  void testSetVisualizzatoMessaggio() {
    MessaggioModel.setVisualizzatoMessaggio(id, CFDestinatario, true);
    MessaggioCompleto messaggioDaTestare = (MessaggioCompleto) MessaggioModel.getMessaggioById(id);
    HashMap<String,Boolean> destinatariDaTestare = messaggioDaTestare.getDestinatariView();
    assertEquals(destinatariDaTestare.get(CFDestinatario),true);
    MessaggioModel.setVisualizzatoMessaggio(id, CFDestinatario, false);

  }

  @Test
  void testCountMessaggiNonLetti() {
    int n = MessaggioModel.countMessaggiNonLetti(CFDestinatario);
    assertEquals(1,n);
  }
}
