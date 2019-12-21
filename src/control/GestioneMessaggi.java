package control;

import java.io.IOException;
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

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import model.MedicoModel;
import model.MessaggioModel;
import model.PazienteModel;
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
	private void inviaMessaggio(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();
		Messaggio messaggio = null;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		System.out.println(isMultipart);
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (paziente != null && medico == null) {
			ArrayList<String> destinatari = new ArrayList<String>(
					Arrays.asList(request.getParameterValues("selectMedico")));
			String CFMittente = paziente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			Part filePart = request.getPart("file");
			String allegato = filePart.getSubmittedFileName();

			/*if (isMultipart) {
				
				final String fileName = uploadFileUtility.getFileName(filePart);
				OutputStream out = null;
				InputStream fileContent = null;
				out = new FileOutputStream(new File(File.separator + fileName));
				fileContent = filePart.getInputStream();
				int read = 0;
		        final byte[] bytes = new byte[1024];
		        while ((read = fileContent.read(bytes)) != -1) {
		            out.write(bytes, 0, read);
		        }
		        allegato=out.toString();
		        out.close();
				System.out.println(allegato);
			}*/

			// inserire qui controlli backend
			messaggio = new Messaggio(CFMittente, destinatari, oggetto, testo, allegato, ZonedDateTime.now(ZoneId.of("Europe/Rome")));
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
			ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Europe/Rome"));

			messaggio = new Messaggio(CFMittente, elencoCFDestinatari, oggetto, testo, allegato, date);
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
	private void visualizzaListaMessaggi(HttpServletRequest request) {
		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();

		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

		if (paziente != null && medico == null) {
			ArrayList<Messaggio> messaggi = new ArrayList <Messaggio>();
			messaggi = MessaggioModel.getMessaggioByCFDestinatario(paziente.getCodiceFiscale());
			request.setAttribute("messaggio", messaggi);
			
			for (Messaggio m : messaggi)
			{
				Medico dottore = MedicoModel.getMedicoByCF(m.getCodiceFiscaleMittente());
				request.setAttribute(m.getCodiceFiscaleMittente(), dottore.getCognome());
			}
		}

		else if (paziente == null && medico != null) {
			ArrayList<Messaggio> m=new ArrayList <Messaggio>();
			m=MessaggioModel.getMessaggioByCFDestinatario(medico.getCodiceFiscale());
			System.out.println(m.toString());
		} else {
			System.out.println("Utente deve esssere loggato");

		}

	}
	
	private void visualizzaMessaggio(HttpServletRequest request) {
		String idMessaggio=request.getParameter("idMessaggio");
		Messaggio messaggio=MessaggioModel.getMessaggioById(idMessaggio);
		MessaggioModel.updateMessaggio(idMessaggio, true);
		request.setAttribute("messaggio", messaggio);
	}
	

}
