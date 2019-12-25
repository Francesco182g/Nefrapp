package utility;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import bean.Amministratore;
import bean.Annuncio;
import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import bean.PianoTerapeutico;
import bean.SchedaParametri;
import model.MedicoModel;
import model.PazienteModel;

/**
 * 
 * @author Antonio
 * Questa classe contiene tutt i metodi di conversione dei documenti in bean
 * 
 */


public class CreaBeanUtility {

	/**
	 * Metodo che converte il documento inviato in paziente
	 * @param datiPaziente documento che continee i dati del paziente
	 * @return paziente convertito dal documento
	 */
	public static Paziente daDocumentAPaziente(Document datiPaziente) {
		Paziente paziente = new Paziente();
		
		paziente.setCodiceFiscale(datiPaziente.getString("CodiceFiscale"));
		paziente.setNome(datiPaziente.getString("Nome"));
		paziente.setCognome(datiPaziente.getString("Cognome"));
		paziente.setSesso(datiPaziente.getString("Sesso"));
		paziente.setEmail(datiPaziente.getString("Email"));
		paziente.setResidenza(datiPaziente.getString("Residenza"));
		paziente.setLuogoDiNascita(datiPaziente.getString("LuogoDiNascita"));
		paziente.setAttivo(datiPaziente.getBoolean("Attivo"));
		paziente.setMedici((ArrayList<String>) datiPaziente.get("Medici"));
		Date temp = datiPaziente.getDate("DataDiNascita");
		if(temp != null)
		{
			LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			paziente.setDataDiNascita(data);
		}
		return paziente;
	}
	
	/**
	 * Metodo che converte il documento inviato in medico
	 * @param datiMedico documento che continee i dati del medico
	 * @return medico convertito dal documento
	 */
	public static Medico daDocumentAMedico(Document datiMedico) {
		Medico medico = new Medico();
		medico.setCodiceFiscale(datiMedico.getString("CodiceFiscale"));
		medico.setNome(datiMedico.getString("Nome"));
		medico.setCognome(datiMedico.getString("Cognome"));
		medico.setSesso(datiMedico.getString("Sesso"));
		Date temp=datiMedico.getDate("DataDiNascita");
		if(temp != null)
		{
			LocalDate data=temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			medico.setDataDiNascita(data);
		}
		medico.setEmail(datiMedico.getString("Email"));
		medico.setResidenza(datiMedico.getString("Residenza"));
		medico.setLuogoDiNascita(datiMedico.getString("LuogoDiNascita"));
		
		return medico;
	}
	
	/**
	 * Metodo che converte il documento inviato in amministratore
	 * @param datiAmministratore documento che continee i dati dell' amministratore
	 * @return amministratore convertito dal documento
	 */
	public static Amministratore daDocumentAdAmministratore(Document datiAmministratore) {
		Amministratore amministratore = new Amministratore();
		amministratore.setCodiceFiscale(datiAmministratore.getString("CodiceFiscale"));
		amministratore.setNome(datiAmministratore.getString("Nome"));
		amministratore.setCognome(datiAmministratore.getString("Cognome"));
		amministratore.setEmail(datiAmministratore.getString("Email"));
		
		return amministratore;
	}

	/**
	 * Metodo che converte il documento inviato in una SchedaParametri
	 * @param datiSchedaParametri documento che continee i dati della scheda
	 * @return schedaParametri convertito dal documento
	 */
	public static SchedaParametri daDocumentASchedaParametri(Document datiSchedaParametri) {
		SchedaParametri schedaParametri= new SchedaParametri();
		schedaParametri.setPazienteCodiceFiscale(datiSchedaParametri.getString("PazienteCodiceFiscale"));
		schedaParametri.setPeso(new BigDecimal(String.valueOf(datiSchedaParametri.get("Peso"))));
		schedaParametri.setPaMin(datiSchedaParametri.getInteger("PaMin"));
		schedaParametri.setPaMax(datiSchedaParametri.getInteger("PaMax"));
		schedaParametri.setScaricoIniziale(datiSchedaParametri.getInteger("ScaricoIniziale"));
		schedaParametri.setUF(datiSchedaParametri.getInteger("UF"));
		schedaParametri.setTempoSosta(datiSchedaParametri.getInteger("TempoSosta"));
		schedaParametri.setScarico(datiSchedaParametri.getInteger("Scarico"));
		schedaParametri.setCarico(datiSchedaParametri.getInteger("Carico"));
		Date temp = datiSchedaParametri.getDate("Data");
		LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		schedaParametri.setData(data);

		return schedaParametri;
	}
	
	/**
	 * Metodo che converte il documento inviato in un PianoTerapeutico
	 * @param datiSchedaParametri documento che continee i dati della scheda
	 * @return schedaParametri convertito dal documento
	 */
	public static PianoTerapeutico daDocumentAPianoTerapeutico(Document datiPiano) {
		PianoTerapeutico piano= new PianoTerapeutico();
		piano.setCodiceFiscalePaziente(datiPiano.getString("PazienteCodiceFiscale"));
		piano.setDiagnosi(datiPiano.getString("Diagnosi"));
		piano.setFarmaco(datiPiano.getString("Farmaco"));
		Date temp = datiPiano.getDate("FineTerapia");
		LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		piano.setDataFineTerapia(data);

		return piano;
	}
	
	/**
	 * Metodo che converte il documento inviato in un Messaggio
	 * @param datiSchedaParametri documento che continee i dati della scheda
	 * @return schedaParametri convertito dal documento
	 */
	public static Messaggio daDocumentAMessaggio(Document datiMessaggio) {
		Messaggio messaggio = new Messaggio();
		messaggio.setCodiceFiscaleMittente(datiMessaggio.getString("MittenteCodiceFiscale"));
		messaggio.setCodiceFiscaleDestinatario((ArrayList<String>) datiMessaggio.get("DestinatarioCodiceFiscale"));
		messaggio.setOggetto(datiMessaggio.getString("Oggetto"));
		messaggio.setTesto(datiMessaggio.getString("Testo"));
		messaggio.setAllegato(datiMessaggio.getString("Allegato"));	
		Date temp = datiMessaggio.getDate("Data");
		ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
		messaggio.setData(data);
		messaggio.setVisualizzato(datiMessaggio.getBoolean("Visualizzato"));
		messaggio.setIdMessaggio(datiMessaggio.getObjectId("_id").toString());
		return messaggio;
	}
	
	public static Annuncio daDocumentAAnnuncio(Document datiAnnuncio) {
		Annuncio annuncio = new Annuncio();
		annuncio.setMedico(datiAnnuncio.getString("MedicoCodiceFiscale"));
		annuncio.setPazienti((ArrayList<String>) datiAnnuncio.get("PazientiCodiceFiscale"));
		annuncio.setAllegato(datiAnnuncio.getString("Allegato"));
		annuncio.setTitolo(datiAnnuncio.getString("Titolo"));
		annuncio.setTesto(datiAnnuncio.getString("Testo"));
		Date temp = datiAnnuncio.getDate("Data");
		ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
		annuncio.setData(data);
		annuncio.setVisualizzato(datiAnnuncio.getBoolean("Visualizzato"));
		annuncio.setIdAnnuncio(datiAnnuncio.get("_id").toString());
		
		return annuncio;
	}
}
