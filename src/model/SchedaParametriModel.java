package model;

import static com.mongodb.client.model.Filters.eq;

import bean.SchedaParametri;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.Document;
import utility.CreaBeanUtility;

/**
 * Questa classe � un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti la scheda dei parametri.
 * @author NefrappTeam
 */
public class SchedaParametriModel {

  /**
   * Questo metodo si occupa di prelevare le schede parametri
   * di un paziente tramite il suo codice fiscale.
   * 
   * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> 
   *        che rappresenta il codice fiscale del paziente.
   * 
   * @return schedeParametri lista di schede di tipo <strong>SchedaParametri</strong>, 
   *         null altrimenti.
   * 
   * @precodition codiceFiscalePaziente != null.
   */
  public static ArrayList<SchedaParametri> getSchedeParametriByCF(String codiceFiscalePaziente) {
    MongoCollection<Document> schedeParametriDB = 
        DriverConnection.getConnection().getCollection("SchedaParametri");
    ArrayList<SchedaParametri> schedeParametri = new ArrayList<SchedaParametri>();
    FindIterable<Document> it  = 
        schedeParametriDB.find(eq("PazienteCodiceFiscale", codiceFiscalePaziente))
        .sort(new BasicDBObject("Data", -1));

    for (Document doc : it) {
      schedeParametri.add(CreaBeanUtility.daDocumentASchedaParametri(doc));
    }

    return schedeParametri;
  }


  /**
   * Questo metodo si occupa di aggiungere una scheda parametri all'interno del database.
   * 
   * @param daAggiungere oggetto di tipo <strong>SchedaParametri</strong> 
   *        che rappresenta la scheda parametri da aggiungere.
   * 
   * @precondition daAggiungere != null.
   */
  public static void addSchedaParametri(SchedaParametri daAggiungere) {
    MongoCollection<Document> scheda = 
        DriverConnection.getConnection().getCollection("SchedaParametri");

    Document doc = new Document("PazienteCodiceFiscale", daAggiungere.getPazienteCodiceFiscale())
        .append("Peso", daAggiungere.getPeso())
        .append("PaMin", daAggiungere.getPaMin())
        .append("PaMax", daAggiungere.getPaMax())
        .append("ScaricoIniziale", daAggiungere.getScaricoIniziale())
        .append("UF", daAggiungere.getUF())
        .append("TempoSosta", daAggiungere.getTempoSosta())
        .append("Carico", daAggiungere.getCarico())
        .append("Scarico", daAggiungere.getScarico())
        .append("Data", daAggiungere.getData());
    scheda.insertOne(doc);
  }


  /**
   * Questo metodo si occupa di ricercare e di restituire tutte le schede parametri 
   * di un paziente comprese in un range di date.
   * 
   * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong>
   *        che rappresenta il codice fiscale del paziente.
   * @param dataInizio oggetto di tipo <strong>LocalDate</strong> 
   *        che rappresenta la data di inizio range.
   * @param dataFine oggetto di tipo <strong>LocalDate</strong> 
   *        che rappresenta la data di fine range.
   * 
   * @return schedeParametri lista di schede di tipo <strong>SchedaParametri</strong>, 
   *         altrimenti null.
   */
  public static ArrayList<SchedaParametri> getReportByPaziente(String codiceFiscalePaziente, 
      LocalDate dataInizio, LocalDate dataFine) {

    MongoCollection<Document> schedeParametriDB = 
        DriverConnection.getConnection().getCollection("SchedaParametri");
    ArrayList<SchedaParametri> schedeParametri = new ArrayList<SchedaParametri>();
    MongoCursor<Document> documenti = schedeParametriDB.find(Filters.and(Filters.gte("Data", dataInizio), Filters.lte("Data",  dataFine), Filters.eq("PazienteCodiceFiscale", codiceFiscalePaziente))).iterator();
    while (documenti.hasNext()) {
      schedeParametri.add(CreaBeanUtility.daDocumentASchedaParametri(documenti.next()));
    }
    return schedeParametri;
  }
}