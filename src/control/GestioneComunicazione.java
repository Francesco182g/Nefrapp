package control;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.mongodb.MongoException;

import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.Medico;
import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.Paziente;
import bean.Utente;
import javax.servlet.http.Part;
import model.MessaggioModel;
import model.AnnuncioModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;


/**
 * Questa classe è una servlet che svolge le operazioni comuni delle
 * funzionalità di comunicazione
 * 
 * @author nico
 */
@WebServlet("/comunicazione")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
    maxFileSize = 15728640, // 15MB
    maxRequestSize = 15728640) // 15MB
public class GestioneComunicazione extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public GestioneComunicazione() {
    super();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    return;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    return;
  }

  /**
   * Metodo che carica i destinatari ammessi per inviare messaggi e annunci.
   * 
   * @param request richiesta utilizzata per ottenere parametri e settare
   *                attributi
   * @throws ServletException possibile eccezione
   * @throws IOException possibile eccezione
   */
  protected void caricaDestinatari(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    Utente utente = (Utente) session.getAttribute("utente");

    if (session.getAttribute("isPaziente") != null 
        && (boolean) session.getAttribute("isPaziente") == true) {
      ArrayList<Medico> mediciCuranti = new ArrayList<>();
      Medico selezionato;
      for (String cf : ((Paziente) utente).getMedici()) {
        selezionato = MedicoModel.getMedicoByCF(cf);

        if (selezionato != null) {
          mediciCuranti.add(selezionato);
        }
      }
      request.setAttribute("mediciCuranti", mediciCuranti);
    }

    else if (session.getAttribute("isMedico") != null 
        && (boolean) session.getAttribute("isMedico") == true) {
      ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
      pazientiSeguiti.addAll(PazienteModel.getPazientiSeguiti(utente.getCodiceFiscale()));
      request.setAttribute("pazientiSeguiti", pazientiSeguiti);
    }

    return;
  }

  /**
   * Metodo che prende mittente, destinatari, oggetto e testo della comunicazione
   * e la salva nel database.
   * 
   * @param request     richiesta utilizzata per ottenere parametri e settare attributi
   * @param operazione stringa usata per distinguere l'inserimento di un annuncio 
   *        dall'inserimento di un messaggio
   * @throws ServletException possibile eccezione
   * @throws IOException possibile eccezione
   */
  protected void inviaComunicazione(HttpServletRequest request, HttpServletResponse response, 
      String operazione)throws IOException, ServletException {

    HttpSession session = request.getSession();
    Utente utente = (Utente) session.getAttribute("utente");
    ArrayList<String> destinatari = null;

    if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
      destinatari = 
          new ArrayList<String>(Arrays.asList(request.getParameterValues("selectMedico")));
    } else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
      destinatari = 
          new ArrayList<String>(Arrays.asList(request.getParameterValues("selectPaziente")));
    }

    String CFMittente = utente.getCodiceFiscale();
    String oggetto = request.getParameter("oggetto");
    String testo = request.getParameter("testo");
    HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
    for (String temp : destinatari) {
      destinatariView.put(temp, false);
    }
    String id = (String) session.getAttribute("id");

    if (controllaParametri(CFMittente, oggetto, testo)) {
      if (operazione.equals("inviaMessaggio")) {
        if (id != null) {
          Messaggio daAggiornare = new MessaggioCompleto(CFMittente, oggetto, testo, null, null, 
              null,destinatariView);
          daAggiornare.setIdMessaggio(id);
          MessaggioModel.updateMessaggio(daAggiornare);
        } else {
          MessaggioModel.addMessaggio(new MessaggioCompleto(CFMittente, oggetto, testo, null, null,
              ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView));
        }
      } else if (operazione.equals("inviaAnnuncio")) {
        if (id != null) {
          Annuncio daAggiornare = new AnnuncioCompleto(CFMittente, oggetto, testo, null, null, null,
              destinatariView);
          daAggiornare.setIdAnnuncio(id);
          AnnuncioModel.updateAnnuncio(daAggiornare);
        } else {
          AnnuncioModel.addAnnuncio(new AnnuncioCompleto(CFMittente, oggetto, testo, null, null,
              ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView));
        }
      }
    } else {
      response.sendRedirect("./dashboard.jsp?notifica=comunicazioneNonInviata");
      return;
    }

    session.removeAttribute("allegato");
    session.removeAttribute("nomeFile");
    session.removeAttribute("id");
  }

  /**
   * Metodo che si occupa del caricamento dell file allegato nel database.
   * 
   * @param request da cui ottenere il file
   * @param tipo da cui ottenere il tipo
   * @param session per ottenere la sessione
   */
  public void caricaAllegato(HttpServletRequest request, String tipo, HttpSession session) {
    String allegato = null;
    String nomeFile = null;
    String id = null;
    Messaggio messaggio = null;
    Annuncio annuncio = null;
    InputStream fileStream = null;

    try {
      Part filePart = request.getPart("file");
      if (filePart == null) {
        request.setAttribute("erroreCaricamento", true);
        return;
      }

      nomeFile = filePart.getHeader("Content-Disposition")
          .replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$",
          "$1");
      if (filePart != null && filePart.getSize() > 0 
          && controllaFile(nomeFile, filePart.getSize())) {
        fileStream = filePart.getInputStream();
        try {
          allegato = CriptazioneUtility.codificaStream(fileStream);
          nomeFile = CriptazioneUtility.codificaStringa(nomeFile);

          if (tipo != null && tipo.equals("messaggio")) {
            messaggio = new MessaggioCompleto(null, null, null, allegato, nomeFile,
                ZonedDateTime.now(ZoneId.of("Europe/Rome")), new HashMap<String, Boolean>());
            id = MessaggioModel.addMessaggio(messaggio);
          } else if (tipo != null && tipo.equals("annuncio")) {
            annuncio = new AnnuncioCompleto(null, null, null, allegato, nomeFile,
                ZonedDateTime.now(ZoneId.of("Europe/Rome")), new HashMap<String, Boolean>());
            id = AnnuncioModel.addAnnuncio(annuncio);
          }

        } catch (Exception e) {
          request.setAttribute("erroreCaricamento", true);
          e.printStackTrace();
          return;
        } finally {
          if (fileStream != null) {
            fileStream.close();
          }
        }
      } else {
        request.setAttribute("erroreCaricamento", true);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute("erroreCaricamento", true);
      return;
    } finally {
      session.setAttribute("id", id);
      session.setAttribute("nomeFile", nomeFile);
      session.setAttribute("allegato", allegato);
      if (nomeFile != null && nomeFile.equals("form-data; name=\"file\"; filename=\"\"")) {
        nomeFile = null;
      }
    }
  }

  /**
   * Questo metodo rimuove una comunicazione incompleta nel caso in cui l'utente
   * decida volontariamente di cancellare un allegato gia' caricato
   * 
   * @param tipo stringa contenente il tipo di comunicazione da eliminare
   * @param session HttpSession da cui eliminare gli attributi relativi
   *                 all'allegato
   */
  protected void rimuoviAllegato(String tipo, HttpSession session) {

    String id = (String) session.getAttribute("id");

    if (tipo != null && id != null && tipo.equals("messaggio")) {
      MessaggioModel.deleteMessaggioById(id);
    } else if (tipo != null && id != null && tipo.equals("annuncio")) {
      AnnuncioModel.deleteAnnuncioById(id);
    }

    session.removeAttribute("allegato");
    session.removeAttribute("nomeFile");
    session.removeAttribute("id");
  }

  /**
   * Questo metodo rimuove una comunicazione incompleta nel caso in cui l'utente
   * esca dalla pagina di invio.
   * 
   * @param tipo stringa contenente il tipo di comunicazione da eliminare
   * @param session HttpSession da cui eliminare gli attributi relativi
   *                 all'allegato
   */
  protected void rimuoviIncompleta(String tipo, HttpSession session) {
    ArrayList<Messaggio> messaggi;
    ArrayList<Annuncio> annunci;

    if (tipo != null && tipo.equals("messaggio")) {
      messaggi = MessaggioModel.getMessaggiByDestinatario(null);
      for (Messaggio m : messaggi) {
        MessaggioModel.deleteMessaggioById(m.getIdMessaggio());
      }
    } else if (tipo != null && tipo.equals("annuncio")) {
      annunci = AnnuncioModel.getAnnunciByCFMedico(null);
      for (Annuncio a : annunci) {
        AnnuncioModel.deleteAnnuncioById(a.getIdAnnuncio());
      }
    }

    session.removeAttribute("allegato");
    session.removeAttribute("nomeFile");
    session.removeAttribute("id");
  }

  /**
   * Metodo che controlla la conformità dei campi con le regex.
   * 
   * @param codiceFiscale da controllare
   * @param oggetto da controllare 
   * @param testo da controllare
   * @return
   */
  protected boolean controllaParametri(String codiceFiscale, String oggetto, String testo) {
    String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";

    if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
      return false;
    } else if (oggetto.length() < 1 || oggetto.length() > 75) {
      return false;
    } else if (testo.length() < 1 || testo.length() > 1000) {
      return false;
    }

    return true;
  }

  /**
   * Metodo che effettua i controlli sui campi del file. Restituisce true se il
   * controllo è andato a buon fine, false altrimenti
   * 
   * @param nomeFile da controllare
   * @param dimensioneFile da controllare
   * @return
   */
  public boolean controllaFile(String nomeFile, long dimensioneFile) {
    String estensione = "";

    if (dimensioneFile == 0) {
      return false;
    }

    // file senza estensione (esistono, basta usare un sistema operativo vero)
    if (dimensioneFile > 0 && !nomeFile.contains(".")) {
      return false;
    }
    // senza questo controllo substring crasha in caso di nessun file e file senza
    // estensione
    if (dimensioneFile > 0 && nomeFile.contains(".")) {
      int indice = nomeFile.indexOf(".");
      estensione = nomeFile.substring(indice);
    }

    if (!estensione.equals("") && !estensione.equals(".jpg") && !estensione.equals(".jpeg")
        && !estensione.equals(".png") && !estensione.equals(".pjpeg") && !estensione.equals(".pjp")
        && !estensione.equals(".jfif") && !estensione.equals(".bmp")) {
      return false;
    } else if (dimensioneFile > 15728640L) {
      return false;
    }

    return true;
  }

}
