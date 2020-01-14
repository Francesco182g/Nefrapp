package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import model.AnnuncioModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * @author Antonio Donnarumma, Davide Benedetto Strianese, Matteo Falco Questa
 *         clase � una servlet che si occupa della gestione del medico
 *
 */
@WebServlet("/GestioneMedico")
public class GestioneMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String operazione = request.getParameter("operazione");
			System.out.println(operazione);
			Amministratore amministratore = null;
			Medico medico = (Medico) request.getSession().getAttribute("utente");
			if (medico == null) {
				amministratore = (Amministratore) request.getSession().getAttribute("utente");
			}

			if (medico == null && amministratore == null) {
				request.setAttribute("notifica", "Non si hanno i permessi necessari");
				dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				return;
			}

			if (operazione.equals("visualizzaProfilo")) {
				ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
				request.setAttribute("pazientiSeguiti", pazientiSeguiti);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./profilo.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			if (operazione.equals("modifica")) {
				request.setAttribute("notifica", "Modifica effettuata con successo"); // Nel caso in cui la modifica non
																						// avviene con successo allora
																						// la stringa verr� cambiata
				modificaAccount(request, response);
				return;

			} else if (operazione.equals("VisualizzaPazientiSeguiti")) {
				String tipo = request.getParameter("tipo");
				ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
				if (tipo != null && tipo.equals("asincrona")) {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					Gson gg = new Gson();
					response.getWriter().write(gg.toJson(pazientiSeguiti));
				} else {
					request.setAttribute("pazientiSeguiti", pazientiSeguiti);
					dispatcher = getServletContext().getRequestDispatcher("/listaPazientiView.jsp"); // reindirizzamento
																										// pazienti
					dispatcher.forward(request, response);
					return;
				}

			} else if (operazione.equals("eliminaAccount")) {
				eliminaAccount(request, response);
				if (!response.isCommitted()) {
					response.sendRedirect("./login.jsp?notifica=accountDisattivato");// Lascio "Disattivato" perchè non
																						// so cosa va a cambiare
				}
				return;

			} else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
				return;
			}
		} catch (Exception e) {
			request.setAttribute("notifica", "Errore in Gestione medico. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		return;
	}

	/**
	 * Questo metodo elimina un account di un utente dal database.
	 * 
	 * @param request
	 * 
	 * @param response
	 */
	private void eliminaAccount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Medico medico = (Medico) session.getAttribute("utente");
		if (medico != null) {

			if (PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale()).size() != 0) {
				// non è possibile effettuare l'eliminazione, notificare al medico che non può
				// proseguire con l'opeazione fino a che
				// segue dei pazienti.
			} else {
				MedicoModel.removeMedico(medico.getCodiceFiscale());
				request.setAttribute("notifica", "Account eliminato con successo");
				session.removeAttribute("utente");
				session.setAttribute("accessDone", false);
			}

		}

	}

	/**
	 * Questo metodo modifica i dati personali dell'utente Medico
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void modificaAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		String codiceFiscale = request.getParameter("codiceFiscale");
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String sesso = request.getParameter("sesso");
		String email = request.getParameter("email");
		String residenza = request.getParameter("residenza");
		String luogoDiNascita = request.getParameter("luogoDiNascita");
		String dataDiNascita = request.getParameter("dataDiNascita");
		String password = request.getParameter("password");
		String confermaPsw = request.getParameter("confermaPsw");

		if (validazione(codiceFiscale, nome, cognome, sesso, email, residenza, luogoDiNascita, dataDiNascita, password,
				confermaPsw)) {

			// Medico medico = MedicoModel.getMedicoByCF(codiceFiscale);
			Medico medico = (Medico) session.getAttribute("utente");

			if (medico != null) {

				if ((!email.equals(medico.getEmail())) && (MedicoModel
						.getMedicoByEmail(email) != null /* TODO || PazienteModel.getPazientebyEmail(email) */)) {
					response.sendRedirect("./ModificaAccountMedicoView.jsp?notifica=EmailGi�InUso");
					return;
				}

				medico.setNome(nome);
				medico.setCognome(cognome);
				medico.setSesso(sesso);
				medico.setEmail(email);
				medico.setResidenza(residenza);
				medico.setLuogoDiNascita(luogoDiNascita);

				if (!dataDiNascita.equals("")) {
					medico.setDataDiNascita(LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				}
				MedicoModel.updateMedico(medico);
				password = CriptazioneUtility.criptaConMD5(password);// serve a criptare la pasword in MD5
																		// prima di registrarla nel db ps.non
																		// cancellare il commento quando
																		// spostate la classe

				MedicoModel.updatePasswordMedico(medico.getCodiceFiscale(), password);
				session.setAttribute("medico", medico);

				response.sendRedirect("./profilo.jsp?notifica=modificaEffettuata");

			} else {
				request.setAttribute("notifica", "Non � stato trovato il medico da aggiornare");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);
			}
		} else {
			request.setAttribute("notifica", "Uno o pi� parametri del medico non sono validi.");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("./ModificaAccountMedicoView.jsp");
			requestDispatcher.forward(request, response);
		}
	}
/**
 * Metodo per controllare la conformità dei campi con le regex.
 * @param codiceFiscale
 * @param nome
 * @param cognome
 * @param sesso
 * @param email
 * @param residenza
 * @param luogoDiNascita
 * @param dataDiNascita
 * @param password
 * @param confermaPsw
 * @return
 */
	private boolean validazione(String codiceFiscale, String nome, String cognome, String sesso, String email,
			String residenza, String luogoDiNascita, String dataDiNascita, String password, String confermaPsw) {

		boolean valido = true;

		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String expNome = "^[A-Z][a-zA-Z ']*$";
		String expCognome = "^[A-Z][a-zA-Z ']*$";
		String expSesso = "^[MF]$";
		String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
		String expResidenza = "^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$";
		String expLuogoDiNascita = "^[A-Z][a-zA-Z ']*$";
		String expDataDiNascita = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
		String expPassword = "^[a-zA-Z0-9]*$";

		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
			valido = false;
			System.out.println("1");
		}

		if (!Pattern.matches(expNome, nome) || nome.length() < 2 || nome.length() > 30) {
			valido = false;
			System.out.println("2");
		}
		if (!Pattern.matches(expCognome, cognome) || cognome.length() < 2 || cognome.length() > 30)
			valido = false;
		if (!Pattern.matches(expPassword, password) || password.length() < 6 || password.length() > 20) {
			valido = false;
			System.out.println("3");
		}
		if (!Pattern.matches(expSesso, sesso) || sesso.length() != 1) {
			valido = false;
			System.out.println("4");
		}
		if (!Pattern.matches(expEmail, email)) {
			valido = false;
			System.out.println("5");
		}
		if (!residenza.equals(""))
			if (!Pattern.matches(expResidenza, residenza)) {
				valido = false;
				System.out.println("6");
			}
		if (!luogoDiNascita.equals(""))
			if (!Pattern.matches(expLuogoDiNascita, luogoDiNascita)) {
				valido = false;
				System.out.println("7");
			}
		if (!dataDiNascita.equals(""))
			if (!Pattern.matches(expDataDiNascita, dataDiNascita)) {
				valido = false;
				System.out.println("8");
			}
		if (!confermaPsw.equals(password)) {
			valido = false;
			System.out.println("9");
		}

		return valido;
	}
}