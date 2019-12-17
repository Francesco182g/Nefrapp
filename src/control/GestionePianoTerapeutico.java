package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
			String codiceFiscalePaziente = request.getParameter("CFPaziente");
			
			if(operazione.equals("visualizza")) {
				PianoTerapeutico pianoTerapeutico = null;
				
				//TODO query per prendere il piano terapeutico del paziente
				//pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByCF(codiceFiscalePaziente); controllare nome metodo sull'ODD
				
				request.setAttribute("pianoTerapeutico", pianoTerapeutico);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/visualizzaPianoTerapeutico.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("modifica")) {
				//TODO modifica piano terapeutico
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
}
