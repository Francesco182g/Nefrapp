package model;

import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import bean.Amministratore;
import utility.CreaBeanUtility;

/**
 * 
 * @author Luca Esposito, Eugenio Corbisiero
 * Questa classe � un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti l'amministratore
 */
public class AmministratoreModel {
	
	/**
	 * 
	 * Questo metodo si occupa di verificare se i dati immessi dall'amministratore per effettuare il login sono presenti nel database.
	 *
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale dell'amministratore.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password dell'amministratore.
	 * 
	 * @return un oggetto di tipo <strong>Amministratore</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null && password != null .
	 */
	public static Amministratore getAmministratoreByCFPassword(String codiceFiscale, String password) {
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
	 * 
	 * Questo metodo si occupa di ottenre la password di un amministratore tramite codice fiscale .
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale dell'amministratore.
	 * 
	 * @return password oggetto di tipo <strong>String</strong> che rappresenta la password dell'amministratore.
	 * 
	 * @precondition codiceFiscale != null.
	 */
	public static String getPassword(String codiceFiscale) {
		MongoCollection<Document> amministratori = DriverConnection.getConnection().getCollection("Amministratore");
		Document datiAmministratore = amministratori.find(eq("CodiceFiscale", codiceFiscale)).first();
		String password = datiAmministratore.getString("Password");
		return password;
	}
	
	/**
	 * 
	 * Questo metodo si occupa di aggiornare i dati dell'amministratore.
	 * 
	 * @param daAggiornare oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale dell'amministratore.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password dell'amministratore.
	 * 
	 * @precondition daAggiornare != null && password != null.
	 */
	public static void updateAmministratore(String daAggiornare,String password) {
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		BasicDBObject nuovoAmministratore = new BasicDBObject();
		nuovoAmministratore.append("$set", new Document().append("Password", password));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare);
		amministratore.updateOne(searchQuery, nuovoAmministratore);
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare un amministratore tramite codice fiscale.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale dell'amministratore.
	 * 
	 * @return amministratore oggetto di tipo <strong>Amministratore</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null.
	 */
	public static Amministratore getAmministratoreByCF(String codiceFiscale) {
		
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		Amministratore admin = null;
		Document datiAmministratore = amministratore.find(eq("CodiceFiscale", codiceFiscale)).first();
		if(datiAmministratore != null)
			admin = CreaBeanUtility.daDocumentAdAmministratore(datiAmministratore);
		
		return admin;
	}
}