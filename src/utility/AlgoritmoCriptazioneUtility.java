package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;

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
	
	public static String codificaInBase64(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
	
	public static void decodificaDaBase64(File file, String string) 
	{
		byte[] bytes = Base64.decodeBase64(string);
		FileOutputStream fileOuputStream = null;
		try {
			fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(bytes);
		} catch (IOException e) {

			e.printStackTrace();
		}
		 
	}

}
