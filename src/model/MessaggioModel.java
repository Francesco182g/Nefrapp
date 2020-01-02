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
 * 
 * @author Domenici Musone.
 * Questa classe si occupa di contattare il database ed effettuare tutte leoperazioni CRUD relative ai messaggi.
 */
public class MessaggioModel {

	/**
	 * 
	 * Questo metodo si occupa di aggiungere un messaggio all'interno del database.
	 * 
	 * @param daAggiungere oggetto di tipo <strong>Messaggio</strong> che rappresenta il messaggio da aggiungere.
	 * 
	 * @return oggetto di tipo <strong>String</strong> che rappresenta l'id del messaggio inserito.
	 * 
	 * @precondition daAggiungere != null.
	 */
	public static String addMessaggio(Messaggio daAggiungere) {
		MongoCollection<Document> messaggio = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Document> destinatariView = new ArrayList<Document>();
		Iterator it = daAggiungere.getDestinatariView().entrySet().iterator();

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
	 * 
	 * Questo metodo si occupa di rimuovere un messaggio dal database tramite il suo id.
	 * 
	 * @param idMessaggio oggetto di tipo <strong>String</strong> che rappresenta l'id del messaggio da rimuovere.
	 * 
	 * @precondition idMessaggio != null.
	 */
	public static void deleteMessaggioById(String idMessaggio) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		Document messaggioDoc = messaggi.find(eq("_id", new ObjectId(idMessaggio))).first();
		if (messaggioDoc != null) {
			messaggi.deleteOne(messaggioDoc);
		}
	}

	/**
	 * 
	 * Questo metodo si occupa di ricercare e restituire i messaggi tramite il codice fiscale di un destinatario.
	 * 
	 * @param CFDestinatario oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del destinatario.
	 *
	 * @return una lista di messaggi di tipo <strong>Messaggio</strong>, altrimenti null.
	 * 
	 * @precondition CFDestinatario != null.
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
	 * 
	 * Questo metodo si occupa di ricercare un messaggio nel database tramite il suo id.
	 *
	 * @param idMessaggio oggetto di tipo <strong>String</strong> che rappresenta l'id del messaggio.
	 * 
	 * @return messaggio oggetto di tipo <strong>Messaggio</strong>, altrimenti null.
	 * 
	 * @precondition idMessaggio != null.
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
	 * Questo metodo si occupa di individuare un messaggio nel database tramite il suo id e lo aggiorna con i dati passati,
	 * ignorando i campi null. Se si vuole aggiornare solo alcuni campi, si passi null negli altri.
	 * 
	 * @param id oggetto di tipo <strong>String</strong> che rappresenta l'id del messaggio.
	 * @param codiceFiscaleMittente oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del mittente.
	 * @param oggetto
	 * @param testo oggetto di tipo <strong>String</strong> che rappresenta il testo del messaggio.
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
	 * 
	 * Questo metodo si occupa di modificare lo stato della lettura del messaggio.
	 * 
	 * @param idMessaggio oggetto di tipo <strong>String</strong> che rappresenta l'id del messaggio.
	 * @param visualizzato oggetto di tipo <strong>Boolean</strong> che rappresenta lo stato di lettura da settare del messaggio.
	 * @param CFDestinatario oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del destinatario che ha visualizzato il messaggio.
	 * 
	 * @precondition idMessaggio != null && CFDestinatario != null.
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
	 * 
	 * Questo metodo si occupa di effettuare il conteggio dei messaggi non letti da un destinatario tramite il suo codice fiscale.
	 * 
	 * @param CFDestinatario oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del destinatario.
	 * 
	 * @return oggetto di tipo <strong>int</strong> che rappresenta il numero di messaggi non letti.
	 * 
	 * @precondition CFDestinatario != null.
	 */
	public static int countMessaggiNonLetti(String CFDestinatario) {

		MongoCollection<Document> messaggioDB = DriverConnection.getConnection().getCollection("Messaggio");
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("DestinatariView.CFDestinatario", CFDestinatario));
		obj.add(new BasicDBObject("DestinatariView.Visualizzazione", false));
		BasicDBObject andQuery = new BasicDBObject("$and", obj);
		int numeroMessaggi = (int) messaggioDB.count(andQuery);
		return numeroMessaggi;
	}
}