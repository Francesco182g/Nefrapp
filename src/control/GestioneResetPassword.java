package control;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.Utente;
import model.MedicoModel;
import model.PazienteModel;
import model.UtenteModel;
import utility.InvioEmailUtility;

/**
 * @author Davide Benedetto Strianese, Questa classe è una servlet che si occupa
 *         del reset della password 
 */
@WebServlet("/GestioneResetPassword")
public class GestioneResetPassword extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private RequestDispatcher dispatcher;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
        request.setAttribute("notifica", "Errore generato dalla richiesta!");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("./dashboard.jsp"); 
        dispatcher.forward(request, response);
        return;
      }
      
      String operazione = request.getParameter("operazione");

      if (operazione == null) {
        response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
        return;
      }

      // Viene scelta l'operaizione per richiedere il reset della password
      if (operazione.equals("identificaRichiedente")) {
        identificaRichiedente(request, response);

        if (!response.isCommitted()) {
          response.sendRedirect("./dashboard.jsp?notifica=identificazioneSuccesso");
        }
        return;
      }

      //operazione non in uso. si può rimuovere?
      else if (operazione.equals("richiesta")) {
        richiediReset(request, response);

        if (!response.isCommitted()) {
          response.sendRedirect("./dashboard.jsp?");	
        }

        return;
      }

      // Operazione per effettuare il reset della password
      else if (operazione.equals("reset")) {
        effettuaReset(request, response);
        if (!response.isCommitted()) {
          response.sendRedirect("./dashboard.jsp?notifica=resetSuccesso");
        }
        return;
      }

      else {
        response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
        return;
      }

    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
    }

    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
    return;
  }

  /**
   * Prende in esame il CF inserito dall'utente e ne esamina l'esistenza e
   * l'appartenenza a una categoria tra paziente e medico.
   * 
   * @param request richiesta
   * @param response risposta
   * @throws Exception possibile eccezione
   */
  private void identificaRichiedente(HttpServletRequest request, HttpServletResponse response) 
      throws Exception {
    String codiceFiscale = request.getParameter("codiceFiscale");
    // controlla se esiste il CF nel database (che sia paziente o medico)
    Utente utente = UtenteModel.getUtenteByCF(codiceFiscale);
    if (utente != null) {
      // messaggio da Sara: controlla se è medico o paziente,
      if (MedicoModel.getMedicoByCF(utente.getCodiceFiscale()) != null) {
        System.out.println("è un medico");
        // è un medico, manda la mail con il link per la modifica della password
        String destinatario = utente.getEmail();
        InvioEmailUtility.inviaEmail(destinatario);
        return;

      } else if (PazienteModel.getIdPazienteByCF(utente.getCodiceFiscale()) != null) {
        // è un paziente, viene mandata una mail, dove il destinatario è
        // l'amministratore
        String destinatario = "cuccy15@hotmail.it"; // mail di prova
        InvioEmailUtility.inviaEmail(destinatario);
        return;
      }
    } else { 
      System.out.println("identificaRichiedente: il CF inserito non è nel DB");
      response.sendRedirect("./richiestaResetView.jsp?notifica=CFnonPresente");
      //notifica già implementata
      return;
    }
  }

  /*
   * Questo metodo non so se sia ancora utile. Potrebbe esserlo nel momento in cui
   * decidiamo di fare il check sull'identità del medico in due step. Si
   * tratterebbe di aggiungere una view (dopo che il medico ha inserito il suo CF)
   * dove si chiede al medico di inserire la sua mail. La lascio quì.
   */
  private void richiediReset(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String email = request.getParameter("email");

    if (validaMail(email)) {
      try {
        request.setAttribute("notifica", "Richiesta reset password inviata");
        InvioEmailUtility.inviaEmail(email);
        dispatcher = getServletContext().getRequestDispatcher("/dashboard.jsp");
        return;

      } catch (Exception e) {
        request.setAttribute("notifica", "Si è veriifcato un'erorre durante l'invio dell'eamil. Riprova");
        dispatcher = getServletContext().getRequestDispatcher("/richiestaResetView.jsp");
        return;
      }
    } else {
      request.setAttribute("notifica", "Email inserita non valida");
      dispatcher = getServletContext().getRequestDispatcher("/richiestaResetView.jsp");
      return;
    }
  }

  /**
   * Metodo che effettua il reset della password per un Medico.
   * @param request richiesta
   * @param response risposta
   * @throws ServletException possibile eccezione
   * @throws IOException possibile eccezione
   */
  private void effettuaReset(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    String email = request.getParameter("email");
    String codiceFiscale = request.getParameter("codiceFiscale");
    String password = request.getParameter("password");
    String confermaPsw = request.getParameter("confermaPsw");

    System.out.println("email " + email);
    System.out.println(codiceFiscale);

    if (validaReset(email, codiceFiscale, password, confermaPsw)) {
      Medico medico = MedicoModel.getMedicoByCF(codiceFiscale);
      System.out.println(medico);
      if (medico.getEmail().equals(email)) {
        System.out.println("Email corrisponde");
        MedicoModel.updatePasswordMedico(codiceFiscale, password);
        return;
      } else {
        response.sendRedirect("./resetPasswordView.jsp?notifica=datiErrati");
        // modificare il jsp per scegliere la notifica da mostrare nella pagina di errore 
        // laddove necessario basta fare il check jstl per il parametro passato nel 
        // redirect e mostrare una notifica solo nel caso in cui il valore corrisponda. 
        // Nel caso occorresse un esempio, ne ho già implementate in paginaErrore.jsp 
        // e richiestaResetView.jsp
        return;
      }
    } else {
      System.out.println("effettuaReset: i dati inseriti nella richiesta di reset non sono validi");
      response.sendRedirect("./paginaErrore.jsp?notifica=datiErrati");
      //come sopra
      return;
    }
  }
  
  /**
   * Metodo che controlla la conformità della email con la regex.
   * @param email da validare
   * @return
   */
  private boolean validaMail(String email) {

    final String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";

    if (Pattern.matches(expEmail, email)) {
      return true;
    }

    return false;
  }
  
  /**
   * Metodo che controlla al conformità dei campi con le regex.
   * @param email da validare
   * @param codiceFiscale da validare
   * @param password da validare
   * @param confermaPsw da validare
   * @return
   */
  private boolean validaReset(String email, String codiceFiscale, String password,
      String confermaPsw) {
    boolean valido = true;

    final String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
    final String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
    final String expPassword = "^[a-zA-Z0-9]*$";

    if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16)
      valido = false;

    if (!Pattern.matches(expPassword, password) || password.length() < 6 || password.length() > 20)
      valido = false;

    if (!Pattern.matches(expEmail, email) || email.length() < 6 || email.length() > 50)
      valido = false;

    if (!password.equals(confermaPsw))
      valido = false;

    return valido;
  }
}