package model;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.PianoTerapeutico;
import utility.CreaBeanUtility;

/**
 * @author nico Questa classe si occupa di contattare il database ed effettuare
 *         tutte le operazioni CRUD relative ai piani terapeutici
 */
public class PianoTerapeuticoModel {

	/**
	 * Metodo che preleva il piano terapeutico di un paziente dal DataBase
	 * 
	 * @param pazienteCodiceFiscale: codice fiscale del paziente
	 * @return PianoTerapeutico: il piano terapeutico trovato
	 * @author nico
	 */
	public static PianoTerapeutico getPianoTerapeuticoByPaziente(String codiceFiscalePaziente) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection()
				.getCollection("PianoTerapeutico");
		PianoTerapeutico piano = null;
		MongoCursor<Document> documenti = pianoTerapeuticoDB.find(eq("PazienteCodiceFiscale", codiceFiscalePaziente))
				.iterator();

		if (documenti.hasNext())
			piano = CreaBeanUtility.daDocumentAPianoTerapeutico(documenti.next());

		documenti.close();
		return piano;
	}

	/**
	 * 
	 * Query che aggiorna il piano terapeutico passato in input
	 * 
	 * @param daAggiornare piano terapeutico da aggiornare
	 */
	public static void updatePianoTerapeutico(PianoTerapeutico daAggiornare) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection()
				.getCollection("PianoTerapeutico");
		BasicDBObject nuovoPianoTerapeutico = new BasicDBObject();
		nuovoPianoTerapeutico.append("$set", new Document().append("Diagnosi", daAggiornare.getDiagnosi()));
		nuovoPianoTerapeutico.append("$set", new Document().append("Farmaco", daAggiornare.getFarmaco()));
		nuovoPianoTerapeutico.append("$set", new Document().append("FineTerapia", daAggiornare.getDataFineTerapia()));
		BasicDBObject searchQuery = new BasicDBObject().append("PazienteCodiceFiscale",
				daAggiornare.getCodiceFiscalePaziente());
		pianoTerapeuticoDB.updateOne(searchQuery, nuovoPianoTerapeutico);
	}
/**
 * Controlla se il piano terapeutico è stato già visualizzato dal paziente
 * @param codiceFiscalePaziente
 * @return boolean true se è stato visualizzato, false altrimenti
 */
	public static boolean isPianoTerapeuticoVisualizzato(String codiceFiscalePaziente) {
		MongoCollection<Document> annunciDB = DriverConnection.getConnection().getCollection("Annuncio");
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente));
		obj.add(new BasicDBObject("Visualizzato", true));
		andQuery.put("$and", obj);
		MongoCursor<Document> documenti = annunciDB.find(andQuery).iterator();
		if (documenti.hasNext()) {
			return true;
		}
		documenti.close();
		return false;
	}

	/**
	 * Cambia lo stato della lettura del piano terapeutico.
	 * 
	 * @param pazienteCodiceFiscale codice fiscale collegato al piano terapeutico che è stato appena aperto
	 * @param visualizzato settaggio del campo "visualizzato" del piano terapeutico appena
	 *                     aperto
	 */
	public static void setVisualizzatoPianoTerapeutico(String codiceFiscalePaziente, Boolean visualizzato) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		pianoTerapeuticoDB.updateOne( new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente),
			    new BasicDBObject("$set", new BasicDBObject("Visualizzato", visualizzato)));
		
	}
}
