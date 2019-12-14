package utility;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.Document;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import bean.SchedaParametri;

/**
 * 
 * @author Antonio
 * Questa classe contiene tutt i metodi di conversione dei documenti in bean
 * 
 */


public class CreaBeanUtility {

	/**
	 * Classe che converte il documento inviato in paziente
	 * @param datiPaziente documento che continee i dati del paziente
	 * @return paziente convertito dal documento
	 */
	public static Paziente daDocumentAPaziente(Document datiPaziente) {
		Paziente paziente = new Paziente();
		
		paziente.setCodiceFiscale(datiPaziente.getString("CodiceFiscale"));
		paziente.setNome(datiPaziente.getString("Nome"));
		paziente.setCognome(datiPaziente.getString("Cognome"));
		paziente.setSesso(datiPaziente.getString("Sesso"));
		paziente.setDataDiNascita(datiPaziente.getDate("DataDiNascita"));
		paziente.setEmail("Email");
		paziente.setResidenza(datiPaziente.getString("Residenza"));
		paziente.setAttivo(datiPaziente.getBoolean("Attivo"));
		paziente.setMedici((ArrayList<String>) datiPaziente.get("Medici"));
		
		return paziente;
	}
	
	/**
	 * Classe che converte il documento inviato in medico
	 * @param datiMedico documento che continee i dati del medico
	 * @return medico convertito dal documento
	 */
	public static Medico daDocumentAMedico(Document datiMedico) {
		Medico medico = new Medico();
		
		medico = new Medico();
		medico.setCodiceFiscale(datiMedico.getString("CodiceFiscale"));
		medico.setNome(datiMedico.getString("Nome"));
		medico.setCognome(datiMedico.getString("Cognome"));
		medico.setSesso(datiMedico.getString("Sesso"));
		medico.setDataDiNascita(datiMedico.getDate("DataDiNascita"));
		medico.setEmail("Email");
		medico.setResidenza(datiMedico.getString("Residenza"));
		
		return medico;
	}
	
	/**
	 * Classe che converte il documento inviato in amministratore
	 * @param datiAmministratore documento che continee i dati dell' amministratore
	 * @return amministratore convertito dal documento
	 */
	public static Amministratore daDocumentAdAmministratore(Document datiAmministratore) {
		Amministratore amministratore = new Amministratore();
		
		amministratore = new Amministratore();
		amministratore.setCodiceFiscale(datiAmministratore.getString("CodiceFiscale"));
		amministratore.setNome(datiAmministratore.getString("Nome"));
		amministratore.setCognome(datiAmministratore.getString("Cognome"));
		amministratore.setEmail(datiAmministratore.getString("Email"));
		
		return amministratore;
	}

	/**
	 * Classe che converte il documento inviato in una SchedaParametri
	 * @param datiSchedaParametri documento che continee i dati della scheda
	 * @return schedaParametri convertito dal documento
	 */
	public static SchedaParametri daDocumentASchedaParametri(Document datiSchedaParametri) {
		SchedaParametri schedaParametri= new SchedaParametri();

		schedaParametri = new SchedaParametri();
		schedaParametri.setPazienteCodiceFiscale(datiSchedaParametri.getString("PazienteCodiceFiscale"));
		schedaParametri.setPeso(new BigDecimal(datiSchedaParametri.getString("Peso")));
		schedaParametri.setPaMin(Integer.parseInt(datiSchedaParametri.getString("PaMin")));
		schedaParametri.setPaMax(Integer.parseInt(datiSchedaParametri.getString("PaMax")));
		schedaParametri.setScaricoIniziale(Integer.parseInt(datiSchedaParametri.getString("ScaricoIniziale")));
		schedaParametri.setUF(Integer.parseInt(datiSchedaParametri.getString("UF")));
		schedaParametri.setTempoSosta(Integer.parseInt(datiSchedaParametri.getString("TempoSosta")));
		schedaParametri.setScarico(Integer.parseInt(datiSchedaParametri.getString("Scarico")));
		schedaParametri.setCarico(Integer.parseInt(datiSchedaParametri.getString("Carico")));
		schedaParametri.setData(LocalDate.parse((datiSchedaParametri.getString("Data"))));
		
		
		return schedaParametri;
	}
	
}
