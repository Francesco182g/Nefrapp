package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author Antonio Donnarumma
 * Questa classe si occupa di gestire la connessione con il database.
 */
public class DriverConnection {
  //DATABASE ORIGINARIO SU AZURE
  //private static MongoClientURI uri = new MongoClientURI("mongodb+srv://teamNefrapp:nefrapp01@cluster0-gkgb4.azure.mongodb.net/test");
	
  //USATO PER LO SVILUPPO
  //private static MongoClientURI doUri = new MongoClientURI("mongodb://filippo:pippo123@157.245.26.31/test");
  //private static MongoClient mongoClient = new MongoClient(doUri);
  //private static MongoDatabase database = mongoClient.getDatabase("NefrApp");
	
  //USATO PER LA DEMO DEL CLIENTE
  //private static MongoClientURI doUri = new MongoClientURI("mongodb://demo:pippo123@157.245.26.31/test");
  //private static MongoClient mongoClient = new MongoClient(doUri);
  //private static MongoDatabase database = mongoClient.getDatabase("NefrApp_demo");      

  //USATO PER I TEST
  private static MongoClientURI doUri = new MongoClientURI("mongodb://NefrAdmin:pippo123@157.245.26.31/admin");
  private static MongoClient mongoClient = new MongoClient(doUri);
  private static MongoDatabase database = mongoClient.getDatabase("NefrApp_test");

  /**
   * Questo metodo si occupa di restituire la connessione al database.
   * @return oggetto di tipo <strong>MongoDatabase</strong> 
   *         che rappresenta la connessione al database
   */
  public static MongoDatabase getConnection() {
    return database;
  }
}