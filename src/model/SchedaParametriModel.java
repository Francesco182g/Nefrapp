package model;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.Paziente;
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
}
