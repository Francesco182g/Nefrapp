package control;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.Medico;
import model.MedicoModel;

/**
 * Servlet implementation class RegistrazioneMedico
 */
@WebServlet("/RegistrazioneMedico")
public class RegistrazioneMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String codiceFiscale= request.getParameter("codiceFiscale");
		String nome=request.getParameter("nome");
		String cognome=request.getParameter("cognome");
		String sesso=request.getParameter("sesso");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		
		
		if (validazione(codiceFiscale,nome,cognome,sesso,email,password)) {
			Medico medico=new Medico(sesso,"",null,codiceFiscale,nome,cognome,email);
			MedicoModel med=new MedicoModel();
			med.addMedico(medico, password);
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("");
		requestDispatcher.forward(request, response);	}
	
	private boolean validazione(String codiceFiscale,String nome, String cognome,String sesso, String email,String password) {
		boolean valido=true;
		String expCodiceFiscale="^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		String expNome="^[A-Z][a-zA-Z ]*$";
		String expCognome="^[A-Z][a-zA-Z ]*$";
		String expSesso="^[MF]$";
		String expEmail="^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$";
		String expPassword="^[a-zA-Z0-9]*$";
		
		if (!Pattern.matches(expCodiceFiscale, codiceFiscale)||codiceFiscale.length()!=16)
			valido=false;
		if (!Pattern.matches(expNome, nome)||nome.length()<2||nome.length()>30)
			valido=false;
		if (!Pattern.matches(expCognome, cognome)||cognome.length()<2||cognome.length()>30)
			valido=false;
		if (!Pattern.matches(expPassword, password)||password.length()<6||password.length()>20)
			valido=false;
		if (!Pattern.matches(expSesso, sesso)||sesso.length()!=1)
			valido=false;
		if (!Pattern.matches(expEmail, email))
			valido=false;
		return valido;
	}

}
