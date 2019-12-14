package model;

import java.util.ArrayList; 
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import bean.Paziente;
import utility.CreaBeanUtility;

import static com.mongodb.client.model.Filters.*;

/**
 * 
 * @author Antonio
 * 
 */

public class PazienteModel {

	/**
	 * Metodo che effettua il login per il paziente
	 * @param codiceFiscale codice fiscale del paziente
	 * @param password password del paziente
	 * @return dati del paziente se le credenziali sono corrette, null altrimenti
	 */
	public static Paziente checkLogin(String codiceFiscale, String password) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		Paziente paziente = null;
		BasicDBObject andQuery = new BasicDBObject();
		
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("CodiceFiscale", codiceFiscale));
		obj.add(new BasicDBObject("Password", password));
		andQuery.put("$and", obj);
		
		Document datiPaziente = pazienti.find(andQuery).first();
		if(datiPaziente != null) {
			paziente = CreaBeanUtility.daDocumentAPaziente(datiPaziente);
		}
		
		return paziente;
	}
	
	
	/**
	 * Metodo che ottiene tutti i dati dei pazienti seguiti da un medico
	 * @param codiceFiscaleMedico codice fiscale del medico che richiede i dati dei pazienti
	 * @return pazienti seguiti dal medico
	 */
	public static ArrayList<Paziente> getPazientiSeguiti(String codiceFiscaleMedico) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		ArrayList<Paziente> pazientiMedico = new ArrayList<Paziente>();
		MongoCursor<Document> documenti = pazienti.find(eq("Medici", codiceFiscaleMedico)).iterator();
		
		while(documenti.hasNext()) {
			pazientiMedico.add(CreaBeanUtility.daDocumentAPaziente(documenti.next()));
		}
		
		return pazientiMedico;	
	}
	
	/**
	 * Metodo che permette l'aggiunta di un paziente al database
	 * @param toAdd paziente da aggiungere
	 * @param password password del paziente
	 */
	public static void addPaziente(Paziente toAdd,String password) {
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		
		Document doc = new Document("CodiceFiscale", toAdd.getCodiceFiscale())
				.append("Nome", toAdd.getNome())
				.append("Cognome", toAdd.getCognome())
				.append("Password",password)
				.append("DataDiNascita", toAdd.getDataDiNascita())
				.append("Sesso", toAdd.getSesso())
				.append("Residenza", toAdd.getResidenza())
				.append("Email", toAdd.getEmail());
		paziente.insertOne(doc);	
	}
	
}
