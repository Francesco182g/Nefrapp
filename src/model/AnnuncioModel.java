package model;

import static com.mongodb.client.model.Filters.eq;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;

import bean.Annuncio;
import utility.CreaBeanUtility;

/**
 * 
 * @author 
 * Questa classe � un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti l'annuncio
 */
public class AnnuncioModel {

  /**
   * Questo metodo si occupa di ricercare un annuncio nel database tramite il suo id.
   * 
   * @param idAnnuncio oggetto di tipo <strong>String</strong> che rappresenta l'id dell'annuncio.
   * 
   * @return annuncio oggetto di tipo <strong>Annuncio</strong>, altrimenti null.
   * 
   * @precondition idAnnuncio != null.
   */
  public static Annuncio getAnnuncioById(String idAnnuncio) {
    MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
    Document annuncioDoc = annunci.find(eq("_id", new ObjectId(idAnnuncio))).first();
    if (annuncioDoc != null) {
      Annuncio annuncio = CreaBeanUtility.daDocumentAAnnuncio(annuncioDoc);
      return annuncio;
    }
    return null;
  }

  /**
   * Questo metodo si occupa di rimuovere un annuncio dal database tramite il suo id.
   * 
   * @param idAnnuncio oggetto di tipo <strong>String</strong>
   *        che rappresenta l'id dell'annuncio da rimuovere.
   *
   * @precondition idAnnuncio != null.
   */
  public static void deleteAnnuncioById(String idAnnuncio) {
    MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
    Document annuncioDoc = annunci.find(eq("_id", new ObjectId(idAnnuncio)))
        .projection(Projections.include("_id")).first();
    if (annuncioDoc != null) {
      annunci.deleteOne(annuncioDoc);
    }
  }

  /**
   * Questo metodo si occupa di aggiungere un annuncio nel database.
   * 
   * @param daAggiungere oggetto di tipo <strong>Annuncio</strong>
   *        che rappresenta l'annuncio da inserire.
   * 
   * @return oggetto di tipo <strong>String</strong> che rappresenta l'id dell'annuncio inserito.
   * 
   * @precondition daAggiungere != null.
   */
  public static String addAnnuncio(Annuncio daAggiungere) {
    MongoCollection<Document> annuncioDB = 
        DriverConnection.getConnection().getCollection("Annuncio");
    ArrayList<Document> pazientiView = new ArrayList<Document>();
    HashMap<String, Boolean> mp = new HashMap<String, Boolean>();
    Iterator it = daAggiungere.getPazientiView().entrySet().iterator();

    if (!it.hasNext()) {
      Document coppia = new Document();
      coppia.append("CFDestinatario", null).append("Visualizzazione", false);
      pazientiView.add(coppia);
    } else {	
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        Document coppia = new Document();
        coppia.append("CFDestinatario", pair.getKey()).append("Visualizzazione", false);
        pazientiView.add(coppia);
      }
    }

    Document allegato = new Document("NomeAllegato", daAggiungere.getNomeAllegato()).
        append("CorpoAllegato", daAggiungere.getCorpoAllegato());

    Document doc = new Document("MedicoCodiceFiscale", daAggiungere.getMedico())
        //.append("PazientiCodiceFiscale", daAggiungere.getPazientiView())
        .append("Titolo", daAggiungere.getTitolo())
        .append("Testo", daAggiungere.getTesto())
        .append("Allegato", allegato)
        .append("Data", daAggiungere.getData().toInstant())
        .append("PazientiView", pazientiView);
    annuncioDB.insertOne(doc);

    ObjectId idObj = (ObjectId)doc.get("_id");
    return idObj.toString();
  }

  /**
   * Questo metodo si occupa di individuare un annuncio nel database tramite il suo id
   * e lo aggiorna con i dati passati in un bean,
   * ignorando i campi null. L'id del bean deve essere valido.
   * 
   * @param daAggiornare oggetto di tipo <strong>Annuncio</strong> 
   * 
   * @preconditions daAggiornare != null && daAggiornare.id != null && daAggiornare.id != ""
   */
  public static void updateAnnuncio(Annuncio daAggiornare) {
    if (daAggiornare == null || daAggiornare.getIdAnnuncio() == null 
        || daAggiornare.getIdAnnuncio() == "") {
      return;
    }

    MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
    BasicDBObject searchQuery = new BasicDBObject();
    searchQuery.append("_id", new ObjectId(daAggiornare.getIdAnnuncio()));

    ArrayList<Document> dView = new ArrayList<Document>();
    Iterator<Entry<String, Boolean>> it = daAggiornare.getPazientiView().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
      Document coppia = new Document();
      coppia.append("CFDestinatario", pair.getKey()).append("Visualizzazione", false);
      dView.add(coppia);
    }

    FindIterable<Document> documents = 
        annunci.find(eq("_id", new ObjectId(daAggiornare.getIdAnnuncio())))
        .projection(Projections.exclude("Allegato"));
    Document d = documents.first();

    if (daAggiornare.getMedico() != null) {
      d.append("MedicoCodiceFiscale", daAggiornare.getMedico());
    }
    if (daAggiornare.getTitolo() != null) {
      d.append("Titolo", daAggiornare.getTitolo());
    }
    if (daAggiornare.getTesto() != null) {
      d.append("Testo", daAggiornare.getTesto());
    }
    if (daAggiornare.getCorpoAllegato() != null && daAggiornare.getNomeAllegato()!= null) {
      Document allegato = 
          new Document("NomeAllegato", daAggiornare.getNomeAllegato())
          .append("CorpoAllegato", daAggiornare.getCorpoAllegato());
      d.append("Allegato", allegato);
    }
    if (daAggiornare.getData() != null) {
      d.append("Data", daAggiornare.getData().toLocalDate());
    }
    if (daAggiornare.getPazientiView() != null) {
      d.append("PazientiView", dView);
    }

    annunci.updateOne(searchQuery, new Document("$set", d));
  }

  /**
   * Questo metodo si occupa di ricercare tutti gli annunci scritti
   *  da un medico tramite il suo codice fiscale.
   * 
   * @param codiceFiscaleMedico oggetto di tipo <strong>String</strong>
   *        che rappresenta il codice fiscale del medico.
   * 
   * @return una lista di annunci di tipo <strong>Annuncio</strong>, altrimenti null.
   * 
   * @precondition codiceFiscaleMedico != null
   */
  public static ArrayList<Annuncio> getAnnunciByCFMedico(String codiceFiscaleMedico) {
    MongoCollection<Document> annuncioDB = 
        DriverConnection.getConnection().getCollection("Annuncio");
    ArrayList<Annuncio> annunci = new ArrayList<>();
    MongoCursor<Document> documenti = 
        annuncioDB.find(eq("MedicoCodiceFiscale", codiceFiscaleMedico))
        .sort(new BasicDBObject("Data", -1))
        .projection(Projections.exclude("Allegato.CorpoAllegato")).iterator();

    while (documenti.hasNext()) {
      annunci.add(CreaBeanUtility.daDocumentAAnnuncioProxy(documenti.next()));
    }

    documenti.close();
    return annunci;
  }

  /**
   * Questo metodo si occupa di ricercare tutti gli annunci destinati 
   * ad un paziente tramite il suo codice fiscale.
   * 
   * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> 
   *        che rappresenta il codice fiscale del paziente.
   * 
   * @return una lista di annunci di tipo <strong>Annuncio</strong>, altrimenti null.
   * 
   * @precondition codiceFiscalePaziente != null
   */
  public static ArrayList<Annuncio> getAnnuncioByCFPaziente(String codiceFiscalePaziente) {
    MongoCollection<Document> annuncioDB =
        DriverConnection.getConnection().getCollection("Annuncio");
    ArrayList<Annuncio> annunci = new ArrayList<>();
    MongoCursor<Document> documenti =
        annuncioDB.find(eq("PazientiView.CFDestinatario", codiceFiscalePaziente)) 
        .sort(new BasicDBObject("Data", -1))
        .projection(Projections.exclude("Allegato.CorpoAllegato")).iterator();

    while (documenti.hasNext()) {
      annunci.add(CreaBeanUtility.daDocumentAAnnuncioProxy(documenti.next()));
    }

    documenti.close();
    return annunci;
  }

  /**
   * Questo metodo si occupa di effettuare il conteggio degli annunci non letti
   *  da un paziente tramite il suo codice fiscale.
   * 
   * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong>
   *        che rappresenta il codice fiscale del paziente.
   * 
   * @return oggetto di tipo <strong>int</strong> che rappresenta il numero di annunci non letti.
   * 
   * @precondition codiceFiscalePaziente != null
   */
  public static int countAnnunciNonLetti(String codiceFiscalePaziente) {

    MongoCollection<Document> annuncioDB = 
        DriverConnection.getConnection().getCollection("Annuncio");
    List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

    obj.add(new BasicDBObject("PazientiView.CFDestinatario", codiceFiscalePaziente));
    obj.add(new BasicDBObject("PazientiView.Visualizzazione", false));
    BasicDBObject andQuery = new BasicDBObject("$and", obj);

    int numeroAnnunci = (int) annuncioDB.count(andQuery);

    return numeroAnnunci;
  }

  /**
   * Questo metodo si occupa di modificare lo stato della lettura dell'annuncio.
   * 
   * @param idAnnuncio oggetto di tipo <strong>String</strong> 
   *        che rappresenta l'identificativo dell'annuncio.
   * @param visualizzato oggetto di tipo <strong>Boolean</strong> 
   *        che rappresenta lo stato di lettura da settare dell'annuncio.
   * 
   * @precondition idAnnuncio != null.
   */
  public static void setVisualizzatoAnnuncio(String idAnnuncio, String CFPaziente, Boolean visualizzato) {
    MongoCollection<Document> annunciDB= 
        DriverConnection.getConnection().getCollection("Annuncio");
    Document updateQuery = new Document();
    Document query = new Document(new BasicDBObject("_id", new ObjectId(idAnnuncio)))
        .append("PazientiView.CFDestinatario", CFPaziente);
    updateQuery.put("PazientiView.$.Visualizzazione", visualizzato);
    annunciDB.updateOne(query, new Document("$set", updateQuery));
  }
  
}