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
import utility.CriptazioneUtility;

/**
 * 
 * @author Luca Esposito, Antonio Donnarumma.
 * Questa classe � un manager che si occupa di interagire con il database.
 * Gestisce le query riguardanti il medico.
 */

public class MedicoModel {

	/**
	 * 
	 * Questo metodo si occupa di verificare se i dati immessi dal medico per effettuare il login sono presenti nel database.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del medico.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password del medico.
	 * 
	 * @return un oggetto di tipo <strong>Medico</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null && password != null .
	 */
	public static Medico getMedicoByCFPassword(String codiceFiscale, String password) {
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
	 * 
	 * Questo metodo consente di aggiungere un medico al database.
	 * 
	 * @param daAggiungere oggetto di tipo <strong>Medico</strong> che rappresenta il medico da aggiungere.
	 * @param password oggetto di tipo <strong>String</strong> che rappresenta la password del medico da aggiungere.
	 * 
	 * @precondition toAdd != null && password != null.
	 */
	public static void addMedico(Medico daAggiungere, String password) {
		MongoCollection<Document> medico = DriverConnection.getConnection().getCollection("Medico");
		
		Document doc = new Document("CodiceFiscale", daAggiungere.getCodiceFiscale())
				.append("Nome", daAggiungere.getNome())
				.append("Cognome", daAggiungere.getCognome())
				.append("Password",password)
				.append("DataDiNascita", daAggiungere.getDataDiNascita())
				.append("Sesso", daAggiungere.getSesso())
				.append("Residenza", daAggiungere.getResidenza())
				.append("LuogoDiNascita", daAggiungere.getLuogoDiNascita())
				.append("Email", daAggiungere.getEmail());
		medico.insertOne(doc);	
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare un medico tramite codice fiscale.
	 * 
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del medico.
	 * 
	 * @return medico oggetto di tipo <strong>Medico</strong>, altrimenti null.
	 * 
	 * @precondition codiceFiscale != null.
	 */
	public static Medico getMedicoByCF(String codiceFiscale) {
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Medico medico = null;
		Document datiMedico = medici.find(eq("CodiceFiscale", codiceFiscale)).first();
		if(datiMedico != null) {
			medico = CreaBeanUtility.daDocumentAMedico(datiMedico);
		}
		
		return medico;
	}
	
	/**
	 * 
	 * Questo metodo si occupa di rimuovere un medico dal database.
	 * 
	 * @param daRimuovere oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del medico da rimuovere.
	 * 
	 * @precondition daRimuovere != null.
	 */
	public static void removeMedico(String daRimuovere) {
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", daRimuovere);
		medici.deleteOne(document);
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare e restituire tutti i medici presenti nel database.
	 * 
	 * @return una lista di medici di tipo <strong>Medico</strong>, altrimenti null.
	 */
	public static ArrayList<Medico> getAllMedici() {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		ArrayList<Medico>listaMedici = new ArrayList<Medico>();
		for(Document d :medici.find())
		{
			listaMedici.add(CreaBeanUtility.daDocumentAMedico(d));
			
		}
		return listaMedici;
	}
	
	/*
	public static void changePassword(String daAggiornare,String password) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject nuovoMedico = new BasicDBObject();
		nuovoMedico.append("$set", new Document().append("Password", password));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare);
		medici.updateOne(searchQuery, nuovoMedico);
	}
	*/
	/*
	 * public static void updateMedico(Medico daAggiornare) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject nuovoMedico = new BasicDBObject();
		nuovoMedico.append("$set", new Document().append("Nome", daAggiornare.getNome()));
		nuovoMedico.append("$set", new Document().append("Cognome", daAggiornare.getCognome()));
		nuovoMedico.append("$set", new Document().append("DataDiNascita", daAggiornare.getDataDiNascita()));
		nuovoMedico.append("$set", new Document().append("Email", daAggiornare.getEmail()));
		nuovoMedico.append("$set", new Document().append("Residenza", daAggiornare.getResidenza()));
		nuovoMedico.append("$set", new Document().append("LuogoDiNascita", daAggiornare.getLuogoDiNascita()));
		nuovoMedico.append("$set", new Document().append("Sesso", daAggiornare.getSesso()));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare.getCodiceFiscale());
		medici.updateOne(searchQuery, nuovoMedico);
	}
	 */
	/**
	 * 
	 * Questo metodo si occupa di modificare i dati di un medico.
	 * 
	 * @param daAggiornare oggetto di tipo <strong>Medico</strong> che include i nuovi dati del medico.
	 * 
	 * @precondition daAggiornare != null.
	 */
	public static void updateMedico(Medico daAggiornare) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Document query = new Document();
	    
		query.append("CodiceFiscale", daAggiornare.getCodiceFiscale());
	    
		Document nuovoMedico = new Document();
	    nuovoMedico.append("Nome", daAggiornare.getNome())
        		.append("Cognome", daAggiornare.getCognome())
    			.append("DataDiNascita", daAggiornare.getDataDiNascita())
    			.append("Email", daAggiornare.getEmail())
    			.append("Residenza", daAggiornare.getResidenza())
    			.append("LuogoDiNascita", daAggiornare.getLuogoDiNascita())
    			.append("Sesso", daAggiornare.getSesso());

	 
        Document update = new Document();
        update.append("$set", nuovoMedico);
        medici.updateOne(query, update);
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare e restituire tutti i medici che seguono un determinato paziente.
	 * 
	 * @param mediciPaziente paziente da cui ottenere i codici fiscali dei medici che lo seguono.
	 * 
	 * @return oggetti medico richiesti
	 */
	public static ArrayList<Medico> getMediciByPazienteSeguito(Paziente mediciPaziente) {
		ArrayList<String> codiciFiscaliMedici = mediciPaziente.getMedici();
		ArrayList<Medico> datimedici = new ArrayList<Medico>();
		for(String codiceFiscale: codiciFiscaliMedici) {
			datimedici.add(getMedicoByCF(codiceFiscale));
		}
		return datimedici;
	}
	
	/**
	 * 
	 * Questo metodo si occupa di ricercare un medico tramite il suo indirizzo email.
	 *
	 * @param email oggetto di tipo <strong>String</strong> che rappresenta l'indirizzo email del medico.
	 * 
	 * @return medico oggetto di tipo <strong>Medico</strong>, altrimenti null.
	 * 
	 * @precondition email != null.
	 */
	public static Medico getMedicoByEmail(String email) {
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Medico medico = null;
		Document datiMedico = medici.find(eq("Email", email)).first();
		if(datiMedico != null) {
			medico = CreaBeanUtility.daDocumentAMedico(datiMedico);
		}
		
		return medico;
	}
	
	/**
	 * 
	 * Questo medico si occupa di modificare la password di un medico.
	 *
	 * @param codiceFiscale oggetto di tipo <strong>String</strong> che rappresenta il codice fiscale del medico al quale bisogna aggiornare la password.
	 * @param nuovaPassword oggetto di tipo <strong>String</strong> che rappresenta il nuovo valore della password.
	 * 
	 * @precondition codiceFiscale != null && nuovaPassword != null.
	 */
	public static void updatePasswordMedico(String codiceFiscale, String nuovaPassword) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject nuovoMedico = new BasicDBObject();
		
		nuovoMedico.append("$set", new Document().append("Password",nuovaPassword));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", codiceFiscale);
		
		medici.updateOne(searchQuery, nuovoMedico);
	}
	/**
	 * Questo metodi si occupa di verificare che la mail che inserisce il medico non sia già presente 
	 * @param email è la mail che inserisce il medico o l'amministratore 
	 * @return boolean che è true se la mail è presente false altrimenti
	 */
	public static boolean checkEmail(String email)
	{
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		Document datiMedico = medici.find(eq("Email", email)).first();
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		Document datiPaziente = pazienti.find(eq("Email", email)).first();
		
		if(datiMedico == null && datiPaziente == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
}