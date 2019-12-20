package control;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import model.MedicoModel;
import model.MessaggioModel;
import model.PazienteModel;

/**
 * Servlet implementation class GestioneMessaggio
 */
@WebServlet("/GestioneMessaggi")
@MultipartConfig
public class GestioneMessaggi extends HttpServlet {
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

			// questo if permette alla pagina che si occupa dell'invio del messaggio di
			// caricare nella request i possibili destinatari
			// sia dal lato medico che dal lato paziente (non ho ancora la query pushata da
			// Antonio per il lato paziente)
			if (operazione.equals("inserimentoMessaggioView")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);
			}

			if (operazione.equals("inviaMessaggio")) {
				inviaMessaggio(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
				requestDispatcher.forward(request, response);
				// forward temporaneo alla dashboard, bisogna decidere cosa fare
			}
			if (operazione.equals("visualizzaMessaggio")) {
				visualizzaMessaggio(request);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");
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

	private void caricaDestinatari(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");
		System.out.println(medico);
		System.out.println(paziente);

		if (paziente != null && medico == null) {
			ArrayList<Medico> mediciCuranti = new ArrayList<>();
			for (String cf : paziente.getMedici()) {
				mediciCuranti.add(MedicoModel.getMedicoByCF(cf));
			}
			request.setAttribute("mediciCuranti", mediciCuranti);
		}

		// da Nico: per dare i medici al paziente loggato ho usato l'array di medici
		// curanti presente sia nella collection che nel bean del paziente.
		// Vi suggerisco di dare anche al medico il campo con l'array di pazienti
		// associati (il fatto che la cosa non sia simmetrica e' molto strano peraltro)
		// ma non ho voluto farlo io perche' ora state dormendo e non posso chiedervi il
		// permesso. Ciao.
		else if (medico != null && paziente == null) {
			ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
			pazientiSeguiti.addAll(PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale()));
			request.setAttribute("pazientiSeguiti", pazientiSeguiti);
		}

		else {
			System.out.println("L'utente deve essere loggato");
		}
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
	private void inviaMessaggio(HttpServletRequest request) throws IOException, ServletException {

		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();
		Messaggio messaggio = null;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println(isMultipart);
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (paziente != null && medico == null) {
			ArrayList<String> destinatari = new ArrayList<String>(
					Arrays.asList(request.getParameterValues("selectMedico")));
			String CFMittente = paziente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			String allegato = null;
			if (isMultipart) {
				System.out.println("ho capito che c'è un file da prendere");
				Part filePart = request.getPart("file"); // <input type="file" name="file">
				System.out.println(filePart.toString());
				// String fileName =
				// Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				InputStream fileContent = null;
				fileContent = filePart.getInputStream();
				allegato = fileContent.toString();
				System.out.println(allegato);
			}

			// inserire qui controlli backend
			messaggio = new Messaggio(CFMittente, destinatari, oggetto, testo, allegato, LocalTime.now(),
					LocalDateTime.now());
			MessaggioModel.addMessaggio(messaggio);
			// qua verrebbe un notify() ai medici se avessimo un observer

		} else if (paziente == null && medico != null) {
			String CFMittente = medico.getCodiceFiscale();
			String CFDestinatari = request.getParameter("cfdestinataro");
			ArrayList<String> elencoCFDestinatari = new ArrayList<String>();
			elencoCFDestinatari.add(CFDestinatari);
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			// encoding dell'allegato da fare (nel pacchetto utility)

			String allegato = request.getParameter("allegato");
			LocalTime ora = LocalTime.now();
			LocalDateTime date = LocalDateTime.now();

			messaggio = new Messaggio(CFMittente, elencoCFDestinatari, oggetto, testo, allegato, ora, date);
			MessaggioModel.addMessaggio(messaggio);
			// observer per i pazienti
		} else {
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
		System.out.println("voglio leggere");
		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();
		Messaggio messaggio = null;

		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (paziente != null && medico == null) {
			ArrayList<Messaggio> m=new ArrayList <Messaggio>();
			m=MessaggioModel.getMessaggioByCFDestinatario(paziente.getCodiceFiscale());
			System.out.println(m.toString());
		}

		else if (paziente == null && medico != null) {
			ArrayList<Messaggio> m=new ArrayList <Messaggio>();
			m=MessaggioModel.getMessaggioByCFDestinatario(medico.getCodiceFiscale());
			System.out.println(m.toString());
		} else {
			System.out.println("Utente deve esssere loggato");
		}

	}

}
