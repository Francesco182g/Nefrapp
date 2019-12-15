package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GestioneMessaggio
 */
@WebServlet("/GestioneMessaggi")
public class GestioneMessaggi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneMessaggi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Metodo che prende gli autori e i destinatari del messaggio e salva quest'ultimo sul Database
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void inviaMessaggio(HttpServletRequest request) {
		
	}
	
	/**
	 * Metodo che prende il messaggio e lo salva nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaMessaggio(HttpServletRequest request) {
		
	}

}
