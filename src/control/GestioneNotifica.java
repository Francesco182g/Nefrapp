package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Utente;
import model.AnnuncioModel;
import model.MessaggioModel;
import model.PianoTerapeuticoModel;
/**
 * 
 * @author Sara
 */
/**
 * Servlet implementation class GestioneNotifica
 */
@WebServlet("/GestioneNotifica")
public class GestioneNotifica extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GestioneNotifica() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Utente utente = null;
		HttpSession session = request.getSession();
		utente = (Utente) session.getAttribute("utente");
		
		if ((boolean) session.getAttribute("accessDone") == true) {
			
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				// Fai i controlli per il paziente (messaggio, annuncio, piano terapeutico)
				int nm = MessaggioModel.countMessaggiNonLetti(utente.getCodiceFiscale());
				int na = AnnuncioModel.countAnnunciNonLetti(utente.getCodiceFiscale());
				boolean pt = PianoTerapeuticoModel.isPianoTerapeuticoVisualizzato(utente.getCodiceFiscale());
				if (pt) {
					session.setAttribute("notificheAnnunci", na);
					session.setAttribute("notificheMessaggi", nm);
					session.setAttribute("notifichePianoTerapeutico", 1);
				} else {
					session.setAttribute("notificheAnnunci", na);
					session.setAttribute("notificheMessaggi", nm);
				}
			} else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
				// Fai i controlli per il medico (messaggio)
				int nm = MessaggioModel.countMessaggiNonLetti(utente.getCodiceFiscale());
				session.setAttribute("notificheMessaggi", nm);
			}
		} else {
			System.out.println("L'utente deve essere loggato");

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
