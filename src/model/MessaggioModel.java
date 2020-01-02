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
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import bean.Messaggio;
import utility.CreaBeanUtility;

/**
 * Questa classe si occupa di contattare il database ed effettuare tutte le
 * operazioni CRUD relative ai messaggi
 */
/**
 * @author nico
 *
 */
public class MessaggioModel {

	/**
	 * 
	 * Metodo che aggiunge un messaggio al database
	 * 
	 * @param daAggiungere messaggio da aggiungere
	 * @return String con l'id del messaggio appena inserito
	 * @author nico
	 */
	public static String addMessaggio(Messaggio daAggiungere) {
		MongoCollection<Document> messaggio = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Document> destinatariView = new ArrayList<Document>();
		Iterator it = daAggiungere.getDestinatariView().entrySet().iterator();
		
		if (!it.hasNext()) {
			Document coppia = new Document();
			coppia.append("CFDestinatario", null).append("Visualizzazione", false);
			destinatariView.add(coppia);
		}

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Document coppia = new Document();
			coppia.append("CFDestinatario", pair.getKey()).append("Visualizzazione", false);
			destinatariView.add(coppia);
		}

		Document allegato = new Document("NomeAllegato", daAggiungere.getNomeAllegato()).append("CorpoAllegato",
				daAggiungere.getCorpoAllegato());

		Document doc = new Document("MittenteCodiceFiscale", daAggiungere.getCodiceFiscaleMittente())
				.append("Oggetto", daAggiungere.getOggetto()).append("Testo", daAggiungere.getTesto())
				.append("Allegato", allegato).append("Data", daAggiungere.getData().toInstant())
				.append("DestinatariView", destinatariView);
		messaggio.insertOne(doc);
		
		ObjectId idObj = (ObjectId)doc.get("_id");
		return idObj.toString();
	}
	
	/**
	 * Cancella un annuncio dal database
	 * @param idAnnuncio
	 * @return
	 */
	public static void deleteMessaggioById(String idMessaggio) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		Document messaggioDoc = messaggi.find(eq("_id", new ObjectId(idMessaggio)))
				.projection(Projections.include("_id")).first();
		if (messaggioDoc != null) {
			messaggi.deleteOne(messaggioDoc);
		}
	}

	/**
	 * Metodo che ricerca i messaggi per codice fiscale dei destinatari
	 * 
	 * @param CFDestinatario Codice Fiscale del destinatario
	 * @return messaggi: ArrayList contenente i messaggi trovati
	 */

	public static ArrayList<Messaggio> getMessaggiByDestinatario(String CFDestinatario) {
		
		MongoCollection<Document> messaggioDB = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Messaggio> messaggi = new ArrayList<>();
		FindIterable<Document> it = messaggioDB.find(eq("DestinatariView.CFDestinatario", CFDestinatario)).
				sort(new BasicDBObject("Data", -1)).projection(Projections.include("MittenteCodiceFiscale", "Oggetto", "Data", "DestinatariView"));
		
		for (Document doc : it) {
			messaggi.add(CreaBeanUtility.daDocumentAMessaggioProxy(doc, CFDestinatario));
		}
		
		return messaggi;
	}

	/**
	 * Metodo che ricerca un messaggio nel database per id
	 * 
	 * @param idMessaggio: id del messaggio
	 * @return messaggio: risultato della ricerca, vale null se non si trovano
	 *         corrispondenze
	 */
	public static Messaggio getMessaggioById(String idMessaggio) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		Document messaggioDoc = messaggi.find(eq("_id", new ObjectId(idMessaggio))).first();
		if (messaggioDoc != null) {
			Messaggio messaggio = CreaBeanUtility.daDocumentAMessaggio(messaggioDoc, null);
			//valuta se mettere metodo overloaded per evitare il parametro null
			return messaggio;
		}
		return null;
	}
	
	
	/**
	 * Metodo che individua un messaggio nel database per id e lo aggiorna con i dati passati,
	 * ignorando i campi null. Se si vuole aggiornare solo alcuni campi, si passi null negli altri.
	 * 
	 * @param id
	 * @param codiceFiscaleMittente
	 * @param oggetto
	 * @param testo
	 * @param corpoAllegato
	 * @param nomeAllegato
	 * @param data
	 * @param destinatariView
	 */
	public static void updateMessaggio (String id, String codiceFiscaleMittente, String oggetto,
			String testo, String corpoAllegato, String nomeAllegato, ZonedDateTime data,
			HashMap<String, Boolean> destinatariView) {
		
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.append("_id", new ObjectId(id));
		
		ArrayList<Document> dView = new ArrayList<Document>();
		Iterator<Entry<String, Boolean>> it = destinatariView.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			Document coppia = new Document();
			coppia.append("CFDestinatario", pair.getKey()).append("Visualizzazione", false);
			dView.add(coppia);
		}
		
		FindIterable<Document> documents = messaggi.find(eq("_id", new ObjectId(id))).projection(Projections.exclude("Allegato"));
		Document d = documents.first();
		
		if (codiceFiscaleMittente!=null) {
			d.append("MittenteCodiceFiscale", codiceFiscaleMittente);
		}
		if (oggetto!=null) {
			d.append("Oggetto", oggetto);
		}
		if (testo!=null) {
			d.append("Testo", testo);
		}
		if (corpoAllegato!=null && nomeAllegato!=null) {
			Document allegato = new Document("NomeAllegato", nomeAllegato).append("CorpoAllegato", corpoAllegato);
			d.append("Allegato", allegato);
		}
		if (data!=null) {
			d.append("Data", data.toLocalDate());
		}
		if (destinatariView!=null) {
			d.append("DestinatariView", dView);
		}
		
		messaggi.updateOne(searchQuery, new Document("$set", d));
	}

	/**
	 * Cambia lo stato della lettura del messaggio.
	 * 
	 * @param idMessaggio    id del messaggio che è stato appena aperto
	 * @param visualizzato   settaggio a true del campo "visualizzato" del messaggio
	 *                       appena aperto
	 * @param CFDestinatario codiceFiscale dell'utente che ha visualizzato il
	 *                       messaggio
	 */
	public static void setVisualizzatoMessaggio(String idMessaggio, String CFDestinatario, Boolean visualizzato) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		Document updateQuery = new Document();
		Document query = new Document(new BasicDBObject("_id", new ObjectId(idMessaggio)))
				.append("DestinatariView.CFDestinatario", CFDestinatario);
		updateQuery.put("DestinatariView.$.Visualizzazione", visualizzato);
		messaggi.updateOne(query, new Document("$set", updateQuery));

		/*
		 * messaggi.updateOne(new BasicDBObject("_id", new
		 * ObjectId(idMessaggio)).append("DestinatariView.CFDestinatario",
		 * CFDestinatario), new BasicDBObject("$set", new
		 * BasicDBObject("DestinatariView.Visualizzazione", visualizzato)));
		 */
	}

	/**
	 * Conta quanti messaggi non sono stati letti da un determinato destinatario
	 * 
	 * @param CFDestinatario
	 * @return n il numero di messaggi che non sono stati letti
	 */
	public static int countMessaggiNonLetti(String CFDestinatario) {

		MongoCollection<Document> messaggioDB = DriverConnection.getConnection().getCollection("Messaggio");
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("DestinatariView.CFDestinatario", CFDestinatario));
		obj.add(new BasicDBObject("DestinatariView.Visualizzazione", false));
		BasicDBObject andQuery = new BasicDBObject("$and", obj);
		int n = (int) messaggioDB.count(andQuery);
		return n;
	}

}
