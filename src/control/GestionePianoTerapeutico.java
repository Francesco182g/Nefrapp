package control;

import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.Paziente;
import bean.PianoTerapeutico;
import model.PianoTerapeuticoModel;

/**
 * 
 * @author Davide Benedetto Strianese
 * Questa classe è una servlet che si occupa della gestione del piano terapeutico
 */
public class GestionePianoTerapeutico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home dell'utente
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
			
			//prova, TODO eliminare
			else {
				System.out.println("Errore nel selezionare il tipo di operazione");
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
		
		pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(codiceFiscalePaziente); 
		request.setAttribute("pianoTerapeutico", pianoTerapeutico);
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
			String dataFine = request.getParameter("dataFine");
			if(Pattern.matches(REGEX_DATA, dataFine)) {
				String codiceFiscalePaziente = request.getParameter("CFPaziente");
				String diagnosi = request.getParameter("diagnosi");
				String farmaci = request.getParameter("farmaci");
				LocalDate dataFineTerapia  = LocalDate.parse(dataFine);
				PianoTerapeuticoModel.updatePianoTerapeutico(new PianoTerapeutico(codiceFiscalePaziente, diagnosi, farmaci, dataFineTerapia));
			}else {
				//TODO ritorna messaggio errore, data non valida
			}
		}else {
			//TODO messaggio errore perchè il medico non ha loggato
		}
	}
}
