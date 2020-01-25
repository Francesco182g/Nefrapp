package test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import bean.Amministratore;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import model.AmministratoreModel;
import model.DriverConnection;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utility.CriptazioneUtility;

class AmministratoreModelTest {
  private static String password = CriptazioneUtility.criptaConMD5("Pippo1234");

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    MongoCollection<Document> admin = 
        DriverConnection.getConnection().getCollection("Amministratore");

    Document doc = new Document("CodiceFiscale", "FLPBRZ62F17F876F")
        .append("Nome", "Filippo")
        .append("Cognome", "Carbosiero")
        .append("Password",password)
        .append("Email", "f.carbosiero@live.it");
    admin.insertOne(doc);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    MongoCollection<Document> admin = 
        DriverConnection.getConnection().getCollection("Amministratore");
    BasicDBObject document = new BasicDBObject();
    document.put("CodiceFiscale", "FLPBRZ62F17F876F");
    admin.deleteOne(document);
  }

  @Test
  void testGetAmministratoreByCFPassword() {
    Amministratore adminTest = 
        AmministratoreModel.getAmministratoreByCFPassword("FLPBRZ62F17F876F", password);
    assertNotNull(adminTest);
    assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");
    assertEquals(adminTest.getNome(), "Filippo");
    assertEquals(adminTest.getCognome(), "Carbosiero");
    assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");
  }
  
  @Test
  void testGetAmministratoreByCFPasswordErrati() {
    Amministratore adminTest = 
        AmministratoreModel.getAmministratoreByCFPassword("FLPBRZ02F17F876F", password);
    assertNull(adminTest);
  }

  @Test
  void testGetAmministratoreByCF() {
    Amministratore adminTest = AmministratoreModel.getAmministratoreByCF("FLPBRZ62F17F876F");
    assertNotNull(adminTest);
    assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");
    assertEquals(adminTest.getNome(), "Filippo");
    assertEquals(adminTest.getCognome(), "Carbosiero");
    assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");

  }
  
  @Test
  void testGetAmministratoreByCFNonPresente() {
    Amministratore adminTest = AmministratoreModel.getAmministratoreByCF("FLPBTZ62F17F876F");
    assertNull(adminTest);
  }


  @Test
  void testGetPassword() {
    String passwordTest = AmministratoreModel.getPassword("FLPBRZ62F17F876F");
    assertEquals(passwordTest, password);
  }

  @Test
  void testUpdateAmministratore() {
    String nuovaPassword = CriptazioneUtility.criptaConMD5("Pippo5678");
    AmministratoreModel.updateAmministratore("FLPBRZ62F17F876F", nuovaPassword);
    Amministratore adminTest = 
        AmministratoreModel.getAmministratoreByCFPassword("FLPBRZ62F17F876F", nuovaPassword);
    assertNotNull(adminTest);
    assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");
    assertEquals(adminTest.getNome(), "Filippo");
    assertEquals(adminTest.getCognome(), "Carbosiero");
    assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");
  }
}
