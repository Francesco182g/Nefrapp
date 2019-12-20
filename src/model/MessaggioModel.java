package model;


import java.util.ArrayList;

/**
 * 
 * @author Sara
 *
 */
import bean.Messaggio;
public class MessaggioModel {
	/**
	 * Metodo che permette l'aggiunta di un messaggio al database
	 * @param messsaggio messaggio da aggiungere
	 */
	public static void addMessaggio(Messaggio messaggio) {
		//fai cose
	}
	/**
	 * Query che ricerca i messaggi per codice fiscale dei destinatari
	 * @param CFDestinatario Codice Fiscale del destinatario
	 * @return messaggio se trovato almeno un messaggio, altrimenti null
	 */
	public static ArrayList<Messaggio> getMessaggioByCFDestinatario(String CFDestinatario) {
		ArrayList<Messaggio> messaggio=new ArrayList<Messaggio>();
		//fai cose
		return messaggio;
	}
	
	
}
