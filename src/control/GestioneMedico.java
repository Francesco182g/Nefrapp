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

import com.google.gson.Gson;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;

/**
 * @author Antonio Donnarumma, Davide Benedetto Strianese, Matteo Falco
 * Questa clase � una servlet che si occupa della gestione del medico
 *
 */
@WebServlet("/GestioneMedico")
public class GestioneMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
		try {
			
			String operazione = request.getParameter("operazione");
			Amministratore amministratore = null;
			Medico medico = (Medico) request.getSession().getAttribute("utente");
			if(medico == null) {
				amministratore = (Amministratore) request.getSession().getAttribute("utente");
			}
		
			
			if(medico == null || amministratore == null) {
				request.setAttribute("notifica", "Non si hanno i permessi necessari");
				dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			if(operazione.equals("visualizzaProfilo"))
			{
				ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
				request.setAttribute("pazientiSeguiti", pazientiSeguiti);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./profilo.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			if (operazione.equals("modifica")) {
				request.setAttribute("notifica", "Modifica effettuata con successo"); //Nel caso in cui la modifica non avviene con successo allora la stringa verr� cambiata
				modificaAccount(request, response);
				dispatcher.forward(request, response);
				return;

			} 
			else if (operazione.equals("VisualizzaPazientiSeguiti")) {
				String tipo = request.getParameter("tipo");
				ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
				if(tipo != null  && tipo.equals("asincrona"))
				{
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					Gson gg = new Gson();
					response.getWriter().write(gg.toJson(pazientiSeguiti));
				}
				else {
					request.setAttribute("pazientiSeguiti", pazientiSeguiti);
					dispatcher = getServletContext().getRequestDispatcher("/listaPazientiView.jsp"); // reindirizzamento pazienti
					dispatcher.forward(request, response);
					return;
				}
				

			} 
			else if (operazione.equals("elimina")) {
				MedicoModel.removeMedico(medico.getCodiceFiscale());
				request.setAttribute("notifica", "Account eliminato con successo");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				return;
			} 
			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
				return;
			}
		}catch (Exception e) {
			request.setAttribute("notifica", "Errore in Gestione medico. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request,response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		return;
	}

	private void modificaAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
				dispatcher = request.getRequestDispatcher(""); //TODO reindirizzamento pagina modifica (chiedere admin) 
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