package control;

import java.io.IOException;
import java.util.ArrayList;

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

/**
 * Servlet implementation class GestionePaziente
 */
@WebServlet("/GestionePaziente")
public class GestionePaziente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionePaziente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./login.jsp");
				requestDispatcher.forward(request, response);
			}
			
			} catch (Exception e) {
				System.out.println("Errore durante il caricamento della pagina:");
				e.printStackTrace();
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Metodo che preleva il paziente che richiede il cambio password lo inserisce nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void richiediResetPassword(HttpServletRequest request, HttpServletResponse response) {
		
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
		
	}
	
	/**
	 * Metodo che aggiorna i dati personali di un paziente
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void modificaDatipersonali(HttpServletRequest request, HttpServletResponse response) {
		
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
		
	

}
