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

@WebFilter(urlPatterns = { "/messaggio", "/parametri", "/piano", "/comunicazione", "/GestioneAmministratore", 
		"/GestioneAnnunci", "/GestioneMedico", "/GestioneRegistrazione", 
		"/inserimentoMessaggioView.jsp", "/annuncioView.jsp", "/inserimentoAnnuncioView.jsp", "/inserimentoParametriView.jsp",
		"/listaAnnunciView.jsp", "/listaMediciView.jsp", "/listaMessaggiView.jsp", "/listaPazientiView.jsp", "/messaggioView.jsp",
		"/ModificaAccountMedicoView.jsp", "/ModificaAccountPazienteView.jsp", "/monitoraggioParametriView.jsp", "/profilo.jsp",
		"/registraMedico.jsp", "/registraPazienteMedico.jsp", "/visualizzaPianoTerapeutico.jsp"})
public class ErrorPageFilter implements Filter {
	
    public ErrorPageFilter() {}

	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
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

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
