package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.Medico;
import bean.PianoTerapeutico;
import bean.Utente;
import model.PianoTerapeuticoModel;

/**
 * 
 * @author Davide Benedetto Strianese Questa classe è una servlet che si occupa
 *         della gestione del piano terapeutico
 */
@WebServlet("/GestionePianoTerapeutico")
public class GestionePianoTerapeutico extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher;
	final String REGEX_DATA = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";
	final String REGEX_CF = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			String operazione = request.getParameter("operazione");
			String tipo = request.getParameter("tipo");
			
			if (operazione == null) {
				response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
				return;
			}

			if (operazione.equals("visualizza")) {
				visualizzaPiano(request, response, tipo);
				if (!(tipo != null && tipo.equals("asincrona")) && !response.isCommitted()) {
					dispatcher = request.getRequestDispatcher("/visualizzaPianoTerapeutico.jsp");
					dispatcher.forward(request, response);
				}

			}

			else if (operazione.equals("modifica")) {
				modificaPiano(request,response);
				if (!response.isCommitted()) {
					response.sendRedirect(request.getContextPath() + "/GestioneMedico?operazione=VisualizzaPazientiSeguiti");
				}
			}

			else {
				response.sendRedirect("./paginaErrore.jsp?notifica=noOperazione");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("./paginaErrore.jsp?notifica=eccezione");
			return;
		}
		return;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		return;
	}

	/**
	 * Questo metodo richiama dal database il piano terapeutico del paziente che ne
	 * ha richiesto la visualizzazione.
	 * 
	 * @param request
	 * @param response
	 * 
	 * @author Domenico Musone
	 * @throws IOException
	 */
	private void visualizzaPiano(HttpServletRequest request, HttpServletResponse response, String tipo)
			throws IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");

		PianoTerapeutico pianoTerapeutico = null;
		String codiceFiscalePaziente = "";
		// non dovrebbe essere controllato se il paziente è loggato prima ?
		// perchè gli viene passato il CF se tecnicamente lo puoi prendere dall'utente ?
		if (tipo != null && utente != null && tipo.equals("asincrona")) {
			codiceFiscalePaziente = utente.getCodiceFiscale();
		} else {
			codiceFiscalePaziente = request.getParameter("CFPaziente");
		}
		if (codiceFiscalePaziente != null && Pattern.matches(REGEX_CF, codiceFiscalePaziente)) {
			pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(codiceFiscalePaziente);
			if (tipo != null && tipo.equals("asincrona")) {

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				Gson gg = new Gson();
				response.getWriter().write(gg.toJson(pianoTerapeutico));

			} else {
				request.setAttribute("pianoTerapeutico", pianoTerapeutico);
			}

		} else {
			response.sendRedirect("./paginaErrore.jsp?notifica=cfNonValido");
			return;
		}

		if (utente != null) {
			// solo se l'utente è un paziente, la visualizzazione viene settata a false
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				PianoTerapeuticoModel.setVisualizzatoPianoTerapeutico(codiceFiscalePaziente, true);
			}
		}
	}

	/**
	 * 
	 * Questo metodo modifica il piano terapeutico di un paziente
	 * 
	 * @param request richiesta che contiene i parametri da aggiornare
	 * 
	 * @author Antonio Donnarumma
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void modificaPiano(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Medico medico = (Medico) request.getSession().getAttribute("utente");

		if (medico != null) {
			String dataFine = request.getParameter("data");
			String codiceFiscalePaziente = request.getParameter("CFPaziente");
			if (Pattern.matches(REGEX_DATA, dataFine) && Pattern.matches(REGEX_CF, codiceFiscalePaziente)) {
				String diagnosi = request.getParameter("diagnosi");
				String farmaci = request.getParameter("farmaci");
				LocalDate dataFineTerapia = LocalDate.parse(dataFine, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				PianoTerapeuticoModel.updatePianoTerapeutico(new PianoTerapeutico(codiceFiscalePaziente, diagnosi, farmaci, dataFineTerapia));
			} else {
				response.sendRedirect("./paginaErrore.jsp?notifica=datiNonValidi");
				return;
			}
		} else {
			response.sendRedirect("./paginaErrore.jsp?notifica=accessoNegato");
			return;
		}
	}
}
