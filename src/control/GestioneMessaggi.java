package control;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Medico;
import bean.Messaggio;
import bean.Paziente;
import model.MessaggioModel;


/**
 * Servlet implementation class GestioneMessaggio
 */
@WebServlet("/GestioneMessaggi")
@MultipartConfig
public class GestioneMessaggi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneMessaggi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Metodo che prende gli autori e i destinatari del messaggio e salva quest'ultimo sul Database
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void inviaMessaggio(HttpServletRequest request, HttpSession session) throws IOException, ServletException {
	
	Medico medico = null;
	Paziente paziente = null;
	
	Messaggio messaggio;
	
	medico = (Medico) session.getAttribute("medico");
	paziente = (Paziente) session.getAttribute("paziente");
	
	if (paziente != null && medico == null){
		
		String CFMittente=paziente.getCodiceFiscale();
		String CFDestinatari =request.getParameter("cfdestinataro");
		ArrayList<String> elencoCFDestinatari = new ArrayList<String>();
		elencoCFDestinatari.add(CFDestinatari);
		String oggetto =request.getParameter("oggetto");
		String testo = request.getParameter("testo");
		String allegato = request.getParameter("allegato");
		LocalTime ora = LocalTime.now();
		LocalDate date = LocalDate.now();
		
		Part filePart = request.getPart("file"); 
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
	    InputStream fileContent = filePart.getInputStream();
		
	    messaggio=new Messaggio(CFMittente,elencoCFDestinatari,oggetto, testo,allegato,ora,date);
		MessaggioModel.addMessaggio(messaggio);
		//observer per i medici
	}	
	else if(paziente == null && medico != null) {
		String CFMittente=medico.getCodiceFiscale();
		String CFDestinatari =request.getParameter("cfdestinataro");
		ArrayList<String> elencoCFDestinatari = new ArrayList<String>();
		elencoCFDestinatari.add(CFDestinatari);
		String oggetto =request.getParameter("oggetto");
		String testo = request.getParameter("testo");
		String allegato = request.getParameter("allegato");
		LocalTime ora = LocalTime.now();
		LocalDate date = LocalDate.now();
		
		Part filePart = request.getPart("file"); 
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
	    InputStream fileContent = filePart.getInputStream();
		messaggio=new Messaggio(CFMittente,elencoCFDestinatari,oggetto, testo,allegato,ora,date);
		MessaggioModel.addMessaggio(messaggio);
		//observer per i pazienti
	}
	else {
		//TODO messaggio di errore
		System.out.println("Utente deve esssere loggato");
	}
	}
	
	/**
	 * Metodo che prende il messaggio e lo salva nella richiesta
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 */
	private void visualizzaMessaggio(HttpServletRequest request) {
		
	}

}
