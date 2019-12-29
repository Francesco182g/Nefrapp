package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author Antonio
 * 
 */

public class DriverConnection {
	private static MongoClientURI uri = new MongoClientURI("mongodb+srv://teamNefrapp:nefrapp01@cluster0-gkgb4.azure.mongodb.net/test");
	
	//database hostato su un droplet DigitalOcean: è 4-5 volte più veloce di quello fornito per via della vicinanza geografica
	private static MongoClientURI doUri = new MongoClientURI("mongodb://filippo:pippo123@157.245.26.31/test");
	
	private static MongoClient mongoClient = new MongoClient(doUri);
	private static MongoDatabase database = mongoClient.getDatabase("NefrApp");
	
	/**
	 * @return restituisce la connessione al database Mongo
	 */
	public static  MongoDatabase getConnection() {
		return database;
	}
	
}
