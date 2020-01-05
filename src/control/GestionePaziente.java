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

import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * Servlet implementation class GestionePaziente
 */
/**
 * @author Silvio Di Martino
 * Questa clase è una servlet che si occupa della gestione del paziente
 *
 */
@WebServlet("/GestionePaziente")
public class GestionePaziente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionePaziente() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("./dashboard.jsp"); // TODO reindirizzamento home
				dispatcher.forward(request, response);
				return;
			}	
	
			String operazione = request.getParameter("operazione");

			if(operazione.equals("visualizzaProfilo"))
			{
				caricaMedici(request,response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./profilo.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("disattivaAccount"))
			{
				disattivaAccount(request, response);
				if (!response.isCommitted()) {
					response.sendRedirect("./login.jsp?notifica=accountDisattivato");
				}
			}
			
			else if(operazione.equals("modificaAccount"))
			{
				modificaDatiPersonali(request, response);
				caricaMedici(request,response);
				if (!response.isCommitted()) {
					response.sendRedirect("./profilo.jsp?notifica=modificaEffettuata");	
				}
				return;
			}
			
			} catch (Exception e) {
				System.out.println("Errore durante il caricamento della pagina:");
				e.printStackTrace();
				if (!response.isCommitted()) {
					response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");	
				}
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Metodo che preleva il paziente che richiede la disattivazione del proprio account e lo inserisce nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void disattivaAccount(HttpServletRequest request, HttpServletResponse response) {
		Paziente paziente = null;
		HttpSession session = request.getSession();
		paziente = (Paziente) session.getAttribute("utente");
		
		if(paziente!=null){
			paziente.setAttivo(false);
			PazienteModel.updatePaziente(paziente);
			}
		
		session.removeAttribute("utente");
		session.setAttribute("accessDone", false);

		
	}
	
	/**
	 * Metodo che aggiorna i dati personali di un paziente
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */	
	private void modificaDatiPersonali(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		String codiceFiscale = request.getParameter("codiceFiscale");
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String sesso = request.getParameter("sesso");
		String email = request.getParameter("email");
		String residenza = request.getParameter("residenza");
		String luogoDiNascita = request.getParameter("luogoDiNascita");
		String dataDiNascita = request.getParameter("dataDiNascita");
		String password = request.getParameter("password");
		String confermaPsw = request.getParameter("confermaPsw");

		if (validazione(codiceFiscale, nome, cognome, sesso, email, residenza, luogoDiNascita, dataDiNascita, password, confermaPsw)) {
			
			Paziente paziente = (Paziente) session.getAttribute("utente");
			
			if (paziente != null) {
				paziente.setCodiceFiscale(codiceFiscale);
				paziente.setNome(nome);
				paziente.setCognome(cognome);
				paziente.setSesso(sesso);
				paziente.setEmail(email);
				paziente.setResidenza(residenza);
				paziente.setLuogoDiNascita(luogoDiNascita);
				paziente.setDataDiNascita(LocalDate.parse(dataDiNascita, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				password = CriptazioneUtility.criptaConMD5(password);
				
				PazienteModel.changePassword(paziente.getCodiceFiscale(), password);
				PazienteModel.updatePaziente(paziente);
				
				session.setAttribute("paziente", paziente);
				
				
				
			} else {
				System.out.println("modificaDatiPersonali: Paziente non trovato");
				request.setAttribute("notifica", "Non è stato trovato il paziente da aggiornare");
			}
		} else {
			request.setAttribute("notifica", "Formato parametri non valido");
		}
	}
		
	
	/**
	 * Metodo che carica i medici che seguono il paziente in sessione
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */	
	private void caricaMedici(HttpServletRequest request, HttpServletResponse response){
		Paziente paziente = null;
		HttpSession session = request.getSession();
		paziente = (Paziente) session.getAttribute("utente");
		
		if(paziente!=null){
			ArrayList<Medico> mediciCuranti = new ArrayList<>();
			for (String cf : paziente.getMedici()) {
				mediciCuranti.add(MedicoModel.getMedicoByCF(cf));
				}
			request.setAttribute("mediciCuranti", mediciCuranti);
			}
		
	}
	
	private boolean validazione(String codiceFiscale, String nome, String cognome, String sesso, String email,
			String residenza, String luogoDiNascita, String dataDiNascita, String password, String confermaPsw) {

		boolean valido = true;

		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String expNome = "^[A-Z][a-zA-Z ']*$";
		String expCognome = "^[A-Z][a-zA-Z ']*$";
		String expSesso = "^[MF]$";
		String expEmail = "^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
		String expResidenza = "^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$";
		String expLuogoDiNascita = "^[A-Z][a-zA-Z ']*$";
		String expDataDiNascita = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
		String expPassword = "^[a-zA-Z0-9]*$";

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
		if (email.length()!=0) {
			if(!Pattern.matches(expEmail, email))
				valido = false;
			}
		if (!residenza.equals(""))
			if (!Pattern.matches(expResidenza, residenza))
				valido = false;
		if (!luogoDiNascita.equals(""))
			if (!Pattern.matches(expLuogoDiNascita, luogoDiNascita))
				valido = false;
		if (!dataDiNascita.equals(""))
			if (!Pattern.matches(expDataDiNascita, dataDiNascita))
				valido = false;
		if (!confermaPsw.equals(password))
			valido = false;

		return valido;
	}	

}
