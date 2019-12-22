package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GestioneAnnunci
 */
@WebServlet("/GestioneAnnunci")
public class GestioneAnnunci extends GestioneComunicazione {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneAnnunci() {
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
	 * Metodo che prende i pazienti a cui il medico desidera inviare un annunccio e salva l'annuncio sul Database
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void inviaAnnuncio(HttpServletRequest request) {
		
	}
	
	/**
	 * Metodo che prende l'annunccio e lo salva nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaAnnuncio(HttpServletRequest request) {
		
	}

}
