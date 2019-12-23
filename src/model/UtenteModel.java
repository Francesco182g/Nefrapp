package model;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import bean.Utente;
import utility.CreaBeanUtility;

public class UtenteModel {

	public static Utente getUtenteByCF(String codiceFiscale) {
		
		MongoCollection<Document> utenti = DriverConnection.getConnection().getCollection("Medico");
		Utente utente = new Utente();
		Document datiUtente = utenti.find(eq("CodiceFiscale", codiceFiscale)).first();
		
		if(datiUtente != null) {
			utente = CreaBeanUtility.daDocumentAMedico(datiUtente);
		} else {
			utenti = DriverConnection.getConnection().getCollection("Paziente");
			datiUtente = utenti.find(eq("CodiceFiscale", codiceFiscale)).first();
			if (datiUtente!=null && !datiUtente.isEmpty()) {
				utente = CreaBeanUtility.daDocumentAPaziente(datiUtente);
			}
		}
		
		return utente;
	}
}
