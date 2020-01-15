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
  private static MongoClientURI uri = new MongoClientURI("mongodb+srv://teamNefrapp:nefrapp01@cluster0-gkgb4.azure.mongodb.net/test");

  //database hostato su un droplet DigitalOcean: è 4-5 volte più veloce
  //di quello fornito per via della vicinanza geografica
  private static MongoClientURI doUri = 
      new MongoClientURI("mongodb://filippo:pippo123@157.245.26.31/test");

  private static MongoClient mongoClient = new MongoClient(doUri);
  private static MongoDatabase database = mongoClient.getDatabase("NefrApp");

  /**
   * Questo metodo si occupa di restituire la connessione al database.
   * @return oggetto di tipo <strong>MongoDatabase</strong> 
   *         che rappresenta la connessione al database
   */
  public static MongoDatabase getConnection() {
    return database;
  }
}