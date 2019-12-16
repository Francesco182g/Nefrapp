package control;

import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import model.AmministratoreModel;
import model.MedicoModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;
/**
 * 
 * @author Eugenio Corbisiero, Davide Benedetto Strianese
 * Questa classe è una servlet che si occupa della gestione dell'accesso al sistema
 *
 */
@WebServlet("/GestioneAccesso")
public class GestioneAccesso extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	/*
	 * Questo metodo si occupa di effettuare il logout
	 * @precondition l'utente deve essere loggato
	 * @postcondition utente disconnesso dal sistema
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("view/index.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			HttpSession session = request.getSession();
			if(session != null) {
				session.invalidate();
			}
			response.sendRedirect("view/index.jsp");
		}catch(Exception e) {
			System.out.println("Errore in Gestione accesso:");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		//Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("view/index.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			String codiceFiscale = request.getParameter("codiceFiscale");
			String password = request.getParameter("password");
			String operazione = request.getParameter("operazione");
			String ricordaUtente = request.getParameter("ricordaUtente");
			
			HttpSession session = request.getSession();
			synchronized (session) {
				if(operazione.equalsIgnoreCase("admin")){
					Amministratore amministratore = null;
					if(controllaParametri(codiceFiscale, password)){
						password = AlgoritmoCriptazioneUtility.criptaConMD5(password);
						amministratore = AmministratoreModel.checkLogin(codiceFiscale, password);
						if(amministratore != null){
							session.setAttribute("amministratore", amministratore);
							response.sendRedirect("view/dashboard.jsp");
						}
						else{
							response.sendRedirect("view/loginAmministratore.jsp");
						}
					}
				}
				
				else if(operazione.equalsIgnoreCase("paziente")){
					Cookie[] cookies = request.getCookies();
					
					if(cookies != null){
						for(Cookie cookie: cookies){
							if(cookie.getName().equals("salvaPass")){
								password = cookie.getValue();
							}
							
							if(cookie.getName().equals("salvaCF")){
								codiceFiscale = cookie.getValue();
							}
						}
					}
					
					Cookie salvaPass;
					Cookie salvaCF;
					Paziente paziente = null;
					
					if(controllaParametri(codiceFiscale, password)){					
						//password = AlgoritmoCriptazioneUtility.criptaConMD5(password);  
						paziente = PazienteModel.checkLogin(codiceFiscale, password);

						if(paziente != null){
							session.setAttribute("paziente", paziente);
							session.setAttribute("accessDone", true);
							
							if(ricordaUtente != null){
								salvaPass= new Cookie ("salvaPass", password);
								salvaCF = new Cookie ("salvaCF", codiceFiscale);
								salvaPass.setMaxAge(50000);
								salvaCF.setMaxAge(50000);
								response.addCookie(salvaPass);
								response.addCookie(salvaCF);
							}
							
							response.sendRedirect("view/dashboard.jsp");
						}
					}
					else{
						response.sendRedirect("view/login.jsp");
					}
				}
				
				else if(operazione.equalsIgnoreCase("medico")){
					Medico medico = null;
					if(controllaParametri(codiceFiscale, password)){
						password = AlgoritmoCriptazioneUtility.criptaConMD5(password);
						medico = MedicoModel.checkLogin(codiceFiscale, password);
						if(medico != null){
							session.setAttribute("medico", medico);
							response.sendRedirect("view/dashboard.jsp");
						}
						else{
							response.sendRedirect("view/login.jsp"); //reindirizzamento login per il medico/paziente
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();		
		}
		
		return;	
	}
	
	/**
	 * Funzione che controlla i parametri codice fiscale e password dell' amministratore e dell'utente
	 * @param codiceFiscale indica il codice fiscale dell' amministratore o dell'utente 
	 * @param password indica la password dell'amministratore o dell'utente
	 * @return true se i controlli vanno a buon fine false altrimenti
	 */
	public boolean controllaParametri(String codiceFiscale, String password)
	{
		boolean valido=true;
		String expCodiceFiscale="^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String expPassword="^[a-zA-Z0-9]*$";
		
		if (!Pattern.matches(expCodiceFiscale, codiceFiscale)||codiceFiscale.length()!=16)
			valido=false;
		if (!Pattern.matches(expPassword, password)||password.length()<6||password.length()>20)
			valido=false;
		return valido;
	}
}