package control;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Amministratore;
import bean.Paziente;
import bean.Utente;
import model.AmministratoreModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * 
 * @author Eugenio Corbisiero, Davide Benedetto Strianese, Silvio Di Martino
 *         Questa classe � una servlet che si occupa della gestione dell'accesso
 *         al sistema
 *
 */
@WebServlet("/GestioneAccesso")
public class GestioneAccesso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona
		// ok)
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
				return;
			}

			String operazione = request.getParameter("operazione");

			HttpSession session = request.getSession();
			synchronized (session) {

				if (operazione.equals("logout")) {
					logout(request);
					response.sendRedirect("dashboard.jsp");
				}

				else if (operazione.equals("loginAdmin")) {
					// Aggiungere un try-catch per catturare IOException
					loginAmministratore(request, response, session);
				}

				else {
					// Aggiungere un try-catch per catturare IOException
					loginUtente(request, response, session);
				}
			}
		} catch (Exception e) {
			request.setAttribute("notifica",e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			requestDispatcher.forward(request,response);
		}
		return;
	}

	/**
	 * Metodo che permette di effettuare la login dell'amministratore
	 * 
	 * @param request  la richiesta al server
	 * @param response la risposta del server
	 * @param session  la sessione in cui deve essere salvato l'amministratore se
	 *                 avviene con successo la login
	 * @throws IOException lancia un eccezione se si verifica un errore di input /
	 *                     output
	 * @throws ServletException 
	 */
	private void loginAmministratore(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		String codiceFiscale = request.getParameter("codiceFiscale");
		String password = request.getParameter("password");
		Amministratore amministratore = null;
		if (controllaParametri(codiceFiscale, password)) {
			//ATTENZIONE: NON VA BENE COSÌ, BISOGNA HASHARE LA PASSWORD NEL FRONTEND. È ILLEGALE FARLO COSÌ.
			password = CriptazioneUtility.criptaConMD5(password);
			amministratore = AmministratoreModel.getAmministratoreByCFPassword(codiceFiscale, password);
			if (amministratore != null) {
				session.removeAttribute("notifica");
				session.setAttribute("isAmministratore", true);
				session.setAttribute("utente", amministratore);
				session.setAttribute("accessDone", true);
				response.sendRedirect("dashboard.jsp");
			} 
			
			else {
				session.setAttribute("accessDone", false);
				response.sendRedirect("loginAmministratore.jsp");
				return;
			}
			
		} else {
			request.setAttribute("notifica","Codice fiscale o password non validi.");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/loginAmministratore.jsp");
			requestDispatcher.forward(request,response);
		}
	}

	private void loginUtente(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		String codiceFiscale = request.getParameter("codiceFiscale");
		String password = request.getParameter("password");
		String ricordaUtente = request.getParameter("ricordaUtente");
		String idPaziente = PazienteModel.getIdPazienteByCF(codiceFiscale);
		Cookie pazienteID;
		Utente utente = null;

		if (controllaParametri(codiceFiscale, password)) {
			password = CriptazioneUtility.criptaConMD5(password);
			utente = MedicoModel.getMedicoByCFPassword(codiceFiscale, password);
			if (utente == null)
			{
				utente = PazienteModel.getPazienteByCFPassword(codiceFiscale, password);
				Paziente paziente = (Paziente) utente;
				if (utente!=null && paziente.getAttivo()==true) {
					session.setAttribute("isPaziente", true);
					session.setAttribute("accessDone", true);
				}
				
				else {
					session.setAttribute("accessDone", false);
					response.sendRedirect("login.jsp");
					return;
				}
			}
			else {
				session.setAttribute("isMedico", true);
				session.setAttribute("accessDone", true);
			}

			if (utente != null) 
			{
				session.setAttribute("utente", utente);
				response.sendRedirect("dashboard.jsp");
				GestioneNotifica gn=new GestioneNotifica();
				gn.doGet(request, response);
				return;

			}

		}
		
		else {
			request.setAttribute("notifica","Codice fiscale o password non validi.");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
			requestDispatcher.forward(request,response);
		}
	}

	/**
	 * Funzione che permette di effettuare il logout
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
	 * Funzione che controlla i parametri codice fiscale e password dell'
	 * amministratore e dell'utente
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

		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16)
			valido = false;
		if (!Pattern.matches(expPassword, password) || password.length() < 6 || password.length() > 20)
			valido = false;
		return valido;
	}
}