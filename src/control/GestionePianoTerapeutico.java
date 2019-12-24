package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.PianoTerapeutico;
import bean.Utente;
import model.AnnuncioModel;
import model.PianoTerapeuticoModel;

/**
 * 
 * @author Davide Benedetto Strianese
 * Questa classe è una servlet che si occupa della gestione del piano terapeutico
 */
@WebServlet("/GestionePianoTerapeutico")
public class GestionePianoTerapeutico extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp"); 
				dispatcher.forward(request, response);
				return;
			}
			
			String operazione = request.getParameter("operazione");
			
			if(operazione.equals("visualizza")) {
				visualizzaPiano(request);
				dispatcher = request.getRequestDispatcher("/visualizzaPianoTerapeutico.jsp");
				dispatcher.forward(request, response);
			}
			
			else if(operazione.equals("modifica")) {
				modificaPiano(request);
				response.sendRedirect(request.getContextPath() + "/listaPazientiView.jsp");
			}
			
			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
			}
			
		}catch (Exception e) {
			request.setAttribute("notifica", "Errore in Gestione piano terapeutico. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request,response);
			return;		
		}
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
		return;
	}
	
	/**Questo metodo richiama dal database il piano terapeutico del paziente che ne ha richiesto la visualizzazione.
	 * @param request
	 * @param response
	 * 
	 * @author Domenico Musone
	 */
	private void visualizzaPiano(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		PianoTerapeutico pianoTerapeutico = null;
		String codiceFiscalePaziente = request.getParameter("CFPaziente");
		final String REGEX_CF = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		
		if (Pattern.matches(REGEX_CF, codiceFiscalePaziente)) {
			pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(codiceFiscalePaziente); 
			request.setAttribute("pianoTerapeutico", pianoTerapeutico);
		} else {
			System.out.println("visualizzaPiano: errore nel CF passato");
		}
		
		if(utente != null) {
			//solo se l'utente è un paziente, la visualizzazione viene settata a false
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				PianoTerapeuticoModel.setVisualizzatoPianoTerapeutico(codiceFiscalePaziente, true);
			}
			else {
				System.out.println("L'utente deve essere loggato");
			}
		}
	}
	
	
	/**
	 * 
	 * Questo metodo modifica il piano terapeutico di un paziente
	 * @param request richiesta che contiene i parametri da aggiornare
	 * 
	 * @author Antonio Donnarumma
	 */
	private void modificaPiano(HttpServletRequest request) {
		Medico medico = (Medico) request.getSession().getAttribute("utente");

		if(medico != null) {
			final String REGEX_DATA = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
			String dataFine = request.getParameter("data");
			if(Pattern.matches(REGEX_DATA, dataFine)) {
				System.out.println(dataFine);
				String codiceFiscalePaziente = request.getParameter("CFPaziente");
				String diagnosi = request.getParameter("diagnosi");
				String farmaci = request.getParameter("farmaci");
				LocalDate dataFineTerapia  = LocalDate.parse(dataFine, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				PianoTerapeuticoModel.updatePianoTerapeutico(new PianoTerapeutico(codiceFiscalePaziente, diagnosi, farmaci, dataFineTerapia));
			}else {
				//TODO Messaggio d'errore, CF non valido
			}
		}else {
			//TODO messaggio errore perchè il medico non ha loggato
		}
	}
}
