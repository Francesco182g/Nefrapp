package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.Medico;
import bean.Paziente;
import utility.AlgoritmoCriptazioneUtility;
import utility.CreaBeanUtility;

import static com.mongodb.client.model.Filters.*;

/**
 * 
 * @author Antonio Donnarumma, Luca Esposito 
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
	 * Query che aggiorna i medici di un paziente
	 * @param daAggiornare paziente a cui aggiornare i medici
	 */
	public static void updatePaziente(Paziente daAggiornare) {
		MongoCollection<Document> paziente = DriverConnection.getConnection().getCollection("Paziente");
		
		BasicDBObject nuovoPaziente = new BasicDBObject();
		nuovoPaziente.append("$set", new Document().append("Medici", daAggiornare.getMedici()));

		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare.getCodiceFiscale());

		paziente.updateOne(searchQuery, nuovoPaziente);
		
	}
	
	/**
	 * Query che ricerca un paziente per codice fiscale
	 * @param codiceFiscale del paziente da ricercare
	 * @return paziente se trovato, altrimenti null
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
<<<<<<< HEAD
	 * 
	 * Query che ricerca l'id di un paziente per codice fiscale
	 * @param codiceFiscale del paziente da ricercare
	 * @return id del paziente se trovato, altrimenti null
	 */
	public static String getIdPazienteByCF(String codiceFiscale)
	{
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		
		Document datiPaziente = pazienti.find(eq("CodiceFiscale", codiceFiscale)).first();
		
		
		if(datiPaziente != null) {
			String pazienteID=AlgoritmoCriptazioneUtility.criptaConMD5(datiPaziente.getObjectId("_id").toString());
			return pazienteID;
		}
		
		return null;
		
	}
	
	/**
	 * Query che rimuove un paziente dal database
	 * @param daRimuovere codice fiscale del paziente da rimuovere
	 */
	public static void removePaziente(String daRimuovere) {
		
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Paziente");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", daRimuovere);
		pazienti.deleteOne(document);
	}
	
	/**
	 * Metodo che restituisce tutti i pazienti che sono presenti nel database
	 * @return un arraylist di pazienti
	 */
	public static ArrayList<Paziente> getAllPazienti() {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("Medico");
		ArrayList<Paziente>listaPazienti = new ArrayList<Paziente>();
		for(Document d :pazienti.find())
		{
			listaPazienti.add(CreaBeanUtility.daDocumentAPaziente(d));
		}
		return listaPazienti;
	}

}
