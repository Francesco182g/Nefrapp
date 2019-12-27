package bean;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;



/**Interfaccia che determina le operazioni che possono essere compiute da un'implementazione concreta di Messaggio
 * @author nico
 *
 */
public interface Messaggio {
	
	public String getCodiceFiscaleMittente();
	
	public void setCodiceFiscaleMittente(String codiceFiscaleMittente);
	
	public ArrayList<String> getCodiceFiscaleDestinatario();
	
	public void setCodiceFiscaleDestinatario(ArrayList<String> codiceFiscaleDestinatario);
	
	public String getOggetto();
	
	public void setOggetto(String oggetto);
	
	public String getTesto();
	
	public void setTesto(String testo);
	
	public String getCorpoAllegato();
	
	public void setCorpoAllegato(String corpoAllegato);
	
	public String getNomeAllegato();
	
	public void setNomeAllegato(String nomeAllegato);
	
	public String getOraFormattata();
	
	public ZonedDateTime getData();
	
	public String getDataFormattata();
	
	public void setData(ZonedDateTime data);
	
	public void setVisualizzato(Boolean visualizzato);
	
	public Boolean getVisualizzato();
	
	public void setIdMessaggio(String idMessaggio);
	
	public String getIdMessaggio();
	
	public void setDestinatariView(String CFPaziente, Boolean visualizzato);
	
	public HashMap<String,Boolean> getDestinatariView();
	
	public String toString();
	
}
