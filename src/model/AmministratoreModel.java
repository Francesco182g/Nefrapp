package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Amministratore;

/**
 * 
 * @author Eugenio
 *
 */
public class AmministratoreModel {
	/**
	 * Metodo che effettua il login per l'amministratore
	 * @param codiceFiscale codice fiscale dell'amministratore
	 * @param password password dell'amministratore
	 * @return dati dell'amministratore se le credenziali sono corrette, null altrimenti
	 */
	public static Amministratore checkLogin(String codiceFiscale, String password) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		Amministratore amministratore = null;
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("CodiceFiscale", codiceFiscale));
		obj.add(new BasicDBObject("Password", password));
		andQuery.put("$and", obj);
		Document datiPaziente = pazienti.find(andQuery).first();
		if(datiPaziente != null) {
			amministratore = new Amministratore();
			amministratore.setCodiceFiscale(codiceFiscale);
			amministratore.setNome(datiPaziente.getString("Nome"));
			amministratore.setCognome(datiPaziente.getString("Cognome"));
			amministratore.setEmail("Email");
			
		}
		
		return amministratore;
	}
	

}
