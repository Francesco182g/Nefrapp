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



/**Servlet filter che effettua il redirect ad una pagina di errore nel caso in cui un paziente
 * cercasse di fare accesso a una risorsa a lui non disponibile
 * 
 * @author nico
 */
@WebFilter(urlPatterns = { 
		"/inserimentoAnnuncioView.jsp", "/listaPazientiView.jsp", "/ModificaAccountMedicoView.jsp",
		"/registraMedico.jsp", "/registraPazienteMedico.jsp"})
//aggiornare i jsp e le servlet in questa annotazione in caso servisse garantire/negare l'accesso ai pazienti
public class FiltroPaziente implements Filter {
	
    public FiltroPaziente() {}
	RequestDispatcher dispatcher;

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		
		if (session.getAttribute("accessDone") != null && 
			session.getAttribute("isMedico") == null && 
			session.getAttribute("isPaziente") != null && 
			session.getAttribute("isAmministratore") == null &&
			session.getAttribute("utente") != null) {
			
			req.setAttribute("notifica", "Non hai il permesso di accedere a questa pagina! ");
			res.sendRedirect("./paginaErrore.jsp");
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
