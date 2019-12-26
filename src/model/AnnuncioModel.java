package model;


import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.lang.jstl.AndOperator;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bean.Annuncio;
import bean.Messaggio;
import bean.Paziente;
import utility.CreaBeanUtility;

public class AnnuncioModel {
	/**
	 * Cerca un annuncio nel Database
	 * @param idAnnuncio
	 * @return
	 */
	
	public static Annuncio getAnnuncioById(String idAnnuncio) {
		MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
		Document annuncioDoc = annunci.find(eq("_id", new ObjectId(idAnnuncio))).first();
		if (annuncioDoc != null) {
			Annuncio messaggio = CreaBeanUtility.daDocumentAAnnuncio(annuncioDoc);
			return messaggio;
		}
		return null;
	}
	/**
	 * Aggiunge un annuncio nel Database
	 * @param daAggiungere
	 */
	
	public static void addAnnuncio(Annuncio daAggiungere) {
		MongoCollection<Document> annuncioDB = DriverConnection.getConnection().getCollection("Annuncio");
		ArrayList<Document> pazientiView=new ArrayList<Document>();
		HashMap<String, Boolean> mp = new HashMap<String, Boolean>();
		Iterator it = daAggiungere.getPazientiView().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Document coppia=new Document();
			coppia.append("CFDestinatario", pair.getKey())
					.append("Visualizzazione", false);
			pazientiView.add(coppia);
		}
		
		Document allegato = new Document("NomeAllegato", daAggiungere.getNomeAllegato()).
				append("CorpoAllegato", daAggiungere.getCorpoAllegato());
		
		Document doc = new Document("MedicoCodiceFiscale", daAggiungere.getMedico())
				.append("PazientiCodiceFiscale", daAggiungere.getPazienti())
				.append("Titolo", daAggiungere.getTitolo())
				.append("Testo", daAggiungere.getTesto())
				.append("Allegato", allegato)
				.append("Data", daAggiungere.getData().toInstant())
				.append("Visualizzato", false).append("PazientiView", pazientiView);
		annuncioDB.insertOne(doc);
	}
	/**
	 * Cerca e restituisce l'elenco degli annunci scritti da un medico
	 * @param codiceFiscaleMedico
	 * @return
	 */
	public static ArrayList<Annuncio> getAnnunciByCFMedico(String codiceFiscaleMedico) {
		MongoCollection<Document> annuncioDB = DriverConnection.getConnection().getCollection("Annuncio");
		ArrayList<Annuncio> annunci = new ArrayList<>();
		MongoCursor<Document> documenti = annuncioDB.find(eq("MedicoCodiceFiscale", codiceFiscaleMedico)).iterator();

		while (documenti.hasNext()) {
			annunci.add(CreaBeanUtility.daDocumentAAnnuncio(documenti.next()));
		}
		
		documenti.close();
		return annunci;
	}
	/**
	 * Cerca e restituisce l'elenco degli annunci destinati ad un paziente
	 * @param codiceFiscalePaziente
	 * @return annunci
	 */
	
	public static ArrayList<Annuncio> getAnnuncioByCFPaziente(String codiceFiscalePaziente) {
		MongoCollection<Document> annuncioDB = DriverConnection.getConnection().getCollection("Annuncio");
		ArrayList<Annuncio> annunci = new ArrayList<>();
		MongoCursor<Document> documenti = annuncioDB.find(eq("PazientiCodiceFiscale", codiceFiscalePaziente)).iterator();

		while (documenti.hasNext()) {
			annunci.add(CreaBeanUtility.daDocumentAAnnuncio(documenti.next()));
		}
		
		documenti.close();
		return annunci;
	}
	/**
	 * Conta il numero di annunci che non sono stati ancora letti da un determinato paziente
	 * @param codiceFiscalePaziente
	 * @return n numero di annunci non letto
	 */
	
	public static int countAnnunciNonLetti(String codiceFiscalePaziente) {
		MongoCollection<Document> annunciDB= DriverConnection.getConnection().getCollection("Annuncio");
		//nuova query, da testare
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente));
		obj.add(new BasicDBObject("Visualizzato", true));
		andQuery.put("$and", obj);
		int n= (int) annunciDB.count(andQuery);
		//Query vecchia, ma non mi fido
		//Document annuncio = annunciDB.find(eq("_id", new ObjectId(codiceFiscalePaziente))).first().append("Visualizzato", true);
		//int n=(int) annunciDB.count(annuncio);
		return n;
	}
	
	/**
	 * Cambia lo stato della lettura dell'annuncio.
	 * 
	 * @param idAnnuncio  id dell'annuncio che è stato appena aperto
	 * @param visualizzato settaggio del campo "visualizzato" dell'annuncio
	 *                     appena aperto
	 */
	public static void setVisualizzatoAnnuncio(String idAnnuncio, Boolean visualizzato) {
		MongoCollection<Document> annunciDB= DriverConnection.getConnection().getCollection("Annuncio");
		annunciDB.updateOne( new BasicDBObject("_id", new ObjectId(idAnnuncio)),
			    new BasicDBObject("$set", new BasicDBObject("Visualizzato", visualizzato)));
	}
	
	/*
	 * public static String getIdAnnuncio(Annuncio daOttenere) {
		MongoCollection<Document> annunci = DriverConnection.getConnection().getCollection("Annuncio");
		ArrayList<String> codiciFiscaliPazienti = new ArrayList<String>();
		for (Paziente paziente: daOttenere.getPazienti()) {
			codiciFiscaliPazienti.add(paziente.getCodiceFiscale());
		}
		
		Document annuncioDoc = annunci.find(and(eq("MedicoCodiceFiscale", daOttenere.getMedico().getCodiceFiscale()), eq("PazientiCodiceFiscale",codiciFiscaliPazienti), eq("Titolo", daOttenere.getTitolo()), eq("Testo", daOttenere.getTesto()), eq("Data", daOttenere.getData().toInstant()))).first();
		if (annuncioDoc != null) {
			return annuncioDoc.get("_id").toString();
		}
		return null;
		
	}
	*/
	
	
	
}	
	