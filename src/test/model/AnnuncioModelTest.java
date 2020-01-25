package test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bean.Annuncio;
import bean.AnnuncioCompleto;
import com.mongodb.client.MongoCollection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import model.AnnuncioModel;
import model.DriverConnection;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AnnuncioModelTest {
  private static HashMap<String, Boolean> destinatari;
  private static String idAnnuncio = "";
  private static final String medico = "GRMBNN67L11B516R";
  private static final String titolo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.";
  private static final String testo = "Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.";
  private static final String corpoAllegato = "codiceallegato";
  private static final String nomeAllegato = "dialisi-peritoneale.pdf";
  private static final String codiceFiscalePaziente = "BNCLRD67A01F205I";
  //serve che questo paziente non sia nel database per rendere attendibile il test di counNonLetti

  private static ZonedDateTime data = null;

  //TODO: non usare i metodi del model ma direttamente le query Mongo
  //poi anche add e delete andrebbero testati in qualche modo

  @BeforeEach
  void setUp() throws Exception {
    data = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
    destinatari = new HashMap<String, Boolean>();
    destinatari.put(codiceFiscalePaziente, false);
    AnnuncioCompleto annuncio = 
        new AnnuncioCompleto(medico,titolo,testo,corpoAllegato,nomeAllegato,data,destinatari);
    idAnnuncio = AnnuncioModel.addAnnuncio(annuncio);
  }

  @AfterEach
  void tearDown() throws Exception {
    AnnuncioModel.deleteAnnuncioById(idAnnuncio);
  }
  
  @AfterAll
  static void tearDownAfterClass() throws Exception {
    MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Annuncio");
    pazienti.deleteMany(new Document());
  }

  @Test
  void testGetAnnuncioById() {
    Annuncio annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
    assertNotNull(annuncio);
    assertEquals(annuncio.getIdAnnuncio(),idAnnuncio);
    assertEquals(annuncio.getMedico(),medico);
    assertEquals(annuncio.getTitolo(),titolo);
    assertEquals(annuncio.getTesto(),testo);
    assertEquals(annuncio.getCorpoAllegato(),corpoAllegato);
    assertEquals(annuncio.getNomeAllegato(),nomeAllegato);
    assertEquals(annuncio.getData(),data);
    assertEquals(annuncio.getPazientiView(),destinatari);
  }
  
  @Test
  void testGetAnnuncioByIdSbagliato() {
    Annuncio annuncio = AnnuncioModel.getAnnuncioById("5e2a094047426c115426d64c");
    assertNull(annuncio);
  }
  
  @Test
  void testAddAnnuncioNoDestinatari() {
    HashMap<String, Boolean> hm = new HashMap<>();
    AnnuncioCompleto originale = new AnnuncioCompleto(medico, titolo, testo, corpoAllegato, nomeAllegato, ZonedDateTime.now(), hm);
    String id = AnnuncioModel.addAnnuncio(originale);
    originale.setIdAnnuncio(id);
    originale.setVisualizzato(null);
    hm.put(null, false);
    originale.setPazientiView(hm);

    System.out.println(originale);

    Annuncio ottenuto = AnnuncioModel.getAnnuncioById(id);
    assertEquals(originale.toString(), ottenuto.toString());
    AnnuncioModel.deleteAnnuncioById(id);
  }

  @Test
  void testUpdateAnnuncio() {
    Annuncio daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    assertNotNull(daAggiornare);
    daAggiornare.setTitolo("Nuovo annuncio");
    daAggiornare.setTesto("Nuove medicine in commercio");
    AnnuncioModel.updateAnnuncio(daAggiornare);
    daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getTitolo(),"Nuovo annuncio");
    assertEquals(daAggiornare.getTesto(),"Nuove medicine in commercio");
  }
  
  @Test
  void testUpdateAnnuncioMalformato() {
    Annuncio daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    assertNotNull(daAggiornare);
    daAggiornare.setTitolo(null);
    daAggiornare.setTesto(null);
    daAggiornare.setCorpoAllegato(null);
    daAggiornare.setData(null);
    daAggiornare.setMedico(null);
    AnnuncioModel.updateAnnuncio(daAggiornare);
    daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    assertNotNull(daAggiornare);
    assertEquals(daAggiornare.getTitolo(), titolo);
    assertEquals(daAggiornare.getTesto(), testo);
  }
  
  @Test
  void testUpdateAnnuncioNoId() {
    Annuncio daAggiornare = null;
    AnnuncioModel.updateAnnuncio(daAggiornare);  

    daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    daAggiornare.setTitolo(titolo);
    daAggiornare.setTesto(testo);
    daAggiornare.setIdAnnuncio(null);
    AnnuncioModel.updateAnnuncio(daAggiornare);

    daAggiornare.setIdAnnuncio(idAnnuncio);
    assertEquals(daAggiornare.toString(), AnnuncioModel.getAnnuncioById(idAnnuncio).toString());

    daAggiornare = AnnuncioModel.getAnnuncioById(idAnnuncio);
    daAggiornare.setTitolo(titolo);
    daAggiornare.setTesto(testo);
    daAggiornare.setIdAnnuncio("");
    AnnuncioModel.updateAnnuncio(daAggiornare);

    daAggiornare.setIdAnnuncio(idAnnuncio);
    assertEquals(daAggiornare.toString(), AnnuncioModel.getAnnuncioById(idAnnuncio).toString());
  }

  @Test
  void testGetAnnuncioByCFMedico() {
    ArrayList<Annuncio> annunci = AnnuncioModel.getAnnunciByCFMedico(medico);
    for (Annuncio a:annunci) {
      assertEquals(a.getMedico(),medico);
    }
  }
    
  @Test
  void testDeleteAnnuncioById() {
    //id errato per testare se ci sono problemi in tale caso
    AnnuncioModel.deleteAnnuncioById("5e29b99747426c5cf006360b");
    AnnuncioModel.deleteAnnuncioById(idAnnuncio);

    assertNull(AnnuncioModel.getAnnuncioById(idAnnuncio));
  }

  //TODO Questi tre test

  @Test
  void testGetAnnuncioByCFPaziente() {
    ArrayList<Annuncio> annunci = AnnuncioModel.getAnnuncioByCFPaziente(codiceFiscalePaziente);
    boolean destinatarioGiusto = true;

    for (Annuncio a : annunci) {
      if (!a.getPazientiView().containsKey(codiceFiscalePaziente)) {
        destinatarioGiusto = false;
      }
    }

    assertTrue(destinatarioGiusto);
  }

  @Test
  void testCountAnnunciNonLetti() {
    int nonLetti = AnnuncioModel.countAnnunciNonLetti(codiceFiscalePaziente);

    assertEquals(nonLetti, 1);
  }

  @Test
  void testSetVisualizzato() {
    AnnuncioModel.setVisualizzatoAnnuncio(idAnnuncio, codiceFiscalePaziente, true);

    boolean visualizzato = false;
    Annuncio annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
    System.out.println("pazientiview" + annuncio.getPazientiView());
    Iterator<Entry<String, Boolean>> it = annuncio.getPazientiView().entrySet().iterator();
    Entry<String, Boolean> temp; 

    while (it.hasNext()) {
      temp = it.next();
      if (temp.getKey().equals(codiceFiscalePaziente)) {
        visualizzato = temp.getValue();
        break;
      }
    }

    assertEquals(true, visualizzato);
  }
}
