package model;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import bean.Messaggio;
import bean.Paziente;
import utility.AlgoritmoCriptazioneUtility;
import utility.CreaBeanUtility;

/**
 * Questa classe si occupa di contattare il database ed effettuare tutte le
 * operazioni CRUD relative ai messaggi
 */
public class MessaggioModel {

	/**
	 * 
	 * Metodo che aggiunge un messaggio al database
	 * 
	 * @param daAggiungere messaggio da aggiungere
	 * @author nico
	 */
	public static void addMessaggio(Messaggio daAggiungere) {
		MongoCollection<Document> messaggio = DriverConnection.getConnection().getCollection("Messaggio");

		Document doc = new Document("MittenteCodiceFiscale", daAggiungere.getCodiceFiscaleMittente())
				.append("DestinatarioCodiceFiscale", daAggiungere.getCodiceFiscaleDestinatario())
				.append("Oggetto", daAggiungere.getOggetto())
				.append("Testo", daAggiungere.getTesto())
				.append("Allegato", daAggiungere.getAllegato())
				.append("Data", daAggiungere.getData().toInstant())
				.append("Visualizzato", daAggiungere.getVisualizzato());
		messaggio.insertOne(doc);
	}

	/**
	 * 
	 * Metodo che prende dal database una lista di messaggi inviati da un
	 * determinato utente
	 * 
	 * @param codiceFiscaleMittente codice fiscale del mittente
	 * @author nico
	 */
	public static ArrayList<Messaggio> getMessaggiByMittente(String codiceFiscaleMittente) {
		MongoCollection<Document> messaggioDB = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Messaggio> messaggi = new ArrayList<>();
		MongoCursor<Document> documenti = messaggioDB.find(eq("MittenteCodiceFiscale", codiceFiscaleMittente))
				.iterator();

		while (documenti.hasNext())
			messaggi.add(CreaBeanUtility.daDocumentAMessaggio(documenti.next()));

		return messaggi;
	}

	/**
	 * Metodo che ricerca i messaggi per codice fiscale dei destinatari
	 * 
	 * @param CFDestinatario Codice Fiscale del destinatario
	 * @return messaggio se trovato almeno un messaggio, altrimenti null
	 */
	public static ArrayList<Messaggio> getMessaggioByCFDestinatario(String CFDestinatario) {
		MongoCollection<Document> messaggiDB = DriverConnection.getConnection().getCollection("Messaggio");
		ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();
		MongoCursor<Document> documenti = messaggiDB.find(eq("DestinatarioCodiceFiscale", CFDestinatario)).iterator();

		while (documenti.hasNext()) {
			messaggi.add(CreaBeanUtility.daDocumentAMessaggio(documenti.next()));
		}

		return messaggi;
	}

	public static Messaggio getMessaggioById(String idMessaggio) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		Document messaggioDoc = messaggi.find(eq("_id", new ObjectId(idMessaggio))).first();
		if (messaggioDoc != null) {
			Messaggio messaggio = CreaBeanUtility.daDocumentAMessaggio(messaggioDoc);
			return messaggio;
		}
		return null;
	}

	/**
	 * Cambia lo stato della lettura del messaggio da false a true.
	 * 
	 * @param idMessaggio  id del messaggio che è stato appena aperto
	 * @param visualizzato settaggio a true del campo "visualizzato" del messaggio
	 *                     appena aperto
	 */
	public static void updateMessaggio(String idMessaggio, Boolean visualizzato) {
		MongoCollection<Document> messaggi = DriverConnection.getConnection().getCollection("Messaggio");
		messaggi.updateOne( new BasicDBObject("_id", new ObjectId(idMessaggio)),
			    new BasicDBObject("$set", new BasicDBObject("Visualizzato", visualizzato)));
	}

}
