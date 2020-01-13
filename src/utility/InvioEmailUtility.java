package utility;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class InvioEmailUtility {
	
	public static final String MITTENTE = "nefrappteam@gmail.com";
	public static final String PASSWORD = "ForseIlProgettoNonFallisce1289!?#";
	
	public static void inviaEmail(String destinatario) throws Exception{
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		Session sessione = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MITTENTE, PASSWORD);
			}
		});
		
		Message messaggio = preparaMessaggio(sessione, MITTENTE, destinatario);
		
		Transport.send(messaggio);
	}
	
	private static Message preparaMessaggio(Session sessione, String mittente, String destinatario) {
		try {
			Message messaggio = new MimeMessage(sessione);
			
			messaggio.setFrom(new InternetAddress(mittente));
			messaggio.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			messaggio.setSubject("Richiesta reset password");
			messaggio.setText("Salve, è stato richiesto il reset della password. \nVada al seguente link:\nhttp://localhost:8080/Nefrapp/resetPasswordView.jsp");
			
			return messaggio;
		}catch(Exception e) {
			Logger.getLogger(InvioEmailUtility.class.getName()).log(Level.SEVERE, null, e);
		}
		
		return null;
	}
}