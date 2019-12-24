package control;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

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
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); // TODO reindirizzamento
																								// home
				dispatcher.forward(request, response);
				return;
			}

			String operazione = request.getParameter("operazione");
			if (operazione.equals("caricaDestinatariMessaggio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);
			}
			if (operazione.equals("inviaMessaggio")) {
				inviaMessaggio(request, response);
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
	 * Metodo che prende mittente, destinatari, oggetto, testo e allegato del
	 * messaggio e lo salva nel database
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 * @throws ServletException
	 * @throws IOException
	 */
	private void inviaMessaggio(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Utente utente = null;
		HttpSession session = request.getSession();
		Messaggio messaggio = null;
		utente = (Utente) session.getAttribute("utente");
		ArrayList<String> destinatari = null;
		if ((boolean) session.getAttribute("accessDone") == true) {
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectMedico")));
			} else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectPaziente")));
			}
			String CFMittente = utente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			String allegato = new String();

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Part filePart = request.getPart("file");
			if (controllaParametri(CFMittente, oggetto, testo, filePart.getSubmittedFileName(), filePart.getSize())) {
				InputStream fileContent = filePart.getInputStream();
				if (isMultipart) {
					try {
						allegato = AlgoritmoCriptazioneUtility.codificaFile(fileContent);
					} finally {
						if (fileContent != null) {
							fileContent.close();
						}

					}
				}
			} else {

				// inserire il request dispatcher con la variabile notifica ricordare di mettere
				// anche il campo hidden nella jsp
			}

			// inserire qui controlli backend
			messaggio = new Messaggio(CFMittente, destinatari, oggetto, testo, allegato,
					ZonedDateTime.now(ZoneId.of("Europe/Rome")));
			MessaggioModel.addMessaggio(messaggio);

			// qua verrebbe un notify() ai medici se avessimo un observer

		} else {
			System.out.println("L'utente deve essere loggato");
		}
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

		if ((boolean) session.getAttribute("accessDone") == true) {
			ArrayList<String> cache = new ArrayList<>();
			ArrayList<Utente> utentiCache = new ArrayList<>();
			Utente utenteSelezionato = new Utente();
			ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();
			messaggi = MessaggioModel.getMessaggioByCFDestinatario(utente.getCodiceFiscale());
			request.setAttribute("messaggio", messaggi);

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

	private void visualizzaMessaggio(HttpServletRequest request) {
		String idMessaggio = request.getParameter("idMessaggio");
		Messaggio messaggio = MessaggioModel.getMessaggioById(idMessaggio);
		if (messaggio!=null) {
			MessaggioModel.setVisualizzatoMessaggio(idMessaggio, true);
			messaggio.setAllegato(AlgoritmoCriptazioneUtility.decodificaFile(messaggio.getAllegato()));

			request.setAttribute("messaggio", messaggio);
		}
	}

	public boolean controllaParametri(String codiceFiscale, String oggetto, String testo, String nomeFile,
			long dimensioneFile) {
		boolean valido = true;
		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String estensione = "";

		// file senza estensione (esistono, basta usare un sistema operativo vero)
		if (dimensioneFile > 0 && !nomeFile.contains(".")) {
			valido = false;
		}
		// senza questo controllo substring crasha in caso di nessun file e file senza
		// estensione
		if (dimensioneFile > 0 && nomeFile.contains(".")) {
			int indice = nomeFile.indexOf(".");
			estensione = nomeFile.substring(indice);
		}

		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
			valido = false;
		} else if (oggetto.length() < 1 || oggetto.length() > 75) {
			valido = false;
		} else if (testo.length() < 1 || testo.length() > 1000) {
			valido = false;
		} else if (!estensione.equals("") && !estensione.equals(".jpg") && !estensione.equals(".jpeg")
				&& !estensione.equals(".png") && !estensione.equals(".pjpeg") && !estensione.equals(".pjp")
				&& !estensione.equals(".jfif") && !estensione.equals(".bmp")) {
			valido = false;
		} else if (dimensioneFile > 15728640l) {
			valido = false;
		}
		
		System.out.println(valido);
		return valido;
	}

}
