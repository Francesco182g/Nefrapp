package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import model.MessaggioModel;
import model.PazienteModel;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub0
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home
				dispatcher.forward(request, response);
				return;
			} 

			
			String operazione=request.getParameter("operazione");
			Medico medico = null;
			Paziente paziente = null;
			
			//questo if permette alla pagina che si occupa dell'invio del messaggio di caricare nella request i possibili destinatari
			//sia dal lato medico che dal lato paziente (non ho ancora la query pushata da Antonio per il lato paziente)
			if(operazione.equals("inserimentoMessaggioView")) {
				HttpSession session = request.getSession();
				medico = (Medico) session.getAttribute("medico");
				paziente = (Paziente) session.getAttribute("paziente");
				System.out.println(medico);
				System.out.println(paziente);

				if(paziente!=null && medico==null) {
				//query (prendi tutti i medici che hanno in cura il paziente e mettili in un array
				//inseriscili nella request
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);	
				}
			
				else if (medico != null && paziente == null) {
					ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
					pazientiSeguiti.addAll(PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale()));
					request.setAttribute("pazientiSeguiti", pazientiSeguiti);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/inserimentoMessaggioView.jsp");
					requestDispatcher.forward(request, response);
				}
			
				else {
					System.out.println("Utente deve esssere loggato");
				}	
			}
		}
		catch (Exception e) {
			System.out.println("Errore durante il caricamento della pagina:");
			e.printStackTrace();
		}
		return;
	}

	/**
	 * @throws ServletException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Medico medico = null;
		Paziente paziente = null;
		String operazione = request.getParameter("operazione");
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (operazione.equals("inviaMessaggio")) {
			inviaMessaggio(request, response, session);
		}
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * Metodo che prende gli autori e i destinatari del messaggio e salva
	 * quest'ultimo sul Database
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 */
	private void inviaMessaggio(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Medico medico = null;
		Paziente paziente = null;

		Messaggio messaggio;

		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (paziente != null && medico == null) {

			String CFMittente = paziente.getCodiceFiscale();
			String CFDestinatari = request.getParameter("cfdestinataro");
			ArrayList<String> elencoCFDestinatari = new ArrayList<String>();
			elencoCFDestinatari.add(CFDestinatari);
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			// encoding dell'allegato da fare (nel paccheto utility)
			String allegato = request.getParameter("allegato");
			LocalTime ora = LocalTime.now();
			LocalDate date = LocalDate.now();

			messaggio = new Messaggio(CFMittente, elencoCFDestinatari, oggetto, testo, allegato, ora, date);
			MessaggioModel.addMessaggio(messaggio);
			// observer per i medici
		} else if (paziente == null && medico != null) {
			String CFMittente = medico.getCodiceFiscale();
			String CFDestinatari = request.getParameter("cfdestinataro");
			ArrayList<String> elencoCFDestinatari = new ArrayList<String>();
			elencoCFDestinatari.add(CFDestinatari);
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			// encoding dell'allegato da fare (nel paccheto utility)

			String allegato = request.getParameter("allegato");
			LocalTime ora = LocalTime.now();
			LocalDate date = LocalDate.now();

			messaggio = new Messaggio(CFMittente, elencoCFDestinatari, oggetto, testo, allegato, ora, date);
			MessaggioModel.addMessaggio(messaggio);
			// observer per i pazienti
		} else {
			// TODO messaggio di errore
			System.out.println("Utente deve esssere loggato");
		}
	}

	/**
	 * Metodo che prende il messaggio e lo salva nella richiesta
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 */
	private void visualizzaMessaggio(HttpServletRequest request) {

	}

}
