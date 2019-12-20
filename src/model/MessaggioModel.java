package model;


import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * 
 * @author Sara
 *
 */
import bean.Messaggio;
public class MessaggioModel {
	/**
	 * Metodo che permette l'aggiunta di un messaggio al database
	 * @param messsaggio messaggio da aggiungere
	 */
	public static void addMessaggio(Messaggio toAdd) {
		MongoCollection<Document> messaggio = DriverConnection.getConnection().getCollection("Messaggio");
		Document doc = new Document("MittenteCodiceFiscale", toAdd.getCodiceFiscaleMittente())
				.append("DestinatarioCodiceFiscale", toAdd.getCodiceFiscaleDestinatario())
				.append("Oggetto", toAdd.getOggetto())
				.append("Testo",toAdd.getTesto())
				.append("Allegato", toAdd.getAllegato())
				.append("Data", toAdd.getData());
		messaggio.insertOne(doc);	
	}
	/**
	 * Query che ricerca i messaggi per codice fiscale dei destinatari
	 * @param CFDestinatario Codice Fiscale del destinatario
	 * @return messaggio se trovato almeno un messaggio, altrimenti null
	 */
	public static ArrayList<Messaggio> getMessaggioByCFDestinatario(String CFDestinatario) {
		ArrayList<Messaggio> messaggio=new ArrayList<Messaggio>();
		//fai cose
		return messaggio;
	}
	
	
}
