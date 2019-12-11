package model;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import bean.Paziente;

/**
 * 
 * @author Antonio
 * 
 */

public class PazienteModel {

	/**
	 * Metodo che effettua il login per il paziente
	 * @param codiceFiscale codice fiscale del paziente
	 * @param password password del paziente
	 * @return dati del pazienti se le credenziali sono corrette, null altrimenti
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
			paziente = new Paziente();
			paziente.setCodiceFiscale(codiceFiscale);
			paziente.setNome(datiPaziente.getString("Nome"));
			paziente.setCognome(datiPaziente.getString("Cognome"));
			paziente.setSesso(datiPaziente.getString("Sesso"));
			paziente.setDataDiNascita(datiPaziente.getDate("DataDiNascita"));
			paziente.setEmail("Email");
			paziente.setResidenza(datiPaziente.getString("Residenza"));
			paziente.setAttivo(datiPaziente.getBoolean("Attivo"));
		}
		
		return paziente;
	}
	
}
