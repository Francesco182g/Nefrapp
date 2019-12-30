package utility;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Questa classe è un Filter che forza l'encoding UTF-8 in entrata e uscita dal
 * Container
 * 
 * @author nico
 *
 */
@WebFilter(urlPatterns = { "/messaggio", "/parametri", "/piano", "/comunicazione", "/GestioneAccesso", "/annuncio",
		"/GestioneAmministratore", "/GestioneAnnunci", "/GestioneMedico", "/GestioneRegistrazione" })
public class CharacterSetFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		next.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}