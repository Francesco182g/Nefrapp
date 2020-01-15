package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bean.Utente;

class UtenteTest {

  //Test per il costruttore vuoto
  @Test
  void testUtenteCostruttoreVuoto() {
    Utente utenteEmpity = new Utente();
    assertNotNull(utenteEmpity);
  }

  //Test per i metodi di get

  @Test
  void testGetCodiceFiscale() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com");
    assertEquals("GRMBNN67L11B519R", utente.getCodiceFiscale());
  }

  @Test
  void testGetNome() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com");
    assertEquals("Geremia", utente.getNome());
  }

  @Test
  void testGetCognome() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com");
    assertEquals("Bernini", utente.getCognome());
  }

  @Test
  void testGetEmail() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com");
    assertEquals("G.Bernini67@gmail.com", utente.getEmail());
  }

  //Test per i metodi di set

  @Test
  void testSetCodiceFiscale() {
    Utente utente = new Utente("", "Geremia", "Bernini", "G.Bernini67@gmail.com");
    utente.setCodiceFiscale("GRMBNN67L11B519R");
    assertEquals("GRMBNN67L11B519R", utente.getCodiceFiscale());
  }

  @Test
  void testSetNome() {
    Utente utente = new Utente("GRMBNN67L11B519R", "", "Bernini", "G.Bernini67@gmail.com");
    utente.setNome("Geremia");
    assertEquals("Geremia", utente.getNome());
  }

  @Test
  void testSetCognome() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "", "G.Bernini67@gmail.com");
    utente.setCognome("Bernini");
    assertEquals("Bernini", utente.getCognome());
  }

  @Test
  void testSetEmail() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "");
    utente.setEmail("G.Bernini67@gmail.com");
    assertEquals("G.Bernini67@gmail.com", utente.getEmail());
  }

  //Test per il toString
  @Test
  void testToString() {
    Utente utente = new Utente("GRMBNN67L11B519R", "Geremia", "Bernini", "");
    String toStringBean = utente.toString();
    String toStringTest = "bean.Utente [codiceFiscale=" + utente.getCodiceFiscale() 
        + ", nome=" + utente.getNome() 
        + ", cognome=" + utente.getCognome() 
        + ", email=" + utente.getEmail() + "]";
    assertEquals(toStringBean, toStringTest);
  }
}