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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import bean.Annuncio;
import bean.Medico;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * @author Davide Benedetto Strianese
 */
@WebServlet("/GestioneAnnunci")
public class GestioneAnnunci extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home
				dispatcher.forward(request, response);
				return;
			}
			
			String operazione = request.getParameter("operazione");
			
			if(operazione.equals("crea")) {
				creaAnnuncio(request, response);
				dispatcher.forward(request, response);
				return;
			}
			
			else if(operazione.equals("invia")) {
				inviaAnnuncio(request, response);
				dispatcher.forward(request, response);
				return;
			}
			
			else if(operazione.equals("visualizza")) {
				visualizzaAnnuncio(request, response);
				dispatcher.forward(request, response);
				return;
			}
			
			else if(operazione.equals("visualizzaPersonali")) {
				visualizzaAnnunciPersonali(request, response);
				dispatcher.forward(request, response);
				return;
			}
			
			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				dispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				dispatcher.forward(request, response);
			}
			
		} catch(Exception e) {
			request.setAttribute("notifica", "Errore in Gestione annunci. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request,response);
			return;
		}
		
		return;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		return;
	}
	
	private void creaAnnuncio(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Medico medico = (Medico) session.getAttribute("utente");
		
		if(medico != null) {
			ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
			pazientiSeguiti = PazienteModel.getPazientiSeguiti(medico.getCodiceFiscale());
			request.setAttribute("pazientiSeguiti", pazientiSeguiti);
			dispatcher = getServletContext().getRequestDispatcher("/inserimentoAnnuncio.jsp");
			return;
		}
		else {
			request.setAttribute("notifica", "Operazione non consentita");
			dispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			return;
		}
	}
	
	/**
	 * Metodo che prende i pazienti a cui il medico desidera inviare un annunccio e salva l'annuncio sul Database
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 * @throws ServletException 
	 * @throws IOException 
	 */
	private void inviaAnnuncio(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Medico medico = (Medico) session.getAttribute("utente");
		Annuncio annuncio = new Annuncio();
		
		if(medico != null) {
			ArrayList<String> CFDestinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectPaziente")));
			ArrayList<Paziente> pazienti = new ArrayList<Paziente>();
			for(String d: CFDestinatari) {
				pazienti.add(PazienteModel.getPazienteByCF(d));
			}
			String titolo = request.getParameter("titolo");
			String testo = request.getParameter("testo");
			String allegato = new String();
			
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Part filePart = request.getPart("file");
			InputStream fileContent = filePart.getInputStream();
			if (isMultipart) {
				try {
					allegato = AlgoritmoCriptazioneUtility.codificaInBase64(fileContent);
				} catch (IOException e) {
					System.out.println("InvioMessaggio: errore nella codifica dell'allegato");
				} finally {
					if (fileContent != null) {
						fileContent.close();
					}
				}
			}
			
		    annuncio = new Annuncio(medico, pazienti, titolo, testo, allegato, ZonedDateTime.now(ZoneId.of("Europe/Rome")));
		    AnnuncioModel.addAnnuncio(annuncio);
			
			request.setAttribute("notifica", "Annuncio inviato con successo");
			dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento homeMedico
			return;
		}
		else {
			request.setAttribute("notifica", "Operazione non consentita");
			dispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			return;
		}
	}
	
	/**
	 * Metodo che prende l'annunccio e lo salva nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaAnnuncio(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		if(utente != null) {
			Annuncio annuncio = new Annuncio();
			String idAnnuncio = request.getParameter("idAnnuncio");
			
			annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
			
			request.setAttribute("annuncio", annuncio);
			dispatcher = getServletContext().getRequestDispatcher("/annuncioView.jsp");
			return;
		}
		else {
			request.setAttribute("notifica", "Operazione non consentita");
			dispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			return;
		}
	}
	
	private void visualizzaAnnunciPersonali(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		boolean isMedico = (boolean) session.getAttribute("isMedico");
		boolean isPaziente = (boolean) session.getAttribute("isPaziente");
		
		if(isMedico != false && isPaziente == false) {
			Medico medico = (Medico) session.getAttribute("utente");
			ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
			annunci = AnnuncioModel.getAnnunciByCFMedico(medico.getCodiceFiscale());
			
			request.setAttribute("annunci", annunci);
			dispatcher = getServletContext().getRequestDispatcher("/listaAnnunciView.jsp");
			return;
		}
		
		else if(isMedico == false && isPaziente != false) {
			Paziente paziente = (Paziente) session.getAttribute("utente");
			ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
			annunci = AnnuncioModel.getAnnuncioByCFPaziente(paziente.getCodiceFiscale());
			
			request.setAttribute("annunci", annunci);
			dispatcher = getServletContext().getRequestDispatcher("/listaAnnunciView.jsp");
			return;
		}
		
		else {
			request.setAttribute("notifica", "Operazione non consentita");
			dispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			return;
		}
	}
}