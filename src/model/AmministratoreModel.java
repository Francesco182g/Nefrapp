package model;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Amministratore;
import utility.AlgoritmoCriptazioneUtility;
import utility.CreaBeanUtility;

/**
 * 
 * @author Luca Esposito, Eugenio Corbisiero
 * Questa classe è un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti l'amministratore
 */
public class AmministratoreModel {
	/**
	 * 
	 * Metodo che effettua il login per l'amministratore
	 * @param codiceFiscale codice fiscale dell'amministratore
	 * @param password password dell'amministratore
	 * @return dati dell'amministratore se le credenziali sono corrette, null altrimenti
	 */
	public static Amministratore checkLogin(String codiceFiscale, String password) {
		MongoCollection<Document> amministratori = DriverConnection.getConnection().getCollection("Amministratore");
		Amministratore amministratore = null;
		BasicDBObject andQuery = new BasicDBObject();
		
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("CodiceFiscale", codiceFiscale));
		obj.add(new BasicDBObject("Password", password));
		andQuery.put("$and", obj);
		
		Document datiAmministratore = amministratori.find(andQuery).first();
		if(datiAmministratore != null) {
			amministratore = CreaBeanUtility.daDocumentAdAmministratore(datiAmministratore);
		}
		return amministratore;
	}
	
	/**
	 * Query che ricerca la password dell'amministratore
	 * @param codiceFiscale dell'amministratore
	 * @return password dell'amministratore
	 */
	public static String getPassword(String codiceFiscale) {
		MongoCollection<Document> amministratori = DriverConnection.getConnection().getCollection("Amministratore");
		Document datiAmministratore = amministratori.find(eq("CodiceFiscale", codiceFiscale)).first();
		String password=datiAmministratore.getString("Password");
		return password;
	}
	
	/**
	 * Query che aggiorna l'amministratore
	 * @param daAggiornare amministratore
	 * @param password aggiornata
	 */
	public static void updateAmministratore(String daAggiornare,String password) {
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		BasicDBObject nuovoAmministratore = new BasicDBObject();
		nuovoAmministratore.append("$set", new Document().append("Password", password));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare);
		amministratore.updateOne(searchQuery, nuovoAmministratore);
	}
		
}