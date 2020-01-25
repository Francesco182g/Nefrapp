package control;

import bean.Amministratore;
import bean.Paziente;
import bean.Utente;
import com.mongodb.MongoException;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AmministratoreModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * Questa classe è una servlet che si occupa della gestione dell'accesso
 * al sistema.
 * @author Eugenio Corbisiero, Davide Benedetto Strianese, Silvio Di Martino
 */
@WebServlet("/GestioneAccesso")
public class GestioneAccesso extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    doPost(request, response);
    return;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    // Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona
    // ok)
    try {
      String operazione = request.getParameter("operazione");

      HttpSession session = request.getSession();
      synchronized (session) {

        if (operazione.equals("logout")) {
          logout(request);
          response.sendRedirect("./dashboard.jsp");
        }

        else if (operazione.equals("loginAdmin")) {
          loginAmministratore(request, response, session);
          if (!response.isCommitted()) {
            response.sendRedirect("./dashboard.jsp");
          }
        }

        else {
          loginUtente(request, response, session);
          if (!response.isCommitted()) {
            response.sendRedirect("./dashboard.jsp");
          }
        }
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
   * Metodo che permette di effettuare la login dell'amministratore.
   * 
   * @param request  la richiesta al server
   * @param response la risposta del server
   * @param session  la sessione in cui deve essere salvato l'amministratore se
   *                 avviene con successo la login
   * @throws IOException lancia un eccezione se si verifica un errore di input /
   *                     output
   * @throws ServletException possibile eccezione
   */
  private void loginAmministratore(HttpServletRequest request, 
      HttpServletResponse response, HttpSession session)
      throws IOException, ServletException {
    String codiceFiscale = request.getParameter("codiceFiscale");
    String password = request.getParameter("password");
    Amministratore amministratore = null;
    if (controllaParametri(codiceFiscale, password)) {
      password = CriptazioneUtility.criptaConMD5(password);
      amministratore = AmministratoreModel.getAmministratoreByCFPassword(codiceFiscale, password);
      if (amministratore != null) {
        session.removeAttribute("notifica");
        session.setAttribute("isAmministratore", true);
        session.setAttribute("utente", amministratore);
        session.setAttribute("accessDone", true);
        return; 
      }

      else {
        session.setAttribute("accessDone", false);
        response.sendRedirect("./loginAmministratore.jsp?notifica=datiLoginErrati");
        /* modificare la jsp per scegliere la notifica da mostrare nella pagina di 
           errore laddove necessario basta fare il check jstl per il parametro 
           passato nel redirect e mostrare una notifica solo 
           nel caso in cui il valore corrisponda. Vedere in login utente (già implementata).
         */
        return;
      }

    } else {
      response.sendRedirect("./loginAmministratore.jsp?notifica=datiLoginErrati");
      //come sopra
      return;
    }
  }
  
  /**
   * Metodo che permette di effettuare la login all'utente.
   * 
   * @param request  la richiesta al server
   * @param response la risposta del server
   * @param session  la sessione in cui deve essere salvato l'utente se
   *                 avviene con successo la login
   * @throws IOException lancia un eccezione se si verifica un errore di input /
   *                     output
   * @throws ServletException possibile eccezione
   */

  private void loginUtente(HttpServletRequest request, HttpServletResponse response, 
      HttpSession session)
      throws IOException, ServletException {
    String codiceFiscale = request.getParameter("codiceFiscale");
    String password = request.getParameter("password");
    Utente utente = null;

    if (controllaParametri(codiceFiscale, password)) {
      password = CriptazioneUtility.criptaConMD5(password);
      utente = MedicoModel.getMedicoByCFPassword(codiceFiscale, password);
      if (utente == null) {
        utente = PazienteModel.getPazienteByCFPassword(codiceFiscale, password);
        Paziente paziente = (Paziente) utente;
        if (utente != null && paziente.getAttivo() == true) {
          session.setAttribute("isPaziente", true);
          session.setAttribute("accessDone", true);
        }

        else if (utente == null) {
          session.setAttribute("accessDone", false);
          response.sendRedirect("./login.jsp?notifica=datiLoginErrati");
          return;
        }
        else if (paziente.getAttivo() == false) {
          session.setAttribute("accessDone", false);
          response.sendRedirect("./login.jsp?notifica=accountDisattivo");
          return;
        }
      }
      else {
        session.setAttribute("isMedico", true);
        session.setAttribute("accessDone", true);
      }

      if (utente != null) {
        session.setAttribute("utente", utente);
        GestioneNotifica gn = new GestioneNotifica();
        gn.doGet(request, response);
        return;

      }

    }

    else {
      response.sendRedirect("./login.jsp?notifica=datiLoginErrati");
      return;
    }
  }

  /**
   * Metodo che permette di effettuare il logout.
   * 
   * @param request è il client in cui risiede la sessione che deve essere
   *                invalidata
   */
  private void logout(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session != null) {
      session.invalidate();
    }
  }

  /**
   * Metodo che controlla i parametri codice fiscale e password dell' amministratore e dell'utente.
   * 
   * @param codiceFiscale indica il codice fiscale dell' amministratore o
   *                      dell'utente
   * @param password      indica la password dell'amministratore o dell'utente
   * @return true se i controlli vanno a buon fine false altrimenti
   */
  public boolean controllaParametri(String codiceFiscale, String password) {
    boolean valido = true;
    String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
    String expPassword = "^[a-zA-Z0-9]*$";

    if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
      valido = false;
    }
    if (!Pattern.matches(expPassword, password) || password.length() < 6 
        || password.length() > 20) {
      valido = false;
    }
    return valido;
  }
}