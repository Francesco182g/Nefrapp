package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	private void richiediResetPassword(HttpServletRequest request, HttpServlet response) {
		
	}
	
	/**
	 * Metodo che preleva il paziente che richiede la disattivazione del proprio account e lo inserisce nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void disattivaAccount(HttpServletRequest request, HttpServlet response) {
		
	}
	
	/**
	 * Metodo che aggiorna i dati personali di un paziente
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void modificaDatipersonali(HttpServletRequest request, HttpServlet response) {
		
	}
	

}
