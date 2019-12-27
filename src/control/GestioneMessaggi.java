package control;

import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.messaging.saaj.util.Base64;

import bean.Messaggio;
import bean.Utente;
import model.MessaggioModel;
import model.UtenteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * @author Sara, Nico
 */
/**
 * Servlet implementation class GestioneMessaggio
 */
@WebServlet("/GestioneMessaggi")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
		maxFileSize = 15728640, // 15MB
		maxRequestSize = 15728640) // 15MB
public class GestioneMessaggi extends GestioneComunicazione {
	private static final long serialVersionUID = 1L;

	public GestioneMessaggi() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); // TODO reindirizzamento																						// home
				dispatcher.forward(request, response);
				return;
			}

			String operazione = request.getParameter("operazione");
			if (operazione.equals("caricaDestinatariMessaggio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);
			}
			
			//per Eugenio: la chiamata asincrona per l'upload triggera QUESTO
			if (operazione.equals("caricaAllegato")) {
				caricaAllegato(request, operazione);
				//qualunque cosa tu voglia fare dopo una chiamata asincrona
				
				//per Eugenio: ricordati di togliere la chiamata a caricaAllegato() da inviaComunicazione
				//quando avrai pronto il bottone che carica soltanto l'allegato
				//altrimenti caricherà di nuovo l'allegato anche quando premi invio
			}
			
			if (operazione.equals("inviaMessaggio")) {
				inviaComunicazione(request, operazione);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);
				// forward temporaneo alla dashboard, TODO bisogna decidere cosa fare
			}
			if (operazione.equals("visualizzaElencoMessaggio")) {
				visualizzaListaMessaggi(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./listaMessaggiView.jsp");
				requestDispatcher.forward(request, response);
			}
			if (operazione.equals("visualizzaMessaggio")) {
				visualizzaMessaggio(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./messaggioView.jsp");
				requestDispatcher.forward(request, response);
			}
		} catch (Exception e) {
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
		doGet(request, response);
	}

	/**
	 * Metodo che prende la lista dei messaggi ricevuti dall'utente e lo salva nella
	 * richiesta
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 */
	private void visualizzaListaMessaggi(HttpServletRequest request) {
		Utente utente = null;
		HttpSession session = request.getSession();

		utente = (Utente) session.getAttribute("utente");

		if (session.getAttribute("accessDone") != null && (boolean) session.getAttribute("accessDone") == true) {
			ArrayList<String> cache = new ArrayList<>();
			ArrayList<Utente> utentiCache = new ArrayList<>();
			Utente utenteSelezionato = new Utente();
			ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();
			messaggi = MessaggioModel.getMessaggiByDestinatario(utente.getCodiceFiscale());

			if (messaggi != null)
				request.setAttribute("messaggio", messaggi);
			else
				return;

			// piccolo sistema di caching per minimizzare le query sui destinatari che hanno
			// mandato messaggi
			// prima di fare la query sul destinatario controlla se ce l'ha già in cache
			// attraverso il suo CF.
			// Se ce l'ha lo usa per ottenere le informazioni che servono alla pagina jsp
			// se non ce l'ha effettua la query e immette il risultato in cache.
			// In questo modo se ci sono 200 messaggi da 5 destinatari fa 5 query e non 200.
			for (Messaggio m : messaggi) {
				if (!cache.contains(m.getCodiceFiscaleMittente())) {
					cache.add(m.getCodiceFiscaleMittente());
					utenteSelezionato = UtenteModel.getUtenteByCF(m.getCodiceFiscaleMittente());
					if (utenteSelezionato != null) {
						utentiCache.add(utenteSelezionato);
						request.setAttribute(m.getCodiceFiscaleMittente(),
								utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
					}
				} else if (cache.contains(m.getCodiceFiscaleMittente())) {
					for (Utente ut : utentiCache) {
						if (ut.getCodiceFiscale() == m.getCodiceFiscaleMittente()) {
							utenteSelezionato = ut;
							request.setAttribute(m.getCodiceFiscaleMittente(),
									utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
						}
					}

				}
			}
		} else {
			System.out.println("L'utente deve essere loggato");
		}

	}
	
	/**
	 * Metodo che prende l'id di un messaggio dalla request e lo usa
	 * per prendere il messaggio corrispondente dal database, decriptarne l'allegato
	 * e mettere nella request le informazioni da mostrare
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 */
	private void visualizzaMessaggio(HttpServletRequest request) {
		String idMessaggio = request.getParameter("idMessaggio");
		Messaggio messaggio = MessaggioModel.getMessaggioById(idMessaggio);
		String nomeAllegato = messaggio.getNomeAllegato();
		String corpoAllegato = messaggio.getCorpoAllegato();
		Utente utente=new Utente();
		utente=(Utente) request.getSession().getAttribute("utente");
		
		if (messaggio != null) {
			MessaggioModel.setVisualizzatoMessaggio(idMessaggio, utente.getCodiceFiscale(),true);
			if (nomeAllegato!=null && corpoAllegato!=null) {
				messaggio.setCorpoAllegato(AlgoritmoCriptazioneUtility.decodificaFile(corpoAllegato));
				nomeAllegato = AlgoritmoCriptazioneUtility.decodificaFile(nomeAllegato);
				messaggio.setNomeAllegato(Base64.base64Decode(nomeAllegato));
			}

			request.setAttribute("messaggio", messaggio);
		}
	}

}
