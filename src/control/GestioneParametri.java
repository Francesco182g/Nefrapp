package control;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Paziente;

@WebServlet("GestioneParametri")
public class GestioneParametri extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//static SchedaParametriModel parametriModel = new SchedaParametriModel();
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			//Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(new Gson().toJson("Errore generato dalla richiesta!"));
				return;
			}
			
			String flag = request.getParameter("flag");
			
			//Download report
			if(flag.equals("0")) {
				
			}
			
			//Inserisci parametri nella scheda
			if(flag.equals("1")) {
				
			}
			
			//Visualizza la scheda dei parametri del paziente selezionati
			if(flag.equals("2")) {
				String pazienteCF = request.getParameter("codiceFiscale");
				
				//TODO query per il recupero della scheda parametri del paziente
				
				request.setAttribute("schedaParametri", /*risultato della query se non è null*/);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //reindirizzamento view per la visualizzazione della scheda
				dispatcher.forward(request, response);
			}
			
		}catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
		return;
	}
}