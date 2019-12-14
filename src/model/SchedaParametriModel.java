package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.SchedaParametri;
import utility.CreaBeanUtility;

public class SchedaParametriModel {
	public static SchedaParametri getSchedaParametriByCFDate(String codiceFiscalePaziente, LocalDate data) {
		MongoCollection<Document> schedaParametri = DriverConnection.getConnection().getCollection("SchedaParametri");
		SchedaParametri scheda= null;
		BasicDBObject andQuery = new BasicDBObject();
		
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("PazienteCodiceFiscale", codiceFiscalePaziente));
		obj.add(new BasicDBObject("Data", data));
		andQuery.put("$and", obj);
		
		Document datiSchedaParametri = schedaParametri.find(andQuery).first();
		if(datiSchedaParametri != null) {
			scheda = CreaBeanUtility.daDocumentASchedaParametri(datiSchedaParametri);
		}
		
		return scheda;
	}

	public void addSchedaParametri(SchedaParametri daAggiungere) 
	{
		MongoCollection<Document> scheda = DriverConnection.getConnection().getCollection("SchedaParametri");
		
		Document doc = new Document("PazienteCodiceFiscale", daAggiungere.getPazienteCodiceFiscale())
				.append("Peso", daAggiungere.getPeso())
				.append("PaMin", daAggiungere.getPaMin())
				.append("PaMax", daAggiungere.getPaMax())
				.append("ScaricoIniziale", daAggiungere.getScaricoIniziale())
				.append("UF", daAggiungere.getUF())
				.append("TempoSosta", daAggiungere.getTempoSosta())
				.append("Carico", daAggiungere.getCarico())
				.append("Scarico", daAggiungere.getScarico())
				.append("Date", daAggiungere.getData());
		scheda.insertOne(doc);	
	}
}
