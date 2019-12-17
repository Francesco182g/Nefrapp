package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import model.AmministratoreModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * @author Luca Esposito
 * Questa classe è una servlet che si occupa della gestione delle funzionalità dell'amministratore
 */
@WebServlet("/GestioneAmministratore")
public class GestioneAmministratore extends HttpServlet {
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
					HttpSession session=request.getSession();
					String operazione = request.getParameter("operazione");
					if(operazione.equals("rimuoviAccount")) {
						Object daRimuovere=(Object)session.getAttribute("daRimuovere");
						if(daRimuovere instanceof Medico) {
							MedicoModel.removeMedico(((Medico)daRimuovere).getCodiceFiscale());;
						}
						else if(daRimuovere instanceof Paziente) {
							PazienteModel.removePaziente(((Paziente) daRimuovere).getCodiceFiscale());
						}
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dashboard.jsp"); 
						dispatcher.forward(request, response);
					}else if(operazione.equals("ModificaDatiPersonali")) {
						Amministratore amministratore= (Amministratore) session.getAttribute("amministratore");
						if(amministratore!=null) {
							String vecchiaPassword=request.getParameter("vecchiaPassword");
							String nuovaPassword=request.getParameter("nuovaPassword");
							String confermaPassword=request.getParameter("confermaPassword");
							String password= AmministratoreModel.getPassword(amministratore.getCodiceFiscale());
							vecchiaPassword = AlgoritmoCriptazioneUtility.criptaConMD5(vecchiaPassword);
							if(vecchiaPassword.equals(password) && nuovaPassword.equals(confermaPassword)) {
								nuovaPassword = AlgoritmoCriptazioneUtility.criptaConMD5(nuovaPassword);
								AmministratoreModel.updateAmministratore(amministratore.getCodiceFiscale(),nuovaPassword);
							}
						}
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dashboard.jsp"); 
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
