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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**Servlet filter che effettua il redirect ad una pagina di errore nel caso in cui un utente non registrato
 * cercasse di fare accesso a una risorsa protetta
 * 
 * @author nico
 */
@WebFilter(urlPatterns = { "/messaggio", "/parametri", "/piano", "/comunicazione", "/GestioneAmministratore", 
		"/GestioneAnnunci", "/annuncio", "/GestioneMedico", "/GestioneRegistrazione", 
		"/inserimentoMessaggioView.jsp", "/annuncioView.jsp", "/inserimentoAnnuncioView.jsp", "/inserimentoParametriView.jsp",
		"/listaAnnunciView.jsp", "/listaMediciView.jsp", "/listaMessaggiView.jsp", "/listaPazientiView.jsp", "/messaggioView.jsp",
		"/ModificaAccountMedicoView.jsp", "/ModificaAccountPazienteView.jsp", "/monitoraggioParametriView.jsp", "/profilo.jsp",
		"/registraMedico.jsp", "/registraPazienteMedico.jsp", "/visualizzaPianoTerapeutico.jsp"})
//aggiornare i jsp e le servlet in questa annotazione in caso servisse garantire/negare l'accesso a visitatori non loggati
public class PaginaErroreFilter implements Filter {
	
    public PaginaErroreFilter() {}
	RequestDispatcher dispatcher;

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		
		if (session.getAttribute("accessDone") == null ||
			(session.getAttribute("isPaziente") == null && session.getAttribute("isMedico") == null && session.getAttribute("isAmministratore")==null) ||
			session.getAttribute("utente") == null) {
			
			req.setAttribute("notifica", "Non hai il permesso di accedere a questa pagina! ");
			res.sendRedirect("./paginaErrore.jsp");
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
