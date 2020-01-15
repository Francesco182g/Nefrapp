package utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bson.Document;

import bean.Amministratore;
import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.AnnuncioProxy;
import bean.Medico;
import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.MessaggioProxy;
import bean.Paziente;
import bean.PianoTerapeutico;
import bean.SchedaParametri;

/**
 * 
 * @author Antonio Questa classe contiene tutt i metodi di conversione dei
 *         documenti in bean
 * 
 */

public class CreaBeanUtility {

  /**
   * Metodo che converte il documento inviato in paziente.
   * 
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
    if (temp != null) {
      LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      paziente.setDataDiNascita(data);
    }
    return paziente;
  }

  /**
   * Metodo che converte il documento inviato in medico.
   * 
   * @param datiMedico documento che continee i dati del medico
   * @return medico convertito dal documento
   */
  public static Medico daDocumentAMedico(Document datiMedico) {
    Medico medico = new Medico();
    medico.setCodiceFiscale(datiMedico.getString("CodiceFiscale"));
    medico.setNome(datiMedico.getString("Nome"));
    medico.setCognome(datiMedico.getString("Cognome"));
    medico.setSesso(datiMedico.getString("Sesso"));
    Date temp = datiMedico.getDate("DataDiNascita");
    if (temp != null) {
      LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      medico.setDataDiNascita(data);
    }
    medico.setEmail(datiMedico.getString("Email"));
    medico.setResidenza(datiMedico.getString("Residenza"));
    medico.setLuogoDiNascita(datiMedico.getString("LuogoDiNascita"));

    return medico;
  }

  /**
   * Metodo che converte il documento inviato in amministratore.
   * 
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
   * Metodo che converte il documento inviato in una SchedaParametri.
   * 
   * @param datiSchedaParametri documento che continee i dati della scheda
   * @return schedaParametri convertito dal documento
   */
  public static SchedaParametri daDocumentASchedaParametri(Document datiSchedaParametri) {
    SchedaParametri schedaParametri = new SchedaParametri();
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
   * 
   * @param datiPiano documento che continee i dati della scheda
   * @return schedaParametri convertito dal documento
   */
  public static PianoTerapeutico daDocumentAPianoTerapeutico(Document datiPiano) {
    PianoTerapeutico piano = new PianoTerapeutico();
    piano.setCodiceFiscalePaziente(datiPiano.getString("PazienteCodiceFiscale"));
    piano.setDiagnosi(datiPiano.getString("Diagnosi"));
    piano.setFarmaco(datiPiano.getString("Farmaco"));
    Date temp = datiPiano.getDate("FineTerapia");
    LocalDate data = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    piano.setDataFineTerapia(data);
    piano.setVisualizzato(datiPiano.getBoolean("Visualizzato"));

    return piano;
  }

  /**
   * Metodo che converte il documento inviato in un Messaggio.
   * 
   * @param datiMessaggio documento che continee i dati del messaggio
   * @return messaggio convertito dal documento
   */
  public static Messaggio daDocumentAMessaggio(Document datiMessaggio) {
    Messaggio messaggio = new MessaggioCompleto();
    messaggio.setCodiceFiscaleMittente(datiMessaggio.getString("MittenteCodiceFiscale"));
    messaggio.setOggetto(datiMessaggio.getString("Oggetto"));
    messaggio.setTesto(datiMessaggio.getString("Testo"));
    Document allegato = (Document) datiMessaggio.get("Allegato");
    messaggio.setNomeAllegato(allegato.getString("NomeAllegato"));
    messaggio.setCorpoAllegato(allegato.getString("CorpoAllegato"));
    Date temp = datiMessaggio.getDate("Data");
    ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
    messaggio.setData(data);
    messaggio.setIdMessaggio(datiMessaggio.getObjectId("_id").toString());

    // caricamento dell'hashmap dall'array di documenti nel database
    HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
    ArrayList<Document> campo = (ArrayList<Document>) datiMessaggio.get("DestinatariView");

    if (campo != null) {
      for (Document d : campo) {
        destinatariView.put(d.getString("CFDestinatario"), d.getBoolean("Visualizzazione"));
      }
    }
    messaggio.setDestinatariView(destinatariView);

    return messaggio;
  }

  /**
   * Metodo che converte un documento ad annuncio.
   * @param datiAnnuncio documento che contiene i dati dell'annuncio
   * @return annuncio convertito dal document
   */
  public static Annuncio daDocumentAAnnuncio(Document datiAnnuncio) {
    Annuncio annuncio = new AnnuncioCompleto();
    annuncio.setMedico(datiAnnuncio.getString("MedicoCodiceFiscale"));
    Document allegato = (Document) datiAnnuncio.get("Allegato");
    annuncio.setNomeAllegato(allegato.getString("NomeAllegato"));
    annuncio.setCorpoAllegato(allegato.getString("CorpoAllegato"));
    annuncio.setTitolo(datiAnnuncio.getString("Titolo"));
    annuncio.setTesto(datiAnnuncio.getString("Testo"));
    Date temp = datiAnnuncio.getDate("Data");
    ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
    annuncio.setData(data);
    annuncio.setIdAnnuncio(datiAnnuncio.get("_id").toString());

    // caricamento dell'hashmap dall'array di documenti nel database
    HashMap<String, Boolean> pazientiView = new HashMap<String, Boolean>();
    ArrayList<Document> campo = (ArrayList<Document>) datiAnnuncio.get("PazientiView");

    if (campo != null) {
      for (Document d : campo) {
        pazientiView.put(d.getString("CFDestinatario"), d.getBoolean("Visualizzazione"));
      }
    }

    annuncio.setPazientiView(pazientiView);

    return annuncio;
  }

  /**
   * Metodo che converte un documento a messaggio.
   * @param datiMessaggio documento che contiene i dati del messaggio
   * @return messaggio convertito dal document
   */
  public static Messaggio daDocumentAMessaggioProxy(Document datiMessaggio) {
    Messaggio messaggio = new MessaggioProxy();
    messaggio.setCodiceFiscaleMittente(datiMessaggio.getString("MittenteCodiceFiscale"));
    messaggio.setOggetto(datiMessaggio.getString("Oggetto"));

    Date temp = datiMessaggio.getDate("Data");
    ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
    messaggio.setData(data);
    messaggio.setIdMessaggio(datiMessaggio.getObjectId("_id").toString());

    // caricamento dell'hashmap dall'array di documenti nel database
    HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();

    ArrayList<Document> campo = (ArrayList<Document>) datiMessaggio.get("DestinatariView");

    if (campo != null) {
      for (Document d : campo) {
        destinatariView.put(d.getString("CFDestinatario"), d.getBoolean("Visualizzazione"));
      }
    }

    messaggio.setDestinatariView(destinatariView);

    return messaggio;
  }

  /**
   * Metodo che converte un documento ad annuncioproxy.
   * @param datiAnnuncio documento che contiene i dati dell'annuncioproxy
   * @return annuncioproxy convertito dal document
   */
  public static Annuncio daDocumentAAnnuncioProxy(Document datiAnnuncio) {
    Annuncio annuncio = new AnnuncioProxy();
    annuncio.setMedico(datiAnnuncio.getString("MedicoCodiceFiscale"));
    Document allegato = (Document) datiAnnuncio.get("Allegato");
    annuncio.setNomeAllegato(allegato.getString("NomeAllegato"));
    annuncio.setTitolo(datiAnnuncio.getString("Titolo"));
    annuncio.setTesto(datiAnnuncio.getString("Testo"));
    Date temp = datiAnnuncio.getDate("Data");
    ZonedDateTime data = temp.toInstant().atZone(ZoneId.of("Europe/Rome"));
    annuncio.setData(data);
    annuncio.setIdAnnuncio(datiAnnuncio.get("_id").toString());

    // caricamento dell'hashmap dall'array di documenti nel database
    HashMap<String, Boolean> pazientiView = new HashMap<String, Boolean>();
    ArrayList<Document> campo = (ArrayList<Document>) datiAnnuncio.get("PazientiView");

    if (campo != null) {
      for (Document d : campo) {
        pazientiView.put(d.getString("CFDestinatario"), d.getBoolean("Visualizzazione"));
      }
    }

    annuncio.setPazientiView(pazientiView);

    return annuncio;
  }
}
