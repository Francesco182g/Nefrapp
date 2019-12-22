package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Medico;
import bean.Paziente;
import model.MedicoModel;
import model.PazienteModel;

/**
 * Questa classe è una servlet che modella le operazioni comuni delle funzionalità di comunicazione
 * @author nico
 */
@WebServlet("/comunicazione")
public class GestioneComunicazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GestioneComunicazione() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("./dashboard.jsp"); // TODO reindirizzamento
																								// home
				dispatcher.forward(request, response);
				return;
			}
	
			String operazione = request.getParameter("operazione");
	
			if (operazione.equals("caricaDestinatariMessaggio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("./inserimentoMessaggioView.jsp");
				requestDispatcher.forward(request, response);
			}
			
			if (operazione.equals("caricaDestinatariAnnuncio")) {
				caricaDestinatari(request, response);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(""); //TODO pagina annuncio
				requestDispatcher.forward(request, response);
			}

			} catch (Exception e) {
				System.out.println("Errore durante il caricamento della pagina:");
				e.printStackTrace();
			}
		
		return;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Metodo che carica i destinatari ammessi per inviare messaggi e annunci
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare attributi
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void caricaDestinatari(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Medico medico = null;
		Paziente paziente = null;
		HttpSession session = request.getSession();
		medico = (Medico) session.getAttribute("medico");
		paziente = (Paziente) session.getAttribute("paziente");

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

}
