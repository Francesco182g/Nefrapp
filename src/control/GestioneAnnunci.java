package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Annuncio;
import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.MessaggioModel;
import model.PazienteModel;
import model.UtenteModel;
import utility.CriptazioneUtility;

/**
 * @author Davide Benedetto Strianese, Sara
 * Questa classe � una servlet che si occupa della gestione degli annunci
 */
@WebServlet("/GestioneAnnunci")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
maxFileSize = 15728640, // 15MB
maxRequestSize = 15728640) // 15MB
public class GestioneAnnunci extends GestioneComunicazione {
	private static final long serialVersionUID = 1L;
	//private RequestDispatcher dispatcher;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String operazione = request.getParameter("operazione");
			HttpSession session = request.getSession();

			if(operazione.equals("caricaDestinatariAnnuncio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoAnnuncioView.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if (operazione.equals("caricaAllegato")) {
				caricaAllegato(request, "annuncio", session);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson("success"));
				
			}
			
			else if (operazione.equals("rimuoviAllegato")) {
				rimuoviAllegato("messaggio", session);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson("success"));	
			}
			
			else if(operazione.equals("inviaAnnuncio")) {
				inviaComunicazione(request, operazione);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./dashboard.jsp");	
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("visualizza")) {
				visualizzaAnnuncio(request, response);
				RequestDispatcher requestDispatcher =request.getRequestDispatcher("./annuncioView.jsp");
				requestDispatcher.forward(request, response);
			}
			
			else if(operazione.equals("visualizzaPersonali")) {
				String tipo = request.getParameter("tipo");
				visualizzaAnnunciPersonali(request, response,tipo);
				if(!(tipo != null  && tipo.equals("asincrona")))
				{
					RequestDispatcher requestDispatcher =request.getRequestDispatcher("./annunci.jsp");
					requestDispatcher.forward(request, response);
				}
				
			}
			
			else {
				request.setAttribute("notifica", "Operazione scelta non valida");
				RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
				requestDispatcher.forward(request, response);
			}
			
		} catch(Exception e) {
			System.out.println("Errore durante il caricamento della pagina:");
			e.printStackTrace();
			/*request.setAttribute("notifica", "Errore in Gestione annunci. " + e.getMessage());
			RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/paginaErrore.jsp");
			//requestDispatcher.forward(request, response);
			return;*/
		}
		
		return;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		return;
	}
	
	/**
	 * Metodo che prende l'annuncio e lo salva nella richiesta cos� da poter essere visualizzato
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaAnnuncio(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		if(utente != null) {
			String idAnnuncio= request.getParameter("idAnnuncio");
			Annuncio annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
			//solo se l'utente è un paziente, la visualizzazione viene settata a false
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				AnnuncioModel.setVisualizzatoAnnuncio(idAnnuncio, true);
				
				if (annuncio != null) {
					AnnuncioModel.setVisualizzatoAnnuncio(idAnnuncio, true);
					annuncio.setCorpoAllegato(CriptazioneUtility.decodificaStringa(annuncio.getCorpoAllegato(), true));
					String nomeAllegato = CriptazioneUtility.decodificaStringa(annuncio.getNomeAllegato(), false);
					annuncio.setNomeAllegato(nomeAllegato);
				}
			}
			else {
				System.out.println("L'utente deve essere loggato");
			}
			request.setAttribute("annuncio", annuncio);
			
		}
		/*else {
			request.setAttribute("notifica", "Operazione non consentita");
			RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			return;
		}*/
	}
	
	/**
	 * Metodo che prende gli annunci personali di un medico o di un paziente e li mostra in una lista
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 * @param tipo  indica il tipo della chiamata che puo essere asincrona se il tipo != null
	 * @throws IOException 
	 */
	private void visualizzaAnnunciPersonali(HttpServletRequest request, HttpServletResponse response, String tipo) throws IOException {
		HttpSession session = request.getSession();
		boolean isMedico = false;

		boolean isPaziente = false;
		
		if(session.getAttribute("isMedico") != null)
		{
			isMedico = (boolean) session.getAttribute("isMedico");
		}
		if(session.getAttribute("isPaziente") != null)
		{
			isPaziente = (boolean) session.getAttribute("isPaziente");
		}
		
		
		if(isMedico) {
			Medico medico = (Medico) session.getAttribute("utente");
			ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
			annunci = AnnuncioModel.getAnnunciByCFMedico(medico.getCodiceFiscale());
			if(tipo != null  && tipo.equals("asincrona"))
			{
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson(annunci));
			}else
			{
				request.setAttribute("annunci", annunci);
			}
			
		}
		
		else if(isPaziente) {
			Paziente paziente = (Paziente) session.getAttribute("utente");
			ArrayList<Annuncio> annunci = new ArrayList<Annuncio>();
			annunci = AnnuncioModel.getAnnuncioByCFPaziente(paziente.getCodiceFiscale());
			
			
			ArrayList<String> cache = new ArrayList<>();
			ArrayList<Utente> utentiCache = new ArrayList<>();
			Utente utenteSelezionato = new Utente();

			for (Annuncio a : annunci) {
				if (!cache.contains(a.getMedico())) {
					cache.add(a.getMedico());
					utenteSelezionato = UtenteModel.getUtenteByCF(a.getMedico());
					if (utenteSelezionato != null) {
						utentiCache.add(utenteSelezionato);
						request.setAttribute(a.getMedico(),
								utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
					}
				} else if (cache.contains(a.getMedico())) {
					for (Utente ut : utentiCache) {
						if (ut.getCodiceFiscale() == a.getMedico()) {
							utenteSelezionato = ut;
							request.setAttribute(a.getMedico(),
									utenteSelezionato.getNome() + " " + utenteSelezionato.getCognome());
						}
					}

				}
			}
			
			
			if(tipo != null  && tipo.equals("asincrona"))
			{
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson(annunci));
			}else
			{
				request.setAttribute("annunci", annunci);
			}
		}
		
		else {
			request.setAttribute("notifica", "Operazione non consentita");
			return;
		}
	}}