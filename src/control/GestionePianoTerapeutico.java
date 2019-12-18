package control;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Paziente;
import bean.PianoTerapeutico;

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
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home dell'utente
				dispatcher.forward(request, response);
				return;
			}
			
			String operazione = request.getParameter("operazione");
			
			if(operazione.equals("visualizza")) {
				visualizzaPiano(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/visualizzaPianoTerapeutico.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("modifica")) {
				modificaPiano(request, response);
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
	private void visualizzaPiano(HttpServletRequest request, HttpServletResponse response) {
		PianoTerapeutico pianoTerapeutico = null;
		String codiceFiscalePaziente = request.getParameter("CFPaziente");
		
		//pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(codiceFiscalePaziente); 
		request.setAttribute("pianoTerapeutico", pianoTerapeutico);
	}
	
	private void modificaPiano(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
