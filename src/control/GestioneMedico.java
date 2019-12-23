package control;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * @author Antonio Donnarumma, Davide Benedetto Strianese, Matteo Falco Servlet
 *         implementation class GestioneMedico
 */
@WebServlet("/GestioneMedico")
public class GestioneMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona
		// ok)
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				return;
			}
			String operazione = request.getParameter("operazione");

			if (operazione.equals("modifica")) {
				request.setAttribute("notifica", "Modifica effettuata con successo"); // Se ci� non avviene la stringa
																						// viene cambiata dal metodo
				modifica(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(""); // TODO reindirizzamento pagina
																						// di modifica
				requestDispatcher.forward(request, response);
				return;

			} else if (operazione.equals("VisualizzaPazientiSeguiti")) {
				Medico medico = (Medico) request.getSession().getAttribute("medico");
				ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
				request.setAttribute("pazientiSeguiti", pazientiSeguiti);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/listaPazientiView.jsp"); // reindirizzamento pazienti
				dispatcher.forward(request, response);

			} else if (operazione.equals("elimina")) {
				Medico medico = (Medico) request.getSession().getAttribute("medico");
				MedicoModel.removeMedico(medico.getCodiceFiscale());
				request.setAttribute("notifica", "Account eliminato con successo");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
			} else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			System.out.println("Errore in gestione medico:");
			e.printStackTrace();
		}

		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		return;
	}

	private void modifica(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO verificare i nomi dei parametri con la jsp
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

			Medico medico = MedicoModel.getMedicoByCF(codiceFiscale);

			if (medico != null) {

				medico.setNome(nome);
				medico.setCognome(cognome);
				medico.setSesso(sesso);
				medico.setEmail(email);
				medico.setResidenza(residenza);
				medico.setLuogoDiNascita(luogoDiNascita);

				if (!dataDiNascita.equals("")) {
					medico.setDataDiNascita(LocalDate.parse(dataDiNascita));
				}
				MedicoModel.updateMedico(medico);
				//password = AlgoritmoCriptazioneUtility.criptaConMD5(password);// serve a criptare la pasword in MD5
																				// prima di registrarla nel db ps.non
																				// cancellare il commento quando
																				// spostate la classe

				// TODO aggiorna dati del medico, anche la password
			} else {
				request.setAttribute("notifica", "Non � stato trovato il medico da aggiornare");
			}
		} else {
			request.setAttribute("notifica", "Formato parametri non valido");
		}
	}

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

		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16)
			valido = false;
		if (!Pattern.matches(expNome, nome) || nome.length() < 2 || nome.length() > 30)
			valido = false;
		if (!Pattern.matches(expCognome, cognome) || cognome.length() < 2 || cognome.length() > 30)
			valido = false;
		if (!Pattern.matches(expPassword, password) || password.length() < 6 || password.length() > 20)
			valido = false;
		if (!Pattern.matches(expSesso, sesso) || sesso.length() != 1)
			valido = false;
		if (!Pattern.matches(expEmail, email))
			valido = false;
		if (!residenza.equals(""))
			if (!Pattern.matches(expResidenza, residenza))
				valido = false;
		if (!luogoDiNascita.equals(""))
			if (!Pattern.matches(expLuogoDiNascita, luogoDiNascita))
				valido = false;
		if (!dataDiNascita.equals(""))
			if (!Pattern.matches(expDataDiNascita, dataDiNascita))
				valido = false;
		if (!confermaPsw.equals(password))
			valido = false;

		return valido;
	}
}