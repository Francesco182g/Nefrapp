package control;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import bean.Amministratore;
import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * @author Luca Esposito, Antonio Donnarumma, Davide Benedetto Strianese,
 * Questa classe � una servlet che si occupa della registrazione di un utente del sistema.
 */
@WebServlet("/GestioneRegistrazione")
public class GestioneRegistrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	@Override
	/*
	 * @precondition La richiesta deve essere sincrona
	 * 				 codiceFiscale != null && codiceFiscale.matches("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$") && codiceFiscale.lenght()==16,
	 * 				 nome != null && nome.matches("^[A-Z][a-zA-Z ]*$") && (nome.lenght()>1 && nome.lenght() < 31),
	 * 				 cognome != null && cognome.matches("^[A-Z][a-zA-Z ]*$") && (cognome.lenght() > 1 && cognome.lenght() < 31),
	 * 				 sesso != null && sesso.matches("^[MF]$") && sesso.lenght() == 1,
	 * 				 email != null && email.matches("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$"),
	 * 				 password != null && password.matches("^[a-zA-Z0-9]*$") && (password.lenght() > 4 && password.lenght() < 21),
	 * @postcondition L'utente � stato registrato nel sistema
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home
				dispatcher.forward(request, response);
				return;
			}
			
			HttpSession session = request.getSession();
			String operazione = request.getParameter("operazione");
			if(operazione.equals("registraMedico")) {
				Amministratore amministratore = (Amministratore) session.getAttribute("utente");
				if(amministratore != null) {
					registraMedico(request,response);
				}
			}else if(operazione.equals("registraPazienteMedico")) { //registrazione paziente per il medico
					Medico medicoLoggato = (Medico) session.getAttribute("utente");
					if(medicoLoggato != null) {
						String registrato = request.getParameter("registrato");
							if(registrato.equals("No")) { //paziente non registrato
								ArrayList<String> medici = new ArrayList<String>();
								medici.add(medicoLoggato.getCodiceFiscale());
								registraPaziente(request, response, medici);
								System.out.println("lo facciamo sto redirect");
								response.sendRedirect("./dashboard.jsp");
								
							}else { // solo aggiunta del cf del medico tra i seguiti (paziente già registrato)
									String codiceFiscale = request.getParameter("codiceFiscale");
									if(PazienteModel.getPazienteByCF(codiceFiscale) != null) {
										Paziente paziente = PazienteModel.getPazienteByCF(codiceFiscale);
										paziente.addMedico(medicoLoggato.getCodiceFiscale());
										PazienteModel.updatePaziente(paziente);
										response.sendRedirect("./dashboard.jsp");
									}else {
										//TODO gestione errore nel caso in cui paziente non registrato
										request.setAttribute("notifica","Il paziente non è stato registratro");
										RequestDispatcher requestDispatcher = request.getRequestDispatcher("/registraPazienteMedico.jsp");
										requestDispatcher.forward(request,response);
									}
							}
					}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
		}
		
		return;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
		return;
	}
	
	private void registraMedico(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
			String codiceFiscale = request.getParameter("codiceFiscale");
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String sesso = request.getParameter("sesso");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String residenza = request.getParameter("residenza");
			String luogoDiNascita=request.getParameter("luogoDiNascita");
			String dataDiNascita = request.getParameter("dataDiNascita");
			
			if (validazione(codiceFiscale, nome, cognome, sesso, email, password,residenza,luogoDiNascita,dataDiNascita)) {
				if(MedicoModel.getMedicoByCF(codiceFiscale)==null && PazienteModel.getPazienteByCF(codiceFiscale)==null) {
					Medico medico = new Medico(sesso, residenza, null, codiceFiscale, nome, cognome, email,luogoDiNascita);
					if(!dataDiNascita.equals("")) {
						medico.setDataDiNascita(LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
					}
					password = CriptazioneUtility.criptaConMD5(password);//serve a criptare la pasword in MD5 prima di registrarla nel db ps.non cancellare il commento quando spostate la classe
					MedicoModel.addMedico(medico, password);
					System.out.println("medico registrato");
					response.sendRedirect("./registraMedico.jsp?notifica=registrato");
				}else {
					response.sendRedirect("./registraMedico.jsp?notifica=presente");
					
				}
			}else {
				response.sendRedirect("./registraMedico.jsp?notifica=ParamErr");
			
		}
	}
	
	private void registraPaziente(HttpServletRequest request,HttpServletResponse response, ArrayList<String> medici) throws ServletException, IOException {
		//TODO gestione della data
		String codiceFiscale = request.getParameter("codiceFiscale");
		if(PazienteModel.getPazienteByCF(codiceFiscale) == null) {
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String sesso = request.getParameter("sesso");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String residenza = request.getParameter("residenza");
			String luogoDiNascita=request.getParameter("luogoDiNascita");
			String dataDiNascita = request.getParameter("dataDiNascita");
			
			Paziente paziente = null;
			
			if (validazione(codiceFiscale, nome, cognome, sesso, email, password,residenza,luogoDiNascita,dataDiNascita)) {
				password = CriptazioneUtility.criptaConMD5(password);
				paziente = new Paziente(sesso, codiceFiscale, nome, cognome, email, residenza, luogoDiNascita, LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")), true, medici);
				PazienteModel.addPaziente(paziente,password);
			}else {
				request.setAttribute("notifica","Uno o più parametri del paziente non sono validi.");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/registraPazienteMedico.jsp");
				requestDispatcher.forward(request,response);
			} 
		} else {
			request.setAttribute("notifica","Paziente già presente.");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/registraPazienteMedico.jsp");
			requestDispatcher.forward(request,response);
		}
	}
	
	
	
	
	private boolean validazione(String codiceFiscale, String nome, String cognome,String sesso, String email,String password,String residenza,String luogoDiNascita,String dataDiNascita) {
		boolean valido = true;
		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String expNome = "^[A-Z][a-zA-Z ']*$";
		String expCognome = "^[A-Z][a-zA-Z ']*$";
		String expSesso = "^[MF]$";
		String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
		String expPassword = "^[a-zA-Z0-9]*$";
		String expResidenza= "^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$";
		String expLuogoDiNascita= "^[A-Z][a-zA-Z ']*$";
		String expDataDiNascita="^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
		
		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16)
			valido = false;
		if (!Pattern.matches(expNome, nome) || nome.length() < 2 || nome.length() > 30)
			valido = false;
		if (!Pattern.matches(expCognome, cognome) || cognome.length() < 2 || cognome.length() > 30)
			valido = false;
		if (!Pattern.matches(expPassword, password) || password.length() < 6 || password.length() > 20)
			valido = false;
		if (!Pattern.matches(expSesso, sesso) || sesso.length() != 1)
			valido = false;
		if(!email.equals(""))
			if (!Pattern.matches(expEmail, email))
				valido = false;
		if(!residenza.equals(""))
			if(!Pattern.matches(expResidenza, residenza))
				valido=false;
		if(!luogoDiNascita.equals(""))
			if(!Pattern.matches(expLuogoDiNascita, luogoDiNascita))
				valido=false;
		if(!dataDiNascita.equals(""))
			if(!Pattern.matches(expDataDiNascita, dataDiNascita))
				valido=false;
		return valido;
	}
}