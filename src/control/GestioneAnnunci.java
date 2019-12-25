package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Annuncio;
import bean.Medico;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * @author Davide Benedetto Strianese
 * Questa classe � una servlet che si occupa della gestione degli annunci
 */
@WebServlet("/GestioneAnnunci")
public class GestioneAnnunci extends GestioneComunicazione {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notifica", "Errore generato dalla richiesta!");
				dispatcher = getServletContext().getRequestDispatcher("paginaErrore");
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
				inviaComunicazione(request, operazione);
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
	
	/*
	 * Metodo che prepara la creazione e l'invio dell'annuncio. Caricamento dei dati dei pazienti seguiti dal medico
	 * @param request richiesta utilizzata per ottenere il medico loggato e settare la lista dei pazienti seguiti
	 * @param response
	 */
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
	 * Metodo che prende l'annuncio e lo salva nella richiesta cos� da poter essere visualizzato
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaAnnuncio(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		if(utente != null) {
			Annuncio annuncio = new Annuncio();
			String idAnnuncio = request.getParameter("idAnnuncio");
			annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
			//solo se l'utente è un paziente, la visualizzazione viene settata a false
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				AnnuncioModel.setVisualizzatoAnnuncio(idAnnuncio, true);
				
				if (annuncio != null) {
					annuncio.setAllegato(AlgoritmoCriptazioneUtility.decodificaFile(annuncio.getAllegato()));
				}
			}
			else {
				System.out.println("L'utente deve essere loggato");
			}
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
	
	/**
	 * Metodo che prende gli annunci personali di un medico o di un paziente e li mostra in una lista
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
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