package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.mongodb.MongoException;

import bean.Messaggio;
import bean.Utente;
import model.MessaggioModel;
import model.UtenteModel;
import utility.CriptazioneUtility;

/**
 * @author Sara, Nico
 * Servlet implementation class GestioneMessaggio.
 */
@WebServlet("/GestioneMessaggi")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
    maxFileSize = 15728640, // 15MB
    maxRequestSize = 15728640) // 15MB
public class GestioneMessaggi extends GestioneComunicazione {
  private static final long serialVersionUID = 1L;

  public GestioneMessaggi() {
    super();
  }

  /**
   * @throws IOException possibile eccezione
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {

      HttpSession session = request.getSession();
      String operazione = request.getParameter("operazione");
      if (operazione.equals("caricaDestinatariMessaggio")) {
        caricaDestinatari(request, response);
        RequestDispatcher requestDispatcher =
            request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
        requestDispatcher.forward(request, response);
        return;
      }

      else if (operazione.equals("caricaAllegato")) {
        caricaAllegato(request, "messaggio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));
        return;
      }

      else if (operazione.equals("rimuoviAllegato")) {
        rimuoviAllegato("messaggio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));
        return;
      }

      else if (operazione.equals("rimuoviMessaggioIncompleto")) {
        rimuoviIncompleta("messaggio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));
      }

      else if (operazione.equals("inviaMessaggio")) {
        inviaComunicazione(request, response, operazione);
        if (!response.isCommitted()) {
          response.sendRedirect("./dashboard.jsp?notifica=messaggioInviato");
        }
        return;
      } 

      else if (operazione.equals("visualizzaElencoMessaggio")) {
        visualizzaListaMessaggi(request, response);
        if (!response.isCommitted()) {
          RequestDispatcher requestDispatcher =
              request.getRequestDispatcher("./listaMessaggiView.jsp");
          requestDispatcher.forward(request, response);
        }
        return;

      } else if (operazione.equals("visualizzaMessaggio")) {
        visualizzaMessaggio(request, response);
        if (!response.isCommitted()) {
          RequestDispatcher requestDispatcher = request.getRequestDispatcher("./messaggioView.jsp");
          requestDispatcher.forward(request, response);
        }
        return;
      }
    } catch (MongoException e) {
      response.sendRedirect("./paginaErrore.jsp?notifica=erroreDb");
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
    }
    return;
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      doGet(request, response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Metodo che prende la lista dei messaggi ricevuti dall'utente e lo salva nella
   * richiesta
   * 
   * @param request richiesta utilizzata per ottenere parametri e settare
   *                attributi
   * @throws IOException
   */
  private void visualizzaListaMessaggi(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    Utente utente = null;
    HttpSession session = request.getSession();

    utente = (Utente) session.getAttribute("utente");

    if (session.getAttribute("accessDone") != null 
        && (boolean) session.getAttribute("accessDone") == true) {
      ArrayList<String> cache = new ArrayList<>();
      ArrayList<Utente> utentiCache = new ArrayList<>();
      Utente utenteSelezionato = new Utente();
      ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();

      messaggi = MessaggioModel.getMessaggiByDestinatario(utente.getCodiceFiscale());
      request.setAttribute("messaggio", messaggi);

      if (messaggi.isEmpty()) {
        return;
      }


      // piccolo sistema di caching per minimizzare le query sui mittenti dei messaggi
      // Se un paziente ha 200 messaggi da 5 medici si fanno 5 query e non 200.
      for (Messaggio m : messaggi) {
        if (!cache.contains(m.getCodiceFiscaleMittente())) {
          cache.add(m.getCodiceFiscaleMittente());
          utenteSelezionato = UtenteModel.getUtenteByCF(m.getCodiceFiscaleMittente());
          if (utenteSelezionato != null) {
            utentiCache.add(utenteSelezionato);
            request.setAttribute(m.getCodiceFiscaleMittente(),
                utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
          }
        } else if (cache.contains(m.getCodiceFiscaleMittente())) {
          for (Utente ut : utentiCache) {
            if (ut.getCodiceFiscale() == m.getCodiceFiscaleMittente()) {
              utenteSelezionato = ut;
              request.setAttribute(m.getCodiceFiscaleMittente(),
                  utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
            }
          }

        }
      }
    } 
  }

  /**
   * Metodo che prende l'id di un messaggio dalla request e lo usa
   * per prendere il messaggio corrispondente dal database, decriptarne l'allegato
   * e mettere nella request le informazioni da mostrare.
   * 
   * @param request richiesta utilizzata per ottenere parametri e settare
   *                attributi
   * @throws IOException possibile eccezione
   */
  private void visualizzaMessaggio(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    String idMessaggio = request.getParameter("idMessaggio");
    Messaggio messaggio;
    messaggio = MessaggioModel.getMessaggioById(idMessaggio);

    String nomeAllegato = messaggio.getNomeAllegato();
    String corpoAllegato = messaggio.getCorpoAllegato();
    Utente utente = new Utente();
    utente = (Utente) request.getSession().getAttribute("utente");

    if (messaggio != null) {
      MessaggioModel.setVisualizzatoMessaggio(idMessaggio, utente.getCodiceFiscale(),true);
      messaggio.setVisualizzato(true);
      if (nomeAllegato != null && corpoAllegato != null) {
        messaggio.setCorpoAllegato(CriptazioneUtility.decodificaStringa(corpoAllegato, true));
        nomeAllegato = CriptazioneUtility.decodificaStringa(nomeAllegato, false);
        messaggio.setNomeAllegato(nomeAllegato);
      }

      request.setAttribute("messaggio", messaggio);
    }
  }

}
