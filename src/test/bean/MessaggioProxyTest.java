package test.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.MessaggioProxy;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import model.MessaggioModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessaggioProxyTest {
  private static HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();

  static String id = new String();
  
  @BeforeAll
  static void setUp() {
    Messaggio messaggio = new MessaggioCompleto("ABCD1111DFS12", "visita",
        "Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
        "codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
        destinatariView);
    id = MessaggioModel.addMessaggio(messaggio);
  }

  @AfterAll
  static void tearDown() {
    MessaggioModel.deleteMessaggioById(id);
  }
  
  // Test per il costruttore vuoto
  @Test
  void testMessaggioProxyCostruttoreVuoto() {
    MessaggioProxy MessaggioProxyEmpty = new MessaggioProxy();
    assertNotNull(MessaggioProxyEmpty);
  }

  // Test per i metodi di get

  @Test
  void testGetCodiceFiscaleMittente() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals("ABCD1111DFS12", MessaggioProxy.getCodiceFiscaleMittente());
  }

  @Test
  void testGetOggetto() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals("visita", MessaggioProxy.getOggetto());
  }

  @Test
  void testGetTesto() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    assertEquals(
        "Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
        MessaggioProxy.getTesto());
  }

  @Test
  void testGetCorpoAllegato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    assertEquals("codiceallegato", MessaggioProxy.getCorpoAllegato());
  }

  @Test
  void testGetNomeAllegato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    assertEquals("ilpazienteirriverente.pdf", MessaggioProxy.getNomeAllegato());
  }

  @Test
  void testGetOraFormattata() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format), MessaggioProxy.getOraFormattata());
  }

  @Test
  void testGetData() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")), MessaggioProxy.getData());
  }

  @Test
  void testGetDataFormattata() {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format), MessaggioProxy.getDataFormattata());
  }

  @Test
  void testGetVisualizzato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals(false, MessaggioProxy.getVisualizzato());
  }

  @Test
  void testGetIDMessaggio() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio("0123");
    assertEquals("0123", MessaggioProxy.getIdMessaggio());
  }

  @Test
  void testGetDestinatariView() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    assertEquals(destinatariView, MessaggioProxy.getDestinatariView());

  }
  // Test per i metodi di set

  @Test
  void testSetCodiceFiscaleMittente() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setCodiceFiscaleMittente("ABCD1111DFS12");
    assertEquals("ABCD1111DFS12", MessaggioProxy.getCodiceFiscaleMittente());
  }

  @Test
  void testSetOggetto() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setOggetto("visita");
    assertEquals("visita", MessaggioProxy.getOggetto());
  }

  @Test
  void testSetTesto() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    MessaggioProxy.setTesto(
        "Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”");
    assertEquals(
        "Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
        MessaggioProxy.getTesto());
  }

  @Test
  void testSetCorpoAllegato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    MessaggioProxy.setCorpoAllegato("codiceallegato");
    assertEquals("codiceallegato", MessaggioProxy.getCorpoAllegato());
  }

  @Test
  void testSetNomeAllegato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio(id);
    MessaggioProxy.setNomeAllegato("ilpazienteirriverente.pdf");
    assertEquals("ilpazienteirriverente.pdf", MessaggioProxy.getNomeAllegato());
  }

  @Test
  void testSetData() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setData(ZonedDateTime.now(ZoneId.of("Europe/Rome")));
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")), MessaggioProxy.getData());
  }

  @Test
  void testSetVisualizzato() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setVisualizzato(true);
    assertEquals(true, MessaggioProxy.getVisualizzato());
  }

  @Test
  void testSetIDMessaggio() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setIdMessaggio("0123");
    assertEquals("0123", MessaggioProxy.getIdMessaggio());
  }

  @Test
  void testSetDestinatariView() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    MessaggioProxy.setDestinatariView(destinatariView);
    assertEquals(destinatariView, MessaggioProxy.getDestinatariView());

  }

  // Test per il toString
  @Test
  void testToString() {
    MessaggioProxy MessaggioProxy = new MessaggioProxy("ABCD1111DFS12", "visita",
        ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
    String toStringBean = MessaggioProxy.toString();
    String toStringTest = "MessaggioProxy [idMessaggio=" + MessaggioProxy.getIdMessaggio() 
        + ", codiceFiscaleMittente=" + MessaggioProxy.getCodiceFiscaleMittente() + ", oggetto=" 
        + MessaggioProxy.getOggetto() + ", data=" + MessaggioProxy.getData()
        + ", visualizzato=" + MessaggioProxy.getVisualizzato()
        + ", destinatariView=" + MessaggioProxy.getDestinatariView() + "]";;
    assertEquals(toStringBean, toStringTest);

  }
}
