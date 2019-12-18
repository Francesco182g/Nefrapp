package utility;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import bean.Annuncio;
import bean.Paziente;

//Questa è una classe che fa parte dell'observer design patter.
/**
 *
 * @author Sara
 *
 */
/*OsservableAnnuncio è un osservable. Quando un annuncio viene creato ed inviato con il relativo setter
 * OsservableAnnuncio notificherà gli observer con il metodo update(). Per fare ciò, ObservableAnnuncio ha
 * bisogno di tenere traccia degli observer, utilizzeremo per questo una lista observers.
 */
public class OsservableAnnuncio  {
	
	private Annuncio annuncio;
	private ArrayList <Osservatore> observer = new ArrayList<Osservatore>();	
	
    public OsservableAnnuncio(Annuncio newAnnuncio, ArrayList<Paziente> newListaPazienti) {
    	this.annuncio=newAnnuncio;
    	
    }
    
	public void addObserver(Osservatore paziente) {
        //notifico l'annuncio non appena un observer (paziente) viene aggiunto
        paziente.update(this.annuncio);
        this.observer.add(paziente);
    }

    public void removeObserver(Osservatore paziente) {
        this.observer.remove(paziente);
    }

    public void notifyAnnuncio(Annuncio newAnnuncio) {
        this.annuncio = newAnnuncio;
        for (Osservatore observer : this.observer) {
            observer.update(this.annuncio);
        }
    }

}
