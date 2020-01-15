package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Paziente;
import bean.SchedaParametri;
import model.DriverConnection;
import model.PazienteModel;
import model.SchedaParametriModel;

class SchedaParametriModelTest {
  private static final LocalDate dataNascitaPaziente = LocalDate.parse("1967-07-11");
  private static final String residenzaPaziente = "Via Roma, 22, Scafati, 80030, NA";
  private static final ArrayList<String> medici = new ArrayList<String>();
  private static final LocalDate dataInizio = LocalDate.parse("2019-05-20");
  private static final LocalDate dataFine = LocalDate.parse("2019-05-29");


  @BeforeEach
  void setUp() throws Exception {
    Paziente paziente = new Paziente("M", "RSSGPP79E16I483P", "Giuseppe", "Russo", "giuseppe.russo@live.it", residenzaPaziente, "Scafati", dataNascitaPaziente, false, medici);
    PazienteModel.addPaziente(paziente, "PasswordDifficile");

    SchedaParametri scheda = new SchedaParametri("RSSGPP79E16I483P", new BigDecimal("50"), 120, 80, 1550, 700, 1, 100, 1500, dataInizio);
    SchedaParametriModel.addSchedaParametri(scheda);
  }

  @AfterEach
  void tearDown() {
    MongoCollection<Document> scheda = 
        DriverConnection.getConnection().getCollection("SchedaParametri");
    BasicDBObject document = new BasicDBObject();
    document.put("PazienteCodiceFiscale", "RSSGPP79E16I483P");
    scheda.deleteOne(document);

    BasicDBObject document1 = new BasicDBObject();
    document1.put("PazienteCodiceFiscale", "RFFGPP79E16I483P");
    scheda.deleteOne(document1);

    PazienteModel.removePaziente("RSSGPP79E16I483P");

  }

  @Test
  void testGetSchedeParametriByCF() {
    ArrayList<SchedaParametri> schedeParametri = new ArrayList<SchedaParametri>();
    schedeParametri = SchedaParametriModel.getSchedeParametriByCF("RSSGPP79E16I483P");
    assertNotNull(schedeParametri);
  }

  @Test
  void testAddSchedaParametri() {
    SchedaParametri daAggiungere = new SchedaParametri("RFFGPP79E16I483P", new BigDecimal("80"), 112, 80, 1550, 700, 1, 100, 1500, dataFine);
    SchedaParametriModel.addSchedaParametri(daAggiungere);

    ArrayList<SchedaParametri> schedeParametri = 
        SchedaParametriModel.getSchedeParametriByCF("RFFGPP79E16I483P");
    assertNotNull(schedeParametri.get(0));
    assertEquals(schedeParametri.get(0).getPazienteCodiceFiscale(),"RFFGPP79E16I483P");
    assertEquals(schedeParametri.get(0).getPeso(),new BigDecimal("80"));
    assertEquals(schedeParametri.get(0).getPaMin(),112);
    assertEquals(schedeParametri.get(0).getPaMax(),80);
    assertEquals(schedeParametri.get(0).getScaricoIniziale(),1550);
    assertEquals(schedeParametri.get(0).getUF(),700);
    assertEquals(schedeParametri.get(0).getTempoSosta(),1);
    assertEquals(schedeParametri.get(0).getCarico(),100);
    assertEquals(schedeParametri.get(0).getScarico(),1500);
    assertEquals(schedeParametri.get(0).getDataFormattata(),"29/05/2019");	
    assertEquals(schedeParametri.get(0).getData(),LocalDate.parse("2019-05-29"));	

  }

  @Test
  void testGetReportByPaziente() {
    ArrayList<SchedaParametri> schedeParametri = 
        SchedaParametriModel.getReportByPaziente("RFFGPP79E16I483P", dataInizio, dataFine);
    assertNotNull(schedeParametri);

  }

}
