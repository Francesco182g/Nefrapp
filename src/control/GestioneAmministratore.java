package control;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import bean.Utente;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import model.PianoTerapeuticoModel;
import utility.CriptazioneUtility;

/**
 *  Questa classe è una servlet che si occupa della gestione delle funzionalità dell'amministratore.
 *  @author Luca Esposito e Eugenio Corbisiero
 */
@WebServlet("/GestioneAmministratore")
public class GestioneAmministratore extends HttpServlet {
  private static final long serialVersionUID = 1L;


  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    //Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)

    try {
      HttpSession session = request.getSession();
      String operazione = request.getParameter("operazione");

      if (operazione.equals("rimuoviAccount")) {
        String codiceFiscale = request.getParameter("codiceFiscale");
        String tipo = request.getParameter("tipo");
        rimuoviAccount(codiceFiscale,tipo);
      }
      else if (operazione.equals("modifica")) {
        modificaDatiPersonali(request, response, session);

      }
      else if (operazione.equals("caricaMedPaz")) {
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente != null && utente instanceof Amministratore) {
          scaricaDatiPazienteMedico(request,response);
        }
      }

      else {
        throw new Exception("Operazione invalida");
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
    }

  }

  /**
   * Metodo che permette ad una chiamata asincrona di prendere dal database
   * i dati di medici e pazienti e di restituirli in formato JSON.
   * @param request indica il client che ha fatto la richiesta 
   * @param response indica il server che risponde alla richiesta
   * @throws IOException eccezione che viene lanciata in caso si verifichi un errore
   *         di scrittura del file JSON
   */
  private void scaricaDatiPazienteMedico(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(MedicoModel.getAllMedici());
    list.add(PazienteModel.getAllPazienti());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    Gson gg = new Gson();
    response.getWriter().write(gg.toJson(list));
    return;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }

  /**
   * Metodo che permette di rimuovere l'account di un medico o di un paziente.
   * @param codiceFiscale indica il codiceFiscale del paziente o del medico da eliminare
   * @param tipo indica il tipo di utente che può essere medico o paziente
   */
  private void rimuoviAccount(String codiceFiscale,String tipo) {

    if (tipo.equals("medico")) {
      MedicoModel.removeMedico((codiceFiscale));
    }
    else if (tipo.equals("paziente")) {
      PazienteModel.removePaziente(codiceFiscale);
      PianoTerapeuticoModel.removePianoTerapeutico(codiceFiscale);
    }
  }
  
  /**
   * Metodo che modifica i dati personali di un utente Paziente.
   * @param request richiesta
   * @param response risposta
   * @param session sessione
   * @throws ServletException eccezione lanciabile
   * @throws IOException eccezione lanciabile
   */
  private void modificaDatiPersonali(HttpServletRequest request, HttpServletResponse response, 
      HttpSession session) throws ServletException, IOException {
    String codiceFiscale = request.getParameter("codiceFiscale");
    if (request.getParameter("tipoUtente").equals("amministratore")) {
      Amministratore amministratoreLoggato = (Amministratore) session.getAttribute("utente");
      if (amministratoreLoggato != null) {
        String vecchiaPassword = request.getParameter("vecchiaPassword");
        String nuovaPassword = request.getParameter("nuovaPassword");
        String confermaPassword = request.getParameter("confermaPassword");
        if (validaPassword(vecchiaPassword,nuovaPassword,confermaPassword)) {
          String password = 
              AmministratoreModel.getPassword(amministratoreLoggato.getCodiceFiscale());
          vecchiaPassword = CriptazioneUtility.criptaConMD5(vecchiaPassword);
          if (vecchiaPassword.equals(password) && nuovaPassword.equals(confermaPassword)) {
            nuovaPassword = CriptazioneUtility.criptaConMD5(nuovaPassword);
            AmministratoreModel.updateAmministratore(amministratoreLoggato.getCodiceFiscale(),nuovaPassword);
            response.sendRedirect("./dashboard.jsp?notifica=ModificaAdmnRiuscita");  
          } else {
            response.sendRedirect("./resetPasswordAmministratoreView.jsp?notifica=PassErr");
          }
        } else {
          response.sendRedirect("./resetPasswordAmministratoreView.jsp?notifica=PassErr");
        }
      }
    } else {
      String nome = request.getParameter("nome");
      String cognome = request.getParameter("cognome");
      String sesso = request.getParameter("sesso");
      String dataDiNascita = request.getParameter("dataDiNascita");
      String luogoDiNascita = request.getParameter("luogoDiNascita");
      String email = request.getParameter("email");
      String residenza = request.getParameter("residenza");
      String password = request.getParameter("password");
      String confermaPassword = request.getParameter("confermaPsw");


      if (request.getParameter("tipoUtente").equals("paziente")) {
        Paziente paziente = PazienteModel.getPazienteByCF(codiceFiscale);
        if (validazione(codiceFiscale,nome,cognome,sesso,email,residenza,luogoDiNascita,dataDiNascita)) {
          paziente.setCognome(cognome);
          paziente.setNome(nome);
          paziente.setDataDiNascita(LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
          paziente.setEmail(email);
          paziente.setResidenza(residenza);
          paziente.setLuogoDiNascita(luogoDiNascita);
          paziente.setSesso(sesso);
          if (!password.equals("") || !confermaPassword.equals("")) {
            if (validaPassword(password,password,confermaPassword) && password.equals(confermaPassword)) {
              password = CriptazioneUtility.criptaConMD5(password);
              PazienteModel.updatePaziente(paziente);
              PazienteModel.changePassword(codiceFiscale, password);
              response.sendRedirect("./dashboard.jsp?notifica=ModificaPazRiuscita");

            } else {
              response.sendRedirect("./ModificaAccountPazienteView.jsp?notifica=PassErr");

            }
          } else {
            PazienteModel.updatePaziente(paziente);
            response.sendRedirect("./dashboard.jsp?notifica=ModificaPazRiuscita");
          }


        } else {
          response.sendRedirect("./ModificaAccountPazienteView.jsp?notifica=ParamErr");
        }
      }
      else if (request.getParameter("tipoUtente").equals("medico")) {
        Medico medico = MedicoModel.getMedicoByCF(codiceFiscale);
        if (validazione(codiceFiscale,nome,cognome,sesso,email,residenza,luogoDiNascita,dataDiNascita) && !MedicoModel.checkEmail(email)) {
          medico.setCognome(cognome);
          medico.setNome(nome);
          if (!dataDiNascita.equals("")) {
            medico.setDataDiNascita(LocalDate.parse(dataDiNascita,DateTimeFormatter.ofPattern("dd/MM/yyyy")));
          }
          medico.setEmail(email);
          medico.setResidenza(residenza);
          medico.setLuogoDiNascita(luogoDiNascita);
          medico.setSesso(sesso);

          if (!password.equals("") || !confermaPassword.equals("")) {
            if (validaPassword(password,password,confermaPassword) && password.equals(confermaPassword)) {
              password = CriptazioneUtility.criptaConMD5(password);
              MedicoModel.updateMedico(medico);
              MedicoModel.updatePasswordMedico(codiceFiscale, password);
              response.sendRedirect("./dashboard.jsp?notifica=ModificaMedRiuscita");
            } else {
              response.sendRedirect("./ModificaAccountMedicoView.jsp?notifica=PassErr");
            }
          }
          else {
            MedicoModel.updateMedico(medico);
            response.sendRedirect("./dashboard.jsp?notifica=ModificaMedRiuscita");
          }

        } else {
          response.sendRedirect("./ModificaAccountMedicoView.jsp?notifica=ParamErr");

        }
      }
    }
  }
  
  /**
   * Metodo per controllare la conformità dei campi con le regex.
   * @param codiceFiscale da validare
   * @param nome da validare
   * @param cognome da validare
   * @param sesso da validare
   * @param email da validare
   * @param residenza da validare
   * @param luogoDiNascita da validare
   * @param dataDiNascita da validare
   * @return
   */
  private boolean validazione(String codiceFiscale, String nome, String cognome,String sesso, 
      String email,String residenza,String luogoDiNascita,String dataDiNascita) {
    boolean valido = true;
    String expSesso = "^[MF]$";
    String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
    String expNome = "^[A-Z][a-zA-Z ']*$";
    String expCognome = "^[A-Z][a-zA-Z ']*$";
    String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
    String expResidenza = 
        "^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$";
    String expLuogoDiNascita = "^[A-Z][a-zA-Z ']*$";
    String expDataDiNascita = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
    if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
      valido = false;
    }

    if (!Pattern.matches(expNome, nome) || nome.length() < 2 || nome.length() > 30) {
      valido = false;
    }
    if (!Pattern.matches(expCognome, cognome) || cognome.length() < 2 || cognome.length() > 30) {
      valido = false;
    }
    if (!Pattern.matches(expSesso, sesso) || sesso.length() != 1) {
      valido = false;
    }
    
    if (!email.equals(""))
      if (!Pattern.matches(expEmail, email) || email.length() < 5 || email.length() > 50) {
        valido = false;
      }
    if (!residenza.equals(""))
      if (!Pattern.matches(expResidenza, residenza) || residenza.length() < 5 
          || residenza.length() > 50) {
        valido = false;
      }
    if (!luogoDiNascita.equals(""))
      if (!Pattern.matches(expLuogoDiNascita, luogoDiNascita) || luogoDiNascita.length() < 3
          || luogoDiNascita.length() > 30) {
        valido = false;
      }
    if (!dataDiNascita.equals(""))
      if (!Pattern.matches(expDataDiNascita, dataDiNascita)) {
        valido = false;
      }
    return valido;
  }
  
  /**
   * Metodo per controllare la conformità dei campi con le regex.
   * @param vecchiaPassword password da verificare
   * @param nuovaPassword password da verificare
   * @param confermaPassword password da verifcare
   * @return
   */
  private boolean validaPassword(String vecchiaPassword, String nuovaPassword,
      String confermaPassword) {
    boolean valido = true;
    String expPassword = "^[a-zA-Z0-9]*$";
    if (!Pattern.matches(expPassword, vecchiaPassword) || vecchiaPassword.length() < 6 || vecchiaPassword.length() > 20)
      valido = false;
    if (!Pattern.matches(expPassword, nuovaPassword) || nuovaPassword.length() < 6 || nuovaPassword.length() > 20)
      valido = false;
    if (!Pattern.matches(expPassword, confermaPassword) || confermaPassword.length() < 6 || confermaPassword.length() > 20)
      valido = false;
    return valido;
  }
}
