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

/**
 * 
 * @author Luca Esposito
 * 
 */

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
				.append("LuogoDiNascita", toAdd.getLuogoDiNascita())
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
	
	/**
	 * Query che rimuove un medico dal database
	 * @param daRimuovere codice fiscale del medico da rimuovere
	 */
	public static void removeMedico(String daRimuovere) {
		
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", daRimuovere);
		medici.deleteOne(document);
	}
	
	/**
	 * Metodo che restituisce tutti i medici che sono presenti nel database
	 * @return un arraylist di medici
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
	
	/**
	 * Query che modifica la password del medico
	 * @param daAggiornare codice fiscale del medico
	 * @param password aggiornata
	 */
	public static void changePassword(String daAggiornare,String password) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		BasicDBObject nuovoMedico = new BasicDBObject();
		nuovoMedico.append("$set", new Document().append("Password", password));
		BasicDBObject searchQuery = new BasicDBObject().append("CodiceFiscale", daAggiornare);
		medici.updateOne(searchQuery, nuovoMedico);
	}
	
	/**
	 * Query che aggiorna il medico
	 * @param daAggiornare medico
	 */
	public static void updateMedico(Medico daAggiornare) {
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
	
	
	/**
	 * Query che ottiene tutti i dati dei medici che seguino un paziente
	 * @param codiciFiscaliMedici array contenente i codici fiscali dei medici che seguono il paziente
	 * @return oggetti medico richiesti
	 */
	public static ArrayList<Medico> getMediciByPazienteSeguito(ArrayList<String> codiciFiscaliMedici) {
		MongoCollection<Document> medici = DriverConnection.getConnection().getCollection("Medico");
		ArrayList<Medico> datimedici = new ArrayList<Medico>();
		for(String codiceFiscale: codiciFiscaliMedici) {
			datimedici.add(getMedicoByCF(codiceFiscale));
		}
		return datimedici;
	}
}
