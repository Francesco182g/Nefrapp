package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Medico;
import bean.Paziente;
import model.PazienteModel;

/**
 * @author Antonio
 * Servlet implementation class GestioneMedico
 */
@WebServlet("/GestioneMedico")
public class GestioneMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(new Gson().toJson("Errore generato dalla richiesta!"));
				return;
			}
			
			String operazione = request.getParameter("operazione");
			if(operazione.equals("ResetPassword")) {
				//cose da fare
			}else if(operazione.equals("ModificaDatiPersonali")) {
				//cose da fare
			}else if (operazione.equals("VisualizzaPazientiSeguiti")) {
				visualizzaPazientiseguiti(request);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/listaPazientiView.jsp"); //reindirizzamento view per la visualizzazione della lista pazienti
				dispatcher.forward(request, response);
				
			}else {
				throw new Exception("Operazione invalida");
			}	
		} catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();		
		}
		
		return;	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Metodo che prende i pazienti seguiti dal medico e li inserisce nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaPazientiseguiti(HttpServletRequest request) {
		Medico medico = (Medico) request.getSession().getAttribute("medico");
		ArrayList<Paziente> pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
		request.setAttribute("pazientiSeguiti", pazientiSeguiti);
	}

}
