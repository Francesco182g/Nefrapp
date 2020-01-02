package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import bean.Paziente;
import utility.CriptazioneUtility;
import utility.CreaBeanUtility;

import static com.mongodb.client.model.Filters.*;

/**
 * 
 * @author Antonio Donnarumma, Luca Esposito.
 * Questa classe è un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti il paziente.
 */

public class PazienteModel {

	/**
	 * 
	 * Questo metodo si occupa di verificare se i dati immessi dal paziente per effettuare il login sono presenti nel database.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password del paziente.
	 * 
	 * @return un oggetto di tipo <strong>Paziente</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null && password != null .
	 */
	public static Paziente getPazienteByCFPassword(String codiceFiscale, String password) {
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
	 * 
	 * Questo metodo si occupa di restituire i pazienti seguiti da un medico.
	 * 
	 * @param codiceFiscaleMedico oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del medico.
	 * 
	 * @return una lista di pazienti di tipo <strong>Paziente</strong>, altrimenti null.
	 */
	public static ArrayList<Paziente> getPazientiSeguiti(String codiceFiscaleMedico) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		ArrayList<Paziente> pazientiMedico = new ArrayList<Paziente>();
		MongoCursor<Document> documenti = pazienti.find(eq("Medici", codiceFiscaleMedico)).iterator();
		
		while(documenti.hasNext()) {
			pazientiMedico.add(CreaBeanUtility.daDocumentAPaziente(documenti.next()));
		}
		
		documenti.close();
		return pazientiMedico;	
	}
	
	/**
	 * 
	 * Questo metodo consente di aggiungere un paziente al database.
	 * 
	 * @param daAggiungere oggetto di tipo <strong>Paziente</strong> che rappresenta il paziente da aggiungere.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password del paziente da aggiungere.
	 * 
	 * @precondition daAggiungere != null && password != null.
	 */
	public static void addPaziente(Paziente daAggiungere,String password) {
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		
		Document doc = new Document("CodiceFiscale", daAggiungere.getCodiceFiscale())
				.append("Nome", daAggiungere.getNome())
				.append("Cognome", daAggiungere.getCognome())
				.append("Password",password)
				.append("DataDiNascita", daAggiungere.getDataDiNascita())
				.append("Sesso", daAggiungere.getSesso())
				.append("Residenza", daAggiungere.getResidenza())
				.append("LuogoDiNascita", daAggiungere.getLuogoDiNascita())
				.append("Email", daAggiungere.getEmail());
		paziente.insertOne(doc);	
	}
		
	/**
	 * 
	 * Questo metodo si occupa di ricercare un paziente tramite codice fiscale.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * 
	 * @return paziente oggetto di tipo <strong>Paziente</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null.
	 */
	public static Paziente getPazienteByCF(String codiceFiscale) {
		
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		Paziente paziente = null;
		Document datiPaziente = pazienti.find(eq("CodiceFiscale", codiceFiscale)).first();
		if(datiPaziente != null)
			paziente = CreaBeanUtility.daDocumentAPaziente(datiPaziente);
		
		return paziente;
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare l'ID di un paziente tramite codice fiscale.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente.
	 * 
	 * @return pazienteID oggetto di tipo <strong>String</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null.
	 */
	public static String getIdPazienteByCF(String codiceFiscale)
	{
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		
		Document datiPaziente = pazienti.find(eq("CodiceFiscale", codiceFiscale)).first();
		
		
		if(datiPaziente != null) {
			String pazienteID=CriptazioneUtility.criptaConMD5(datiPaziente.getObjectId("_id").toString());
			return pazienteID;
		}
		return null;
	}
	
	/**
	 * 
	 * Questo metodo si occupa di rimuovere un paziente dal database.
	 * 
	 * @param daRimuovere oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente da rimuovere.
	 * 
	 * @precondition daRimuovere != null.
	 */
	public static void removePaziente(String daRimuovere) {
		
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", daRimuovere);
		pazienti.deleteOne(document);
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare e restituire tutti i pazienti presenti nel database.
	 * 
	 * @return listaPazienti lista di pazienti di tipo <strong>Paziente</strong>, altrimenti null.
	 */
	public static ArrayList<Paziente> getAllPazienti() {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		ArrayList<Paziente>listaPazienti = new ArrayList<Paziente>();
		for(Document d :pazienti.find())
		{
			listaPazienti.add(CreaBeanUtility.daDocumentAPaziente(d));
		}
		return listaPazienti;
	}
	
	/**
	 * 
	 * Questo medico si occupa di modificare la password di un paziente.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del paziente al quale bisogna aggiornare la password.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta il nuovo valore della password.
	 * 
	 * @precondition codiceFiscale != null && password != null.
	 */
	public static void changePassword(String daAggiornare,String password) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		BasicDBObject nuovoPaziente = new BasicDBObject();
		nuovoPaziente.append("$set", new Document().append("Password", password));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare);
		pazienti.updateOne(searchQuery, nuovoPaziente);
	}
	
	/**
	 * Query che aggiorna il paziente
	 * @param daAggiornare paziente
	 */
//	public static void updatePaziente(Paziente daAggiornare) {
//		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
//		BasicDBObject nuovoPaziente = new BasicDBObject();
//		nuovoPaziente.append("$set", new Document().append("Nome", daAggiornare.getNome()));
//		nuovoPaziente.append("$set", new Document().append("Cognome", daAggiornare.getCognome()));
//		nuovoPaziente.append("$set", new Document().append("DataDiNascita", daAggiornare.getDataDiNascita()));
//		nuovoPaziente.append("$set", new Document().append("Email", daAggiornare.getEmail()));
//		nuovoPaziente.append("$set", new Document().append("Residenza", daAggiornare.getResidenza()));
//		nuovoPaziente.append("$set", new Document().append("LuogoDiNascita", daAggiornare.getLuogoDiNascita()));
//		nuovoPaziente.append("$set", new Document().append("Sesso", daAggiornare.getSesso()));
//		nuovoPaziente.append("$set", new Document().append("Medici", daAggiornare.getMedici()));
//		nuovoPaziente.append("$set", new Document().append("Attivo", daAggiornare.getAttivo()));
//		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare.getCodiceFiscale());
//		pazienti.updateOne(searchQuery, nuovoPaziente);
//	} 
	
	/**
	 * 
	 * Questo metodo si occupa di modificare i dati di un paziente.
	 * 
	 * @param daAggiornare oggetto di tipo <strong>Paziente</strong> che include i nuovi dati del paziente.
	 * 
	 * @precondition daAggiornare != null.
	 */
	public static void updatePaziente(Paziente daAggiornare) {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");

		
		 Document query = new Document();
	     query.append("CodiceFiscale", daAggiornare.getCodiceFiscale());
	     Document nuovoPaziente = new Document();
	     nuovoPaziente.append("Nome", daAggiornare.getNome())
        		.append("Cognome", daAggiornare.getCognome())
    			.append("DataDiNascita", daAggiornare.getDataDiNascita())
    			.append("Email", daAggiornare.getEmail())
    			.append("Residenza", daAggiornare.getResidenza())
    			.append("LuogoDiNascita", daAggiornare.getLuogoDiNascita())
    			.append("Sesso", daAggiornare.getSesso())
    			.append("Medici", daAggiornare.getMedici())
    			.append("Attivo", daAggiornare.getAttivo());

	 
        Document update = new Document();
        update.append("$set", nuovoPaziente);
        pazienti.updateOne(query, update);
		
	}
}