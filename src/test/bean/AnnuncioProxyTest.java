package test.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bson.internal.Base64;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.AnnuncioProxy;
import model.AnnuncioModel;

public class AnnuncioProxyTest {
  static private HashMap<String, Boolean> destinatari = new HashMap<String, Boolean>();
  static String idAnnuncio = new String();
  static String corpoAllegato = Base64.encode("codiceallegato".getBytes());
  static String nomeAllegato = Base64.encode("dialisi-peritoneale.pdf".getBytes());

  @BeforeEach
  void setUp() {
    Annuncio annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.", corpoAllegato,
        nomeAllegato, ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    idAnnuncio = AnnuncioModel.addAnnuncio(annuncio);
    destinatari.put(null, false);
  }

  @AfterEach
  void tearDown() {
    AnnuncioModel.deleteAnnuncioById(idAnnuncio);
  }

  // Test per il costruttore vuoto

  @Test
  void testMessaggioProxyCostruttoreVuoto() {
    AnnuncioProxy AnnuncioProxyEmpty = new AnnuncioProxy();
    assertNotNull(AnnuncioProxyEmpty);
  }


  // Test per i metodi di get

  @Test
  void testGetMedico() {
    AnnuncioProxy annuncioProxy=new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals("DCPLRD71M12C129X",annuncioProxy.getMedico());
  }

  @Test
  void testGetPazientiView() {
    AnnuncioProxy annuncioProxy=new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio(idAnnuncio);
    assertEquals(destinatari, annuncioProxy.getPazientiView());
  }

  @Test
  void testGetTitolo() {
    AnnuncioProxy annuncioProxy=new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",annuncioProxy.getTitolo());
  }

  @Test
  void testGetTesto() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",annuncioProxy.getTesto());
  }

  @Test
  void testGetCorpoAllegato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio(idAnnuncio);
    assertEquals(corpoAllegato, annuncioProxy.getCorpoAllegato());
  }

  @Test
  void testGetNomeAllegato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals(nomeAllegato,annuncioProxy.getNomeAllegato());
  }

  @Test
  void testGetData() {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(formatter).toString(),annuncioProxy.getDataFormattata());
  }

  @Test
  void testGetOraFormattata() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format),annuncioProxy.getOraFormattata());
  }

  @Test
  void testGetDataFormattata() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format),annuncioProxy.getDataFormattata());
  }

  @Test
  void testGetVisualizzato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    assertEquals(false,annuncioProxy.getVisualizzato());
  }

  @Test
  void testGetIdAnnuncio() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio("1234");
    assertEquals("1234",annuncioProxy.getIdAnnuncio());
  }

  //Test per i metodi di set

  @Test
  void testSetMedico() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setMedico("DCPLRD71M12C129X");
    assertEquals("DCPLRD71M12C129X",annuncioProxy.getMedico());
  }

  @Test
  void testSetPazientiView() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio(idAnnuncio);
    assertEquals(destinatari,annuncioProxy.getPazientiView());
  }

  @Test
  void testSetTitolo() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setTitolo("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.");
    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",annuncioProxy.getTitolo());
  }

  @Test
  void testSetTesto() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setTesto("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.");
    assertEquals("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",annuncioProxy.getTesto());
  }

  @Test
  void testSetCorpoAllegato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio(idAnnuncio);
    annuncioProxy.setCorpoAllegato("codiceAllegato");
    assertEquals("codiceAllegato",annuncioProxy.getCorpoAllegato());
  }

  @Test
  void testSetNomeAllegato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        "",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setNomeAllegato(nomeAllegato);
    assertEquals(nomeAllegato,annuncioProxy.getNomeAllegato());
  }

  @Test
  void testSetData() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,null,destinatari);
    annuncioProxy.setData(ZonedDateTime.now(ZoneId.of("Europe/Rome")));
    assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")),annuncioProxy.getData());
  }

  @Test
  void testSetVisualizzato() {
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setVisualizzato(true);
    assertEquals(true,annuncioProxy.getVisualizzato());
  }

  @Test
  void testSetIdAnnuncio() {
    AnnuncioProxy annuncioProxy=new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio("1234");
    assertEquals("1234",annuncioProxy.getIdAnnuncio());
  }

  @Test
  void testToString() { 
    AnnuncioProxy annuncioProxy = new AnnuncioProxy("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
        "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",
        nomeAllegato,ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
    annuncioProxy.setIdAnnuncio(idAnnuncio);
    String toStringBean = annuncioProxy.toString();
    String toStringTest = "AnnuncioProxy [idAnnuncio=" + annuncioProxy.getIdAnnuncio() 
            + ", medico=" + annuncioProxy.getMedico()
            + ", titolo=" + annuncioProxy.getTitolo() + ", testo=" + annuncioProxy.getTesto() 
            + ", data=" + annuncioProxy.getData() + ", visualizzato=" 
            + annuncioProxy.getVisualizzato() + ", pazientiView=" 
            + annuncioProxy.getPazientiView() + "]";
    assertEquals(toStringBean,toStringTest);
  }
}
