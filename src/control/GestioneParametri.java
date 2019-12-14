package control;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import bean.SchedaParametri;
import model.SchedaParametriModel;

@WebServlet("GestioneParametri")
public class GestioneParametri extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
				
				ArrayList<SchedaParametri> sp = SchedaParametriModel.getSchedaParametriByCF(pazienteCF);
				request.setAttribute("schedaParametri", sp);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //reindirizzamento view per la visualizzazione delle schede
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