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
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.PazienteModel;
import utility.CriptazioneUtility;

/**
 * @author Davide Benedetto Strianese
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
			System.out.println(operazione);
			HttpSession session = request.getSession();
			/*Da Sara: mi sono permessa di conformare l'inserimento dell'annuncio a quello dei messaggi.
			 * si richiama il metodo "caricaDestinatari" della superclasse GestioneComunicazioni il quale preleva i possibili
			 * destinatari per un determinato medico e li salva nella sessione.
			 * Questo lo fa solo dalla side bar...
			 */
			if(operazione.equals("caricaDestinatariAnnuncio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoAnnuncioView.jsp");
				requestDispatcher.forward(request, response);
			}
			/**
			 * ..questo credo lo faccia dalla dashboard.
			 */
			if(operazione.equals("crea")) {
				creaAnnuncio(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoAnnuncioView.jsp");
				requestDispatcher.forward(request, response);
			}
			
			//per Eugenio: la chiamata asincrona per l'upload triggera QUESTO
			if (operazione.equals("caricaAllegato")) {
				caricaAllegato(request, operazione,session);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson("success"));
				
			}
			
			else if(operazione.equals("inviaAnnuncio")) {
				System.out.println("voglio inviare un annuncio");
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
					RequestDispatcher requestDispatcher =request.getRequestDispatcher("./listaAnnunciView.jsp");
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
	
	//questo metodo è puro codice duplicato, basta chiamare caricaDestinatari 
	//also, attenti a 'sti nomi, onegai
	//questo metodo non "crea un annuncio" in nessun modo immaginabile. Non crea nulla,
	//e non ha a che fare con gli annunci in nessun punto. Non c'è nemmeno un annuncio dentro.
	//creaAnnuncio() è un nome tanto azzeccato quanto lo sarebbe sgretolaPandoro() o abbracciaMilf()
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
		}
		
		/*else {
			request.setAttribute("notifica", "Operazione non consentita");
			RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/paginaErrore");
			requestDispatcher.forward(request, response);
			
		}*/
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
			System.out.println("stampa annunci :"+annunci);
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
	}
}