package control;

import org.apache.tomcat.util.codec.binary.Base64;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.mongodb.MongoException;

import bean.Annuncio;
import bean.Medico;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.UtenteModel;
import utility.CriptazioneUtility;

/**
 * @author Davide Benedetto Strianese, Sara, Nico, Eugenio Questa classe è una
 *         servlet che si occupa della gestione degli annunci
 */
@WebServlet("/GestioneAnnunci")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
    maxFileSize = 15728640, // 15MB
    maxRequestSize = 15728640) // 15MB
public class GestioneAnnunci extends GestioneComunicazione {
  private static final long serialVersionUID = 1L;
  // private RequestDispatcher dispatcher;

  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    try {
      String operazione = request.getParameter("operazione");
      HttpSession session = request.getSession();

      if (operazione == null) {
        response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
        return;
      }

      if (operazione.equals("caricaDestinatariAnnuncio")) {
        caricaDestinatari(request, response);
        RequestDispatcher requestDispatcher = 
            request.getRequestDispatcher("./inserimentoAnnuncioView.jsp");
        requestDispatcher.forward(request, response);
      }

      else if (operazione.equals("caricaAllegato")) {
        caricaAllegato(request, "annuncio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));

      }

      else if (operazione.equals("rimuoviAllegato")) {
        rimuoviAllegato("messaggio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));
      }

      else if (operazione.equals("rimuoviAnnuncio")) {
        rimuoviAnnuncio(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));

      }

      else if (operazione.equals("rimuoviAnnuncioIncompleto")) {
        rimuoviIncompleta("annuncio", session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson("success"));
      }

      else if (operazione.equals("inviaAnnuncio")) {
        inviaComunicazione(request, response, operazione);
        response.sendRedirect("./dashboard.jsp?notifica=annuncioInviato");
      }

      else if (operazione.equals("visualizzaPersonali")) {
        String tipo = request.getParameter("tipo");
        visualizzaAnnunciPersonali(request, response, tipo);
        if (!(tipo != null && tipo.equals("asincrona"))) {
          RequestDispatcher requestDispatcher = request.getRequestDispatcher("./annunci.jsp");
          requestDispatcher.forward(request, response);
        }

      }

      else if (operazione.equals("generaDownload")) {
        generaDownload(request, response);
      }

      else {
        response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
        return;
      }
    } catch (MongoException e) {
      response.sendRedirect("./paginaErrore.jsp?notifica=erroreDb");
    } catch (Exception e) {
      e.printStackTrace();
      if (!response.isCommitted()) {
        response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
      }
    }

    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    doPost(request, response);
    return;
  }

  /**
   * Metodo che permette di scaricare un allegato dalla pagina di visualizzazione.
   * degli annunci
   * 
   * @param request  HttpRequest da cui prelevare l'id dell'annuncio
   * @param response HttpResponse su cui scrivere l'allegato da rendere
   *                 scaricabile
   */
  private void generaDownload(HttpServletRequest request, HttpServletResponse response) {
    String id = request.getParameter("id");
    String fileType = "*";
    String fileName = "";
    String file = "";
    OutputStream out;
    Annuncio annuncio = null;

    if (id != null) {
      annuncio = AnnuncioModel.getAnnuncioById(id);
    }
    fileName = CriptazioneUtility.decodificaStringa(annuncio.getNomeAllegato(), false);
    file = CriptazioneUtility.decodificaStringa(annuncio.getCorpoAllegato(), true);

    response.setContentType(fileType);
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);

    try {
      out = response.getOutputStream();
      out.write((Base64.decodeBase64(file)));
      out.flush();
      out.close();
    } catch (IOException e) {
      System.out.println("generaDownload: errore nella generazione del file");
    }
  }

  /**
   * Metodo che rimuove un annuncio dal database. L'id dell'annuncio è contenuto
   * nella request.
   * 
   * @param request richiesta
   */
  protected void rimuoviAnnuncio(HttpServletRequest request) {
    String id = (String) request.getParameter("id");
    AnnuncioModel.deleteAnnuncioById(id);
  }

  /**
   * Metodo che prende gli annunci personali di un medico o di un paziente e li
   * mostra in una lista.
   * 
   * @param request richiesta utilizzata per ottenere parametri e settare
   *                attributi
   * @param tipo    indica il tipo della chiamata che puo essere asincrona se il
   *                tipo != null
   * @throws IOException possibile eccezione
   */
  private void visualizzaAnnunciPersonali(HttpServletRequest request, HttpServletResponse response, 
      String tipo)throws IOException {
    HttpSession session = request.getSession();
    boolean isMedico = false;
    boolean isPaziente = false;
    if (session.getAttribute("isMedico") != null) {
      isMedico = (boolean) session.getAttribute("isMedico");
    }
    if (session.getAttribute("isPaziente") != null) {
      isPaziente = (boolean) session.getAttribute("isPaziente");
    }

    if (isMedico) {
      Medico medico = (Medico) session.getAttribute("utente");
      ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
      annunci = AnnuncioModel.getAnnunciByCFMedico(medico.getCodiceFiscale());
      System.out.println(annunci);

      if (!annunci.isEmpty()) {
        for (Annuncio a : annunci) {
          if (a.getNomeAllegato() != "" && a.getNomeAllegato() != null) {
            a.setNomeAllegato(CriptazioneUtility.decodificaStringa(a.getNomeAllegato(), false));
          }
        }
      } else {
        request.setAttribute("annunci", annunci);
        return;
      }

      if (tipo != null && tipo.equals("asincrona")) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson(annunci));
      } else {
        request.setAttribute("annunci", annunci);
      }

    }

    else if (isPaziente) {
      Paziente paziente = (Paziente) session.getAttribute("utente");
      ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
      annunci = AnnuncioModel.getAnnuncioByCFPaziente(paziente.getCodiceFiscale());
      if (!annunci.isEmpty()) {
        for (Annuncio a : annunci) {
          if (a.getNomeAllegato() != "" && a.getNomeAllegato() != null) {
            a.setNomeAllegato(CriptazioneUtility.decodificaStringa(a.getNomeAllegato(), false));
            AnnuncioModel.setVisualizzatoAnnuncio(a.getIdAnnuncio(), paziente.getCodiceFiscale(), true);
          }
        }
      }

      ArrayList<String> cache = new ArrayList<>();
      ArrayList<Utente> utentiCache = new ArrayList<>();
      Utente utenteSelezionato = new Utente();

      // piccolo sistema di caching per minimizzare le query sui medici autori degli
      // annunci
      // Se un paziente ha 200 annunci scritti da 5 medici si fanno 5 query e non 200.
      for (Annuncio a : annunci) {
        if (!cache.contains(a.getMedico())) {
          cache.add(a.getMedico());
          utenteSelezionato = UtenteModel.getUtenteByCF(a.getMedico());
          if (utenteSelezionato != null) {
            utentiCache.add(utenteSelezionato);
            request.setAttribute(a.getMedico(),
                utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
          }
        } else if (cache.contains(a.getMedico())) {
          for (Utente ut : utentiCache) {
            if (ut.getCodiceFiscale() == a.getMedico()) {
              utenteSelezionato = ut;
              request.setAttribute(a.getMedico(),
                  utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
            }
          }

        }
      }

      if (tipo != null && tipo.equals("asincrona")) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gg = new Gson();
        response.getWriter().write(gg.toJson(annunci));
      } else {
        request.setAttribute("annunci", annunci);
      }
    }

    else {
      return;
    }
  }
}