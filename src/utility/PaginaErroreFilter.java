package utility;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



/**Servlet filter che effettua il redirect ad una pagina di errore nel caso in cui un utente non registrato
 * cercasse di fare accesso a una risorsa protetta
 * 
 * @author nico
 */
@WebFilter(urlPatterns = { "/messaggio", "/parametri", "/piano", "/comunicazione", "/GestioneAmministratore", 
		"/GestioneAnnunci", "/GestioneMedico", "/GestioneRegistrazione", 
		"/inserimentoMessaggioView.jsp", "/annuncioView.jsp", "/inserimentoAnnuncioView.jsp", "/inserimentoParametriView.jsp",
		"/listaAnnunciView.jsp", "/listaMediciView.jsp", "/listaMessaggiView.jsp", "/listaPazientiView.jsp", "/messaggioView.jsp",
		"/ModificaAccountMedicoView.jsp", "/ModificaAccountPazienteView.jsp", "/monitoraggioParametriView.jsp", "/profilo.jsp",
		"/registraMedico.jsp", "/registraPazienteMedico.jsp", "/visualizzaPianoTerapeutico.jsp"})
//aggiornare i jsp e le servlet in questa annotazione in caso servisse garantire/negare l'accesso a visitatori non loggati
public class PaginaErroreFilter implements Filter {
	
    public PaginaErroreFilter() {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		RequestDispatcher dispatcher;
		
		if (session.getAttribute("accessDone") == null ||
			(session.getAttribute("isPaziente") == null && session.getAttribute("isMedico") == null && session.getAttribute("isAmministratore")==null) ||
			session.getAttribute("utente") == null) {
			
			request.setAttribute("notifica", "Non hai il permesso di accedere a questa pagina! ");
			dispatcher = request.getRequestDispatcher("/paginaErrore.jsp");
			dispatcher.forward(request, response);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
