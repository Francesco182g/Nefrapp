package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bean.Amministratore;

class AmministratoreTest {

  //Test per il costruttore vuoto
  @Test
  void testAmministratoreCostruttoreVuoto() {
    Amministratore amministratoreEmpity = new Amministratore();
    assertNotNull(amministratoreEmpity);
  }

  //Test per i metodi di get

  @Test
  void testGetCodiceFiscale() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    assertEquals("FLPBRZ62F17F876F", amministratore.getCodiceFiscale());
  }

  @Test
  void testGetNome() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    assertEquals("Filippo", amministratore.getNome());
  }

  @Test
  void testGetCognome() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    assertEquals("Carbosiero", amministratore.getCognome());
  }

  @Test
  void testGetEmail() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    assertEquals("f.carbosiero@live.it", amministratore.getEmail());
  }

  //Test per i metodi di set

  @Test
  void testSetCodiceFiscale() {
    Amministratore amministratore = 
        new Amministratore("", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    amministratore.setCodiceFiscale("FLPBRZ62F17F876F");
    assertEquals("FLPBRZ62F17F876F", amministratore.getCodiceFiscale());
  }

  @Test
  void testSetNome() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "", "Carbosiero", "f.carbosiero@live.it");
    amministratore.setNome("Filippo");
    assertEquals("Filippo", amministratore.getNome());
  }

  @Test
  void testSetCognome() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "", "f.carbosiero@live.it");
    amministratore.setCognome("Carbosiero");
    assertEquals("Carbosiero", amministratore.getCognome());
  }

  @Test
  void testSetEmail() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "");
    amministratore.setEmail("f.carbosiero@live.it");
    assertEquals("f.carbosiero@live.it", amministratore.getEmail());
  }

  //Test per il toString
  @Test
  void testToString() {
    Amministratore amministratore = 
        new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
    String toStringBean = amministratore.toString();
    String toStringTest = 
        "bean.Amministratore [codiceFiscale=" + amministratore.getCodiceFiscale() 
        + ", nome=" + amministratore.getNome() 
        + ", cognome=" + amministratore.getCognome() 
        + ", email=" + amministratore.getEmail() + "]";
    assertEquals(toStringBean, toStringTest);

  }
}
