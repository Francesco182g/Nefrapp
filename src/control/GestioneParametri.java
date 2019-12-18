package control;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Medico;
import bean.Paziente;
import bean.SchedaParametri;
import model.SchedaParametriModel;

/**
 * @author Antonio Donnarumma, Davide Benedetto Strianese,
 * Questa classe � una servlet che si occupa della gestione dei parametri del paziente
 */
@WebServlet("/GestioneParametri")
public class GestioneParametri extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //TODO reindirizzamento home
				dispatcher.forward(request, response);
				return;
			} 
			
			String operazione = request.getParameter("operazione");
			
			if(operazione.equals("inserisciScheda")) {
				// Valutare la possibilitÃƒÂ  di inserire flag di controllo anche se questo ÃƒÂ¨
				// l'unico metodo eseguito da una post request
				HttpSession session=request.getSession();
				Paziente pazienteLoggato= (Paziente) session.getAttribute("paziente");
				inserisciParametri(pazienteLoggato.getCodiceFiscale(), request.getParameter("Peso"),
						request.getParameter("PaMin"), request.getParameter("PaMax"),
						request.getParameter("ScaricoIniziale"), request.getParameter("UF"),
						request.getParameter("TempoSosta"), request.getParameter("Scarico"), 
						request.getParameter("Carico"));

				response.sendRedirect(request.getContextPath() + "/parametri?operazione=visualizzaScheda");
			}
			//Download report
			else if(operazione.equals("")) {
				//TODO
			} 
			//Visualizza la scheda dei parametri del paziente selezionato
			else if(operazione.equals("visualizzaScheda")) {
				monitoraParametri(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/monitoraggioParametriView.jsp");
				requestDispatcher.forward(request, response);
			}
			
		}catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
		return;
	}

	/**Questo metodo richiama dal database la lista delle schede parametri inserite da un determinato utente.
	 * Nel caso in cui la visualizzazione sia richiesta da un paziente, esso ÃƒÂ¨ richiamato dalla sessione,
	 * nel caso in cui la visualizzazione sia richiesta da un medico, il CF del paziente di cui mostrare le schede
	 * ÃƒÂ¨ inserito in un attributo nella request
	 * @param request
	 * @param response
	 * 
	 * @author Antonio Donnarumma, Domenico Musone, Davide Bendetto Strianese
	 */
	private void monitoraParametri(HttpServletRequest request, HttpServletResponse response)
	{
		/*
		 * La visualizzazione puÃ² essere fatta sia dal medico che dal paziente.
		 * Questo significa che bisogna controllare chi Ã¨ in sessione.
		 */
		
		HttpSession session = request.getSession();
		Medico medico = null;
		Paziente paziente = null;
		ArrayList<SchedaParametri> scheda = null;
		
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");
		
		if (paziente != null && medico == null){
			scheda = SchedaParametriModel.getSchedaParametriByCF(paziente.getCodiceFiscale());
		}	
		else if(paziente == null && medico != null) {
			scheda = SchedaParametriModel.getSchedaParametriByCF(request.getParameter("CFPaziente"));
		}
		else {
			//TODO messaggio di errore
			System.out.println("Utente deve esssere loggato");
		}
		
		//stampa di controllo. TODO da eliminare
		for (SchedaParametri s : scheda)
			System.out.println(s.toString());
		
		request.setAttribute("schedaParametri", scheda);
	}
	
	/**Questo metodo inserisce nel database una SchedaParametri formata dai dati inseriti dall'utente.
	 * Riceve le stringhe inserite dall'utente, prelevate dalla request. 
	 * Dopo averle parsate opportunamente, le usa per istanziare un bean SchedaParametri
	 * e usa un'istanza di SchedaParametriModel per caricarla sul database.
	 * 
	 * @param cf
	 * @param peso
	 * @param paMin
	 * @param paMax
	 * @param scaricoIniziale
	 * @param uf
	 * @param tempoSosta
	 * @param scarico
	 * @param carico
	 * @param data
	 * 
	 * @author nico
	 */
	private void inserisciParametri(String cf, String peso, String paMin, String paMax, String scaricoIniziale, String uf, String tempoSosta, String scarico, String carico)
	{
		SchedaParametri daAggiungere;
		
		//valutare la possibilitÃƒÂ  di fare controlli sulle stringhe ottenute prima di parsarle
		
		BigDecimal newPeso = new BigDecimal(peso);
		int newPaMin = Integer.parseInt(paMin);
		int newPaMax = Integer.parseInt(paMax);
		int newScaricoIniziale = Integer.parseInt(scaricoIniziale);
		int newUf = Integer.parseInt(uf);
		int newTempoSosta = Integer.parseInt(tempoSosta);
		int newScarico = Integer.parseInt(scarico);
		int newCarico = Integer.parseInt(carico);
				
		daAggiungere = new SchedaParametri(cf, newPeso, newPaMin, newPaMax, newScaricoIniziale, 
				newUf, newTempoSosta, newScarico, newCarico, LocalDate.now());
		
		SchedaParametriModel.addSchedaParametri(daAggiungere);
	}
}