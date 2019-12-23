package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

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
	
	public static String codificaInBase64(InputStream file){
        String encodedfile = null;
        try {
        	
            encodedfile = new String(Base64.encodeBase64(codificaFile("eugenio", file)), "UTF-8");//la chiave deve essere decisa opterei per il CF
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
	private static byte[] codificaFile(String chiave,InputStream file){
		
			try {
				Key secretKey = new SecretKeySpec(chiave.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] inputBytes;
				
				inputBytes = new byte[(int) file.available()];
				file.read(inputBytes);
				System.out.println("file decodificato :"+inputBytes.toString());
				byte[] outputBytes = cipher.doFinal(inputBytes);
				System.out.println("file codificato :"+outputBytes.toString());
				return outputBytes;
			} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
					| IllegalBlockSizeException | IOException e) {
				e.printStackTrace();
			}
			return null;
		
	}
	private static byte[] decodificaFile(String chiave,byte[] file){
		
		try {
			Key secretKey = new SecretKeySpec(chiave.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			System.out.println("file codificato :"+file.toString());
			byte[] outputBytes = cipher.doFinal(file);
			System.out.println("file decodificato :"+outputBytes.toString());
			
			return outputBytes;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	
}
	public static void decodificaDaBase64(File file, String string) 
	{
		byte[] bytes = Base64.decodeBase64(string);
		byte[] decodifica = decodificaFile("eugenio", bytes);
		FileOutputStream fileOuputStream = null;
		try {
			fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(decodifica);
		} catch (IOException e) {

			e.printStackTrace();
		}
		 
	}

}
