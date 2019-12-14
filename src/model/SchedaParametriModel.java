package model;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.SchedaParametri;
import utility.CreaBeanUtility;

public class SchedaParametriModel {
	/**
	 * Metodo che preleva le schede parametri di un paziente dal DataBase
	 * @param pazienteCodiceFiscale codice fiscale del paziente
	 * @return schedeParametri array di schede di un paziente, null altrimenti
	 */
	public static ArrayList<SchedaParametri> getSchedaParametriByCF(String codiceFiscalePaziente) {
		MongoCollection<Document> schedeParametriDB = DriverConnection.getConnection().getCollection("SchedaParametri");
		ArrayList<SchedaParametri> schedeParametri= new ArrayList<SchedaParametri>();
		MongoCursor<Document> documenti = schedeParametriDB.find(eq("PazienteCodiceFiscale", codiceFiscalePaziente)).iterator();
		
		while(documenti.hasNext()) {
			schedeParametri.add(CreaBeanUtility.daDocumentASchedaParametri(documenti.next()));
		}
		return schedeParametri;
	}

	public static void addSchedaParametri(SchedaParametri daAggiungere) 
	{
		MongoCollection<Document> scheda = DriverConnection.getConnection().getCollection("SchedaParametri");
		
		Document doc = new Document("PazienteCodiceFiscale", daAggiungere.getPazienteCodiceFiscale())
				.append("Peso", daAggiungere.getPeso())
				.append("PaMin", daAggiungere.getPaMin())
				.append("PaMax", daAggiungere.getPaMax())
				.append("ScaricoIniziale", daAggiungere.getScaricoIniziale())
				.append("UF", daAggiungere.getUF())
				.append("TempoSosta", daAggiungere.getTempoSosta())
				.append("Carico", daAggiungere.getCarico())
				.append("Scarico", daAggiungere.getScarico())
				.append("Date", daAggiungere.getData());
		scheda.insertOne(doc);	
	}
}
