package model;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;

import bean.Medico;
import bean.PianoTerapeutico;
import utility.CreaBeanUtility;

/**
 * 
 * @author Domenico Musono
 * Questa classe si occupa di contattare il database ed effettuare tutte le operazioni CRUD relative ai piani terapeutici
 */
public class PianoTerapeuticoModel {
	/**
	 * 
	 * Questo metodo consente di aggiungere un piano terapeutico al database.
	 * 
	 * @param daAggiungere oggetto di tipo <strong>PianoTerapeutico</strong> che rappresenta il piano terapeutico da aggiungere.
	 * 
	 * @precondition daAggiungere != null.
	 */
	public static void addPianoTerapeutico(PianoTerapeutico daAggiungere) {
		MongoCollection<Document> pianoTerapeutico = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		
		Document doc = new Document("PazienteCodiceFiscale", daAggiungere.getCodiceFiscalePaziente())
				.append("Diagnosi", daAggiungere.getDiagnosi())
				.append("Farmaco",daAggiungere.getFarmaco())
				.append("FineTerapia", daAggiungere.getDataFineTerapia())
				.append("Visualizzato", daAggiungere.getVisualizzato());
		pianoTerapeutico.insertOne(doc);	
	}

	/**
	 * Questo metodo si occupa di ricercare il piano terapeutico di un paziente tramite il suo codice fiscale.
	 * 
	 * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * 
	 * @return piano: oggetto di tipo <strong>PianoTerapeutico</strong>, null altrimenti.
	 * 
	 * @precondition codiceFiscalePaziente != null.
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
	 * Questo metodo si occupa di aggiornare il piano terapeutico
	 * 
	 * @param daAggiornare oggetto di tipo <strong>PianoTerapeutico</strong> che include i nuovi dati del piano terapeutico.
	 * 
	 * @precondition daAggiornare != null.
	 */
	public static void updatePianoTerapeutico(PianoTerapeutico daAggiornare) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection()
				.getCollection("PianoTerapeutico");
		
		FindIterable<Document> documents = pianoTerapeuticoDB.find(eq("PazienteCodiceFiscale", daAggiornare.getCodiceFiscalePaziente()));
		Document d = documents.first();
		
		d.append("Diagnosi", daAggiornare.getDiagnosi())
		.append("Farmaco", daAggiornare.getFarmaco())
		.append("FineTerapia", daAggiornare.getDataFineTerapia())
		.append("Visualizzato", false);
		
		BasicDBObject searchQuery = new BasicDBObject().append("PazienteCodiceFiscale",
				daAggiornare.getCodiceFiscalePaziente());
		pianoTerapeuticoDB.replaceOne(searchQuery, d);
	}
	
	/**
	 * 
	 * Questo metodo si occupa di controllare se il piano terapeutico di un paziente � stato visualizzato.
	 * 
	 * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * 
	 * @return oggetto di tipo <strong>Boolena</strong> che indica se � stato visualizzato (true) oppure no (false).
	 * 
	 * @precondition codiceFiscalePaziente != null.
	 */
	public static boolean isPianoTerapeuticoVisualizzato(String codiceFiscalePaziente) {
		MongoCollection<Document> annunciDB = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente));
		obj.add(new BasicDBObject("Visualizzato", false));
		BasicDBObject andQuery = new BasicDBObject("$and", obj);
		int n = (int) annunciDB.count(andQuery);
		if(n == 1) {
			return false;
		}
		else {
			return true;
		}
		/*BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente));
		obj.add(new BasicDBObject("Visualizzato", true));
		andQuery.put("$and", obj);
		MongoCursor<Document> documenti = annunciDB.find(andQuery).iterator();
		if (documenti.hasNext()) {
			return true;
		}
		documenti.close();
		return false;*/
	}

	/**
	 * 
	 * Questo metodo si occupa di modificare lo stato della visualizzazione del piano terapeutico.
	 
	 * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * @param visualizzato oggetto di tipo <strong>Boolean</strong> che rappresenta lo stato di visualizzazione da settare del piano terapeutico.
	 * 
	 * @precondition codiceFiscalePaziente != null.
	 */
	public static void setVisualizzatoPianoTerapeutico(String codiceFiscalePaziente, Boolean visualizzato) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		pianoTerapeuticoDB.updateOne( new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente),
			    new BasicDBObject("$set", new BasicDBObject("Visualizzato", visualizzato)));
		
	}
	
	/**
	 * 
	 * Questo metodo consente di rimuovere un piano terapeutico al database.
	 * 
	 * @param codiceFiscalePaziente oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente al quale è collegato il piano terapeutico da rimuovere.
	 * 
	 * @precondition codiceFiscalePaziente != null.
	 */
	public static void removePianoTerapeutico(String codiceFiscalePaziente) {
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		BasicDBObject document = new BasicDBObject();
		document.put("PazienteCodiceFiscale", codiceFiscalePaziente);
		pianoTerapeuticoDB.deleteOne(document);
		
	}
	
}