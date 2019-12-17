package model;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import bean.Medico;
import bean.Paziente;
import utility.CreaBeanUtility;

public class MedicoModel {

	/**
	 * Metodo che effettua il login per il medico
	 * @param codiceFiscale codice fiscale del medico
	 * @param password password del medico
	 * @return dati del medico se le credenziali sono corrette, null altrimenti
	 */
	public static Medico checkLogin(String codiceFiscale, String password) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Medico medico = null;
		BasicDBObject andQuery = new BasicDBObject();
		
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("CodiceFiscale", codiceFiscale));
		obj.add(new BasicDBObject("Password", password));
		andQuery.put("$and", obj);
		
		Document datiMedico = medici.find(andQuery).first();
		if(datiMedico != null) {
			medico = CreaBeanUtility.daDocumentAMedico(datiMedico);
		}
		
		return medico;
	}
	
	/**
	 * Metodo che permette l'aggiunta di un medico al database
	 * @param toAdd medico da aggiungere
	 * @param password password del medico
	 */
	public static void addMedico(Medico toAdd, String password) {
		MongoCollection<Document> medico = DriverConnection.getConnection().getCollection("Medico");
		
		Document doc = new Document("CodiceFiscale", toAdd.getCodiceFiscale())
				.append("Nome", toAdd.getNome())
				.append("Cognome", toAdd.getCognome())
				.append("Password",password)
				.append("DataDiNascita", toAdd.getDataDiNascita())
				.append("Sesso", toAdd.getSesso())
				.append("Residenza", toAdd.getResidenza())
				.append("Email", toAdd.getEmail());
		medico.insertOne(doc);	
	}
	
	/**
	 * Query che ricerca un medico per codice fiscale
	 * @param codiceFiscale del medico da ricercare
	 * @return medico se trovato, altrimenti null
	 */
	public static Medico getMedicoByCF(String codiceFiscale) {
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Medico medico = null;
		Document datiMedico = medici.find(eq("CodiceFiscale", codiceFiscale)).first();
		if(datiMedico != null)
			medico = CreaBeanUtility.daDocumentAMedico(datiMedico);
		
		return medico;
	}

	
}
