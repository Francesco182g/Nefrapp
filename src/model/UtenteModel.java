package model;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import bean.Utente;
import utility.CreaBeanUtility;

/**
 * 
 * @author NefrappTeam.
 * Questa classe � un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti l'utente.
 */
public class UtenteModel {

  /**
   * Questo metodo si occupa di ricercare un utente tramite codice fiscale.
   * @param codiceFiscale oggetto di tipo <strong>String</strong> 
   *        che rappresenta il codice fiscale dell'utente.
   * @return utente oggetto di tipo <strong>Utente</strong>, altrimenti null.
   * 
   * @precondition codiceFiscale != null.
   */
  public static Utente getUtenteByCF(String codiceFiscale) {

    MongoCollection<Document> utenti = DriverConnection.getConnection().getCollection("Medico");
    Utente utente = null;
    Document datiUtente = utenti.find(eq("CodiceFiscale", codiceFiscale)).first();

    if (datiUtente != null && !datiUtente.isEmpty()) {
      utente = CreaBeanUtility.daDocumentAMedico(datiUtente);
      return utente; 
    }
    else if (datiUtente == null || datiUtente.isEmpty()) {
      utenti = DriverConnection.getConnection().getCollection("Paziente");
      datiUtente = utenti.find(eq("CodiceFiscale", codiceFiscale)).first();
      if (datiUtente != null && !datiUtente.isEmpty()) {
        utente = CreaBeanUtility.daDocumentAPaziente(datiUtente);
      }
    }
    return utente;
  }
}
