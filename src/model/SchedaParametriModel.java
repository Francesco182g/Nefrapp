package model;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
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
		documenti.close();
		return schedeParametri;
	}

	
	/**
	 * 
	 * Query che aggiunge una scheda parametri al database
	 * @param daAggiungere scheda da aggiungere
	 */
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
				.append("Data", daAggiungere.getData());
		scheda.insertOne(doc);	
	}
	
	
	/**
	 * 
	 * Query che prende tutte le schede per un dato range di date di un paziente
	 * @param codiceFiscalePaziente codice fiscale del paziente di cui si vuole ottenere il report
	 * @param dataInizio data di inizio da cui generare il report
	 * @param dataFine data di fine da cui generare il report
	 * @return ArrayList contenente tutte le schede del report richiest
	 */
	public static ArrayList<SchedaParametri> getReportByPaziente(String codiceFiscalePaziente, LocalDate dataInizio, LocalDate dataFine){
		
		MongoCollection<Document> schedeParametriDB = DriverConnection.getConnection().getCollection("SchedaParametri");
		ArrayList<SchedaParametri> schedeParametri = new ArrayList<SchedaParametri>();
		MongoCursor<Document> documenti = schedeParametriDB.find(Filters.and(Filters.gte("Data", dataInizio), Filters.lte("Data",  dataFine), Filters.eq("PazienteCodiceFiscale", codiceFiscalePaziente))).iterator();
		while(documenti.hasNext()) {
			schedeParametri.add(CreaBeanUtility.daDocumentASchedaParametri(documenti.next()));
		}
		return schedeParametri;
	}
	

	
}
