package control;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.Paziente;
import bean.SchedaParametri;
import bean.Utente;
import model.SchedaParametriModel;

/**
 * @author Antonio Donnarumma, Davide Benedetto Strianese, Matteo Falco
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
				inserisciParametri(request);
				response.sendRedirect(request.getContextPath() + "/parametri?operazione=visualizzaScheda");
			}
			//Download report
			else if(operazione.equals("download")) {
				creaExcel(request, response);
			} 
			//Visualizza la scheda dei parametri del paziente selezionato
			else if(operazione.equals("visualizzaScheda")) {
				monitoraParametri(request);
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
	 * Nel caso in cui la visualizzazione sia richiesta da un paziente, esso è richiamato dalla sessione,
	 * nel caso in cui la visualizzazione sia richiesta da un medico, il CF del paziente di cui mostrare le schede
	 * è inserito in un attributo nella request
	 * @param request
	 * @param response
	 * 
	 * @author Antonio Donnarumma, Domenico Musone, Davide Bendetto Strianese
	 */
	private void monitoraParametri(HttpServletRequest request)
	{
		/*
		 * La visualizzazione può essere fatta sia dal medico che dal paziente.
		 * Questo significa che bisogna controllare chi è in sessione.
		 */
		
		HttpSession session = request.getSession();
		Utente utente = null;
		ArrayList<SchedaParametri> scheda = null;
		
		utente = (Utente) session.getAttribute("utente");
		
		if ((boolean)session.getAttribute("ispaziente") == true){
			scheda = SchedaParametriModel.getSchedaParametriByCF(utente.getCodiceFiscale());
		}	
		else if ((boolean)session.getAttribute("ismedico") == true) {
			scheda = SchedaParametriModel.getSchedaParametriByCF(request.getParameter("CFPaziente"));
		}
		else {

			System.out.println("Utente deve esssere loggato");
		}
		
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
	private void inserisciParametri(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		Paziente pazienteLoggato = (Paziente) session.getAttribute("utente");
		
		final String REGEX_CF = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String cf = pazienteLoggato.getCodiceFiscale(); 
		String peso = request.getParameter("Peso");
		String paMin = request.getParameter("PaMin"); 
		String paMax = request.getParameter("PaMax");
		String scaricoIniziale = request.getParameter("ScaricoIniziale"); 
		String uf = request.getParameter("UF");
		String tempoSosta = request.getParameter("TempoSosta"); 
		String scarico = request.getParameter("Scarico");
		String carico = request.getParameter("Carico");
		
		BigDecimal newPeso = null;
		int newPaMin = 0, newPaMax = 0, newScaricoIniziale = 0, newUf = 0, newTempoSosta = 0, newScarico = 0, newCarico = 0;
		SchedaParametri daAggiungere;
		
		try {
			newPeso = new BigDecimal(peso);
			newPaMin = Integer.parseInt(paMin);
			newPaMax = Integer.parseInt(paMax);
			newScaricoIniziale = Integer.parseInt(scaricoIniziale);
			newUf = Integer.parseInt(uf);
			newTempoSosta = Integer.parseInt(tempoSosta);
			newScarico = Integer.parseInt(scarico);
			newCarico = Integer.parseInt(carico);
		} catch (NumberFormatException n) {
			System.out.println("InserisciParametri: Errore nel parsing dei dati passati");
			return;
		}

		if (Pattern.matches(REGEX_CF, cf) && sonoValidi(newPeso, newPaMin, newPaMax, newScaricoIniziale, 
				newUf, newTempoSosta, newScarico, newCarico)) {
			daAggiungere = new SchedaParametri(cf, newPeso, newPaMin, newPaMax, newScaricoIniziale, 
				newUf, newTempoSosta, newScarico, newCarico, LocalDate.now());
			SchedaParametriModel.addSchedaParametri(daAggiungere);
		}
		else {
			System.out.println("InserisciParametri: i dati passati sono malformati");
		}
	}

	private boolean sonoValidi(BigDecimal newPeso, int newPaMin, int newPaMax, int newScaricoIniziale, int newUf,
			int newTempoSosta, int newScarico, int newCarico) {
		if (newPeso.compareTo(new BigDecimal("29")) > 0 && newPeso.compareTo(new BigDecimal("151")) < 0 &&
			newPaMin > 39 && newPaMin < 131 &&
			newPaMax > 79 && newPaMax < 221 &&
			newScaricoIniziale > -1001 && newScaricoIniziale < 3000 &&
			newUf > -1001 && newUf < 1501 &&
			newTempoSosta > 0 && newTempoSosta < 25 &&
			newScarico > 0 && newScarico < 3501 &&
			newCarico > 499 && newCarico < 3001) {
			
			return true;
		}
		
		return false;
	}
	
	/**Questo metodo preleva le schede parametri per un dato intervallo di tempo e inserisce i parametri in un file excel.
	 * 
	 * @author Matteo Falco
	 */
	
	private void creaExcel(HttpServletRequest request, HttpServletResponse response) {

		LocalDate dataInizio = LocalDate.parse(request.getParameter("dataInizio"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate dataFine = LocalDate.parse(request.getParameter("dataFine"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		String pazienteCF = request.getParameter("CFPaziente");
		ArrayList<SchedaParametri> report = SchedaParametriModel.getReportByPaziente(pazienteCF, dataInizio, dataFine);
		
		String fileName = "ReportPaziente-"+ pazienteCF + "-" + dataFine.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
		try {
			PrintWriter out = response.getWriter();
			out.println("Paziente:\t" + pazienteCF);
			out.println("Data\tPeso\tPressione Min\tPressione Max\tScarico Iniziale\tCarico\tScarico\tTempoSosta\tUF");
			for (SchedaParametri scheda : report) {
				out.println(scheda.getDataFormattata()+"\t"+
							scheda.getPeso()+"\t"+
							scheda.getPaMin()+"\t"+
							scheda.getPaMax()+"\t"+
							scheda.getScaricoIniziale()+"\t"+
							scheda.getCarico()+"\t"+
							scheda.getScarico()+"\t"+
							scheda.getTempoSosta()+"\t"+
							scheda.getUF());
			}
		} catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();
		}
		
	}
}