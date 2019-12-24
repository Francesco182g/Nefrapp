package utility;

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
	 * 
	 * @param password indica la password che deve essere criptata
	 * @return la password criptata se la criptazione è andata a buon fine
	 *         altrimenti restituisce null
	 */
	public static String criptaConMD5(String password) {
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passwordBytes = password.getBytes();
			md.reset();
			byte[] digested = md.digest(passwordBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String codificaInBase64(InputStream file) throws IOException {
		String encodedfile = null;
		try {
			byte[] targetArray = new byte[file.available()];
			file.read(targetArray);
			encodedfile = new String(Base64.encodeBase64(targetArray), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}

	public static String codificaFile(String chiave, InputStream file) {
		String base64Result = null;
		try {
			Key secretKey = new SecretKeySpec(chiave.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] inputBytes;

			inputBytes = new byte[(int) file.available()];
			file.read(inputBytes);
			System.out.println("file decodificato :" + inputBytes.toString());
			byte[] outputBytes = cipher.doFinal(inputBytes);
			System.out.println("file codificato :" + outputBytes.toString());
			base64Result = new String(Base64.encodeBase64(outputBytes), "UTF-8");
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
		return base64Result;

	}

	public static String decodificaFile(String chiave, String file) {
		byte[] outputBytes = null;
		try {
			outputBytes = Base64.decodeBase64(file);
			Key secretKey = new SecretKeySpec(chiave.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			System.out.println("file codificato :" + file.toString());
			outputBytes = file.getBytes();
			outputBytes = cipher.doFinal(outputBytes);
			System.out.println("file decodificato :" + outputBytes.toString());

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		return outputBytes.toString();

	}

}
