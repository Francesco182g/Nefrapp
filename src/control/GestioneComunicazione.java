package control;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.Medico;
import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.MedicoModel;
import model.MessaggioModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * Questa classe è una servlet che svolge le operazioni comuni delle
 * funzionalità di comunicazione
 * 
 * @author nico
 */
@WebServlet("/comunicazione")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
maxFileSize = 15728640, // 15MB
maxRequestSize = 15728640) // 15MB
public class GestioneComunicazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;

	public GestioneComunicazione() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("./dashboard.jsp"); // TODO
																											// reindirizzamento
				// home
				dispatcher.forward(request, response);
				return;
			}

		} catch (Exception e) {
			request.setAttribute("notifica", "Errore in Gestione Comunicazione. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request, response);
			e.printStackTrace();
		}

		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Metodo che carica i destinatari ammessi per inviare messaggi e annunci
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void caricaDestinatari(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");

		if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
			ArrayList<Medico> mediciCuranti = new ArrayList<>();
			Medico selezionato;
			for (String cf : ((Paziente) utente).getMedici()) {
				selezionato = MedicoModel.getMedicoByCF(cf);

				if (selezionato != null)
					mediciCuranti.add(selezionato);
			}
			request.setAttribute("mediciCuranti", mediciCuranti);
		}

		else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
			ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
			pazientiSeguiti.addAll(PazienteModel.getPazientiSeguiti(utente.getCodiceFiscale()));
			request.setAttribute("pazientiSeguiti", pazientiSeguiti);
		}

		else {
			System.out.println("L'utente deve essere loggato");
		}
	}

	/**
	 * Metodo che prende mittente, destinatari, oggetto e testo della
	 * comunicazione e la salva nel database
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 * @param operazione, stringa usata per distinguere l'inserimento di un annuncio dall'inserimento di un messaggio
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void inviaComunicazione(HttpServletRequest request, String operazione)
			throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		ArrayList<String> destinatari = null;
		if (session.getAttribute("accessDone") != null && (boolean) session.getAttribute("accessDone") == true) {
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectMedico")));
			} else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectPaziente")));
			}
			String CFMittente = utente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
			for (String temp : destinatari) {
				destinatariView.put(temp, false);
			}

			String id = (String) session.getAttribute("id");

			if (controllaParametri(CFMittente, oggetto, testo)) {
				if (operazione.equals("inviaMessaggio")) {
					if (id != null) {
						MessaggioModel.updateMessaggio(id, CFMittente, oggetto, testo, null, null, null,
								destinatariView);
					} else {
						MessaggioModel.addMessaggio(new MessaggioCompleto(CFMittente, oggetto, testo, null, null,
								ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView));
					}
				} else if (operazione.equals("inviaAnnuncio")) {
					if (id != null) {
						AnnuncioModel.updateAnnuncio(id, CFMittente, oggetto, testo, null, null, null, destinatariView);
					} else {
						AnnuncioModel.addAnnuncio(new AnnuncioCompleto(CFMittente, oggetto, testo, null, null,
								ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView));
					}
				}
			} else {
				System.out.println("L'utente deve essere loggato");
			}
		}

		session.removeAttribute("allegato");
		session.removeAttribute("nomeFile");
		session.removeAttribute("id");
	}

	protected void caricaAllegato(HttpServletRequest request, String tipo, HttpSession session) {
		String allegato = null;
		String nomeFile = null;
		String id = null;
		Messaggio messaggio = null;
		Annuncio annuncio = null;
		InputStream fileStream = null;

		try {
			Part filePart = request.getPart("file");
			nomeFile = filePart.getHeader("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$",
					"$1");
			if (filePart != null && filePart.getSize() > 0 && controllaFile(nomeFile, filePart.getSize())) {
				fileStream = filePart.getInputStream();
				try {
					allegato = CriptazioneUtility.codificaStream(fileStream);
					nomeFile = CriptazioneUtility.codificaStringa(nomeFile);
					
					if (tipo!= null && tipo.equals("messaggio")) {
						messaggio = new MessaggioCompleto(null, null, null, allegato, nomeFile, ZonedDateTime.now(ZoneId.of("Europe/Rome")), new HashMap<String, Boolean>());
						id = MessaggioModel.addMessaggio(messaggio);
					} else if (tipo!= null && tipo.equals("annuncio")) {
						annuncio = new AnnuncioCompleto(null, null, null, allegato, nomeFile, ZonedDateTime.now(ZoneId.of("Europe/Rome")), new HashMap<String, Boolean>());
						id = AnnuncioModel.addAnnuncio(annuncio);
					}
					
				} catch (Exception e) {
					System.out.println("caricaAllegato: errore nel caricamento del file");
					e.printStackTrace();
				} finally {
					if (fileStream != null) {
						fileStream.close();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("caricaAllegato: errore nel caricamento dell'allegato");
			e.printStackTrace();
		} finally {
			session.setAttribute("id", id);
			session.setAttribute("nomeFile", nomeFile);
			session.setAttribute("allegato", allegato);
			if (nomeFile.equals("form-data; name=\"file\"; filename=\"\"")) {
				nomeFile = null;
			}
		}
	}

	public boolean controllaParametri(String codiceFiscale, String oggetto, String testo) {
		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";

		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
			return false;
		} else if (oggetto.length() < 1 || oggetto.length() > 75) {
			return false;
		} else if (testo.length() < 1 || testo.length() > 1000) {
			return false;
		}

		return true;
	}

	private boolean controllaFile(String nomeFile, long dimensioneFile) {
		String estensione = "";

		if (dimensioneFile == 0) {
			return false;
		}

		// file senza estensione (esistono, basta usare un sistema operativo vero)
		if (dimensioneFile > 0 && !nomeFile.contains(".")) {
			return false;
		}
		// senza questo controllo substring crasha in caso di nessun file e file senza
		// estensione
		if (dimensioneFile > 0 && nomeFile.contains(".")) {
			int indice = nomeFile.indexOf(".");
			estensione = nomeFile.substring(indice);
		}

		if (!estensione.equals("") && !estensione.equals(".jpg") && !estensione.equals(".jpeg")
				&& !estensione.equals(".png") && !estensione.equals(".pjpeg") && !estensione.equals(".pjp")
				&& !estensione.equals(".jfif") && !estensione.equals(".bmp")) {
			return false;
		} else if (dimensioneFile > 15728640l) {
			return false;
		}

		return true;
	}

}
