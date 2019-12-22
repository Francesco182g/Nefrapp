	package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import bean.Utente;
import model.MedicoModel;
import model.MessaggioModel;
import model.PazienteModel;
import model.UtenteModel;
import utility.AlgoritmoCriptazioneUtility;
/**
 * @author Sara, Nico
 */
/**
 * Servlet implementation class GestioneMessaggio
 */
@WebServlet("/GestioneMessaggi")
@MultipartConfig
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
				caricaDestinatari(request,response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);
			}
			if (operazione.equals("inviaMessaggio")) {
				inviaMessaggio(request,response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);
				// forward temporaneo alla dashboard, TODO bisogna decidere cosa fare
			}
			if (operazione.equals("visualizzaElencoMessaggio")) {
				visualizzaListaMessaggi(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./listaMessaggiView.jsp");
				requestDispatcher.forward(request, response);
			}
			if(operazione.equals("visualizzaMessaggio")) {
				visualizzaMessaggio(request);
				RequestDispatcher requestDispatcher =request.getRequestDispatcher("./messaggioView.jsp");
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

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		utente = (Utente) session.getAttribute("utente");

		if ((boolean) session.getAttribute("accessDone") == true) {
			ArrayList<String> destinatari = new ArrayList<String>(
					Arrays.asList(request.getParameterValues("selectMedico")));
			String CFMittente = utente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			String allegato = new String();

			Part filePart = request.getPart("file");
			InputStream fileContent = filePart.getInputStream();
			File f = new File(getServletContext() + "temp");
			OutputStream outputStream = null;

			if (isMultipart) {
				try {
					outputStream = new FileOutputStream(f);

					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = fileContent.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					allegato = AlgoritmoCriptazioneUtility.codificaInBase64(f);
				} finally {
					if (outputStream != null) {
						outputStream.close();
						f.delete();
					}
				}
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
	 * Metodo che prende la lista dei messaggi ricevuti dall'utente e lo salva nella richiesta
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
			Utente utenteSelezionato = null;
			ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();
			messaggi = MessaggioModel.getMessaggioByCFDestinatario(utente.getCodiceFiscale());
			request.setAttribute("messaggio", messaggi);

			// piccolo sistema di caching per minimizzare le query sui destinatari che hanno
			// mandato messaggi
			// prima di fare la query sul destinatario controlla se ce l'ha già in cache
			// attraverso il suo CF.
			// Se ce l'ha lo usa per ottenere le informazioni che servono alla pagina jsp
			// se non ce l'ha effettua la query.
			// In questo modo se ci sono 200 messaggi da 5 medici fa 5 query e non 200.
			for (Messaggio m : messaggi) {
				if (!cache.contains(m.getCodiceFiscaleMittente())) {
					cache.add(m.getCodiceFiscaleMittente());
					utenteSelezionato = UtenteModel.getUtenteByCF(m.getCodiceFiscaleMittente());
					utentiCache.add(utenteSelezionato);
				}
				if (cache.contains(m.getCodiceFiscaleMittente())) {
					for (Utente ut : utentiCache) {
						if (ut.getCodiceFiscale() == m.getCodiceFiscaleMittente()) {
							utenteSelezionato = ut;
							break;
						}
					}
				}

				if (utenteSelezionato != null) {
					request.setAttribute(m.getCodiceFiscaleMittente(), utenteSelezionato.getNome() 
							+ " " + utenteSelezionato.getCognome());
				}
			}
		} else {
			System.out.println("L'utente deve essere loggato");
		}

	}
	
	private void visualizzaMessaggio(HttpServletRequest request) {
		String idMessaggio=request.getParameter("idMessaggio");
		Messaggio messaggio=MessaggioModel.getMessaggioById(idMessaggio);
		MessaggioModel.updateMessaggio(idMessaggio, true);
		request.setAttribute("messaggio", messaggio);
	}
	

}
