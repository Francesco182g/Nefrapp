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
 * @author Davide Benedetto Strianese, Questa classe � una servlet che si occupa
 *         del reset della password di un medico
 */
@WebServlet("/GestioneResetPassword")
public class GestioneResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); // TODO reindirizzamento
																								// home
				dispatcher.forward(request, response);
				return;
			}

			HttpSession session = request.getSession();
			Utente utente = (Utente) session.getAttribute("utente");
			// Controllo per verifica se c'� un utente in sessione, se � presente allora si
			// reindirizza alla home
			if (utente != null) {
				request.setAttribute("notifica", "Non � possibile effettuare questa operazione se si � loggati");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				return;
			}

			String operazione = request.getParameter("operazione");

			// Viene scelta l'operaizione per richiedere il reset della password
			if (operazione.equals("identificaRichiedente")) {
				identificaRichiedente(request, response);
				dispatcher.forward(request, response);
				return;
			}

			if (operazione.equals("richiesta")) {
				richiediReset(request, response);
				dispatcher.forward(request, response);
				return;
			}

			// Operaizone per effettuare il reset della password
			else if (operazione.equals("reset")) {
				effettuaReset(request, response);
				dispatcher.forward(request, response);
				return;
			}

			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
			}

		} catch (Exception e) {
			request.setAttribute("notifica", "Errore in Gestione reset password medico" + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			requestDispatcher.forward(request, response);
		}

		return;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		return;
	}

	/**
	 * Prende in esame il CF inserito dall'utente e ne esamina l'esistenza e
	 * l'appartenenza a una categoria tra paziente e medico.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void identificaRichiedente(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);

			} else if (PazienteModel.getIdPazienteByCF(utente.getCodiceFiscale()) != null) {
				// è un paziente, viene mandata una mail, dove il destinatario è
				// l'amministratore
				String destinatario = "cuccy15@hotmail.it"; // mail di prova
				InvioEmailUtility.inviaEmail(destinatario);
				// TODO: non fa il dispatch
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);
			}
		} else { // il CF inserito non è nel DD TODO: reindirizzamento dove???
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("./richiestaResetView.jsp");
			requestDispatcher.forward(request, response);
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
				dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				return;

			} catch (Exception e) {
				request.setAttribute("notifica", "Si � veriifcato un'erorre durante l'invio dell'eamil. Riprova");
				dispatcher = getServletContext().getRequestDispatcher("/richiestaResetView.jsp");
				return;
			}
		} else {
			request.setAttribute("notifica", "Email inserita non valida");
			dispatcher = getServletContext().getRequestDispatcher("/richiestaResetView.jsp");
			return;
		}
	}

	private void effettuaReset(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String codiceFiscale = request.getParameter("codiceFiscale");
		String password = request.getParameter("password");
		String confermaPsw = request.getParameter("confermaPsw");

		if (validaReset(email, codiceFiscale, password, confermaPsw)) {
			Medico medico = MedicoModel.getMedicoByCF(codiceFiscale);

			if (medico.getEmail().equals(email)) {
				MedicoModel.updatePasswordMedico(codiceFiscale, password);
				request.setAttribute("notifica", "Reset password avvenuto con successo");
				dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				return;
			} else {
				request.setAttribute("notifica",
						"Codice fiscale ed indirizzo email non appartengono allo stesso accounto. Riprova");
				dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				return;
			}
		} else {
			request.setAttribute("notifica", "Formato parametri non valido");
			dispatcher = getServletContext().getRequestDispatcher("/richiestaResetView.jsp");
			return;
		}
	}

	private boolean validaMail(String email) {

		final String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";

		if (Pattern.matches(expEmail, email))
			return true;

		return false;
	}

	private boolean validaReset(String email, String codiceFiscale, String password, String confermaPsw) {
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