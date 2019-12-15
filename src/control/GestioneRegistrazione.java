package control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

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
import utility.AlgoritmoCriptazioneUtility;

/**
 * Servlet implementation class RegistrazioneMedico
 */
@WebServlet("/GestioneRegistrazione")
public class GestioneRegistrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		String codiceFiscale= request.getParameter("codiceFiscale");
		String nome=request.getParameter("nome");
		String cognome=request.getParameter("cognome");
		String sesso=request.getParameter("sesso");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String flag=request.getParameter("flag");
		
		if(flag.equals("0")) {
			if (validazione(codiceFiscale,nome,cognome,sesso,email,password)) {
				Medico medico=new Medico(sesso,"",null,codiceFiscale,nome,cognome,email);
				MedicoModel med=new MedicoModel();
				password = AlgoritmoCriptazioneUtility.criptaConMD5(password);//serve a criptare la pasword in MD5 prima di registrarla nel db ps.non cancellare il commento quando spostate la classe
				med.addMedico(medico, password);
			} 
		}
		else if(flag.equals("1")) {
			if (validazione(codiceFiscale,nome,cognome,sesso,email,password)) {
				Medico medicoLoggato= (Medico) session.getAttribute("medico");
				ArrayList<String> medici=new ArrayList<String>();
				medici.add(medicoLoggato.getCodiceFiscale());
				String residenza=request.getParameter("residenza");
				String dataDiNascita=request.getParameter("dataDiNascita");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date data=null;
				try {
					data = formatter.parse(dataDiNascita);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Paziente paziente=new Paziente(sesso,codiceFiscale,nome,cognome,email,residenza,data,true,medici);
				PazienteModel paz=new PazienteModel();
				paz.addPaziente(paziente,password);
			}
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
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
