package bean;

/**
 * Questa classe rappresenta l'amministratore.
 *  @author Eugenio Corbisiero
 *  
 */
public class Amministratore extends Utente {

  public Amministratore() {
  }

  public Amministratore(String codiceFiscale, String nome, String cognome, String email) {
    super(codiceFiscale, nome, cognome,email);
  }

  @Override
  public String toString() {
    return super.toString();
  }
}