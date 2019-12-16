package control;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Amministratore;
import bean.Paziente;
import model.AmministratoreModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;
/**
 * 
 * @author Eugenio
 *
 */
@WebServlet("/GestioneAccesso")
public class GestioneAccesso extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		if(session != null)
		    session.invalidate();
		resp.sendRedirect("/Nefrapp/view/index.jsp");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
		try {
			if("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
				resp.setContentType("application/json");
				resp.setHeader("Cache-Control", "no-cache");
				resp.getWriter().write(new Gson().toJson("Errore generato dalla richiesta!"));
				return;
			}
			
			String codiceFiscale = req.getParameter("codiceFiscale");
			String password = req.getParameter("password");
			String operazione = req.getParameter("operazione");
			String ricordaUtente=req.getParameter("ricordaUtente");
			
			HttpSession session = req.getSession();
			synchronized (session) {
				if(operazione.equalsIgnoreCase("admin"))
				{
					Amministratore amministratore = null;
					if(controllaParametri(codiceFiscale, password))
					{
						password = AlgoritmoCriptazioneUtility.criptaConMD5(password);
						amministratore =AmministratoreModel.checkLogin(codiceFiscale, password);
						if(amministratore != null)
						{
							session.setAttribute("amministratore", amministratore);
							resp.sendRedirect("view/dashboard.jsp");
						}
						else
						{
							resp.sendRedirect("view/loginAmministratore.jsp");
						}
					}
				}
				
				else if(operazione.equalsIgnoreCase("paziente"))
				{
					Cookie[] cookies=req.getCookies();
					
					if(cookies!=null)
					{
						for(Cookie cookie: cookies)
						{
							if(cookie.getName().equals("salvaPass"))
							{
								password = cookie.getValue();
							}
							
							if(cookie.getName().equals("salvaCF"))
							{
								codiceFiscale = cookie.getValue();
							}
						}
					}
					
					Cookie salvaPass;
					Cookie salvaCF;
					
					Paziente paziente = null;
					if(controllaParametri(codiceFiscale, password))
					{					
						//password = AlgoritmoCriptazioneUtility.criptaConMD5(password);  
						paziente = PazienteModel.checkLogin(codiceFiscale, password);

						if(paziente!=null)
						{
							session.setAttribute("paziente", paziente);
							session.setAttribute("accessDone", true);
							
							if(ricordaUtente!=null)
							{
								salvaPass= new Cookie ("salvaPass", password);
								salvaCF= new Cookie ("salvaCF", codiceFiscale);
								salvaPass.setMaxAge(50000);
								salvaCF.setMaxAge(50000);
								resp.addCookie(salvaPass);
								resp.addCookie(salvaCF);
							}
							
							resp.sendRedirect("view/dashboard.jsp");
						}
						
					}

					else
					{
						resp.sendRedirect("view/loginPaziente.jsp");
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