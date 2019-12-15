package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AlgoritmoCriptazioneUtility {
	private static MessageDigest md;
	
	/**
	 * criptaConMD5 è una funzione che permette di criptare le password in MD5
	 * @param password indica la password che deve essere criptata
	 * @return la password criptata se la criptazione è andata a buon fine altrimenti restituisce null
	 */
	public static String criptaConMD5(String password){
		try {
		        md = MessageDigest.getInstance("MD5");
		        byte[] passwordBytes = password.getBytes();
		        md.reset();
		        byte[] digested = md.digest(passwordBytes);
		        StringBuffer sb = new StringBuffer();
		        for(int i=0;i<digested.length;i++)
		        {
		            sb.append(Integer.toHexString(0xff & digested[i]));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException ex) {
		        ex.printStackTrace();
		    }
		        return null;
		   }
}
