package model;
import static com.mongodb.client.model.Filters.eq;



import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.PianoTerapeutico;
import utility.CreaBeanUtility;
/**
 * @author nico
 * Questa classe si occupa di contattare il database 
 * ed effettuare tutte le operazioni CRUD relative ai piani terapeutici
 */
public class PianoTerapeuticoModel {
	
	
	/**
	 * Metodo che preleva il piano terapeutico di un paziente dal DataBase
	 * @param pazienteCodiceFiscale: codice fiscale del paziente
	 * @return PianoTerapeutico: il piano terapeutico trovato 
	 * @author nico
	 */
	public static PianoTerapeutico getPianoTerapeuticoByPaziente(String codiceFiscalePaziente){
		MongoCollection<Document> pianoTerapeuticoDB = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		PianoTerapeutico piano = null;
		MongoCursor<Document> documenti = pianoTerapeuticoDB.find(eq("PazienteCodiceFiscale", codiceFiscalePaziente)).iterator();
		
		if (documenti.hasNext())
			piano = CreaBeanUtility.daDocumentAPianoTerapeutico(documenti.next());
		
		return piano;
	}
}
