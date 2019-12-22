package control;

import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Medico;
import bean.PianoTerapeutico;
import model.PianoTerapeuticoModel;

/**
 * 
 * @author Davide Benedetto Strianese
 * Questa classe è una servlet che si occupa della gestione del piano terapeutico
 */
@WebServlet("/GestionePianoTerapeutico")
public class GestionePianoTerapeutico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp"); 
				dispatcher.forward(request, response);
				return;
			}
			
			String operazione = request.getParameter("operazione");
			
			if(operazione.equals("visualizza")) {
				visualizzaPiano(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/visualizzaPianoTerapeutico.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("modifica")) {
				modificaPiano(request);
				response.sendRedirect(request.getContextPath() + "/listaPazientiView.jsp");
			}
			
			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
			}
			
		}catch (Exception e) {
			System.out.println("Errore in gestione piano terapeutico:");
			e.printStackTrace();		
		}
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
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
		PianoTerapeutico pianoTerapeutico = null;
		String codiceFiscalePaziente = request.getParameter("CFPaziente");
		final String REGEX_CF = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		
		if (Pattern.matches(REGEX_CF, codiceFiscalePaziente)) {
			pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(codiceFiscalePaziente); 
			request.setAttribute("pianoTerapeutico", pianoTerapeutico);
		} else {
			System.out.println("visualizzaPiano: errore nel CF passato");
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
		Medico medico = (Medico) request.getSession().getAttribute("medico");
		if(medico != null) {
			final String REGEX_DATA = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
			String dataFine = request.getParameter("data");
			if(Pattern.matches(REGEX_DATA, dataFine)) {
				String codiceFiscalePaziente = request.getParameter("CFPaziente");
				String diagnosi = request.getParameter("diagnosi");
				String farmaci = request.getParameter("farmaci");
				LocalDate dataFineTerapia  = LocalDate.parse(dataFine);
				PianoTerapeuticoModel.updatePianoTerapeutico(new PianoTerapeutico(codiceFiscalePaziente, diagnosi, farmaci, dataFineTerapia));
			}else {
				//TODO Messaggio d'errore, CF non valido
			}
		}else {
			//TODO messaggio errore perchè il medico non ha loggato
		}
	}
}
