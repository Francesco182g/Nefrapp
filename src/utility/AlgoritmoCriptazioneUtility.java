package utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
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

	public static String codificaFile(InputStream file) throws UnsupportedEncodingException {
		String result = null;
		byte[] outputBytes = null;
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] targetArray = new byte[file.available()];

			while ((nRead = file.read(targetArray, 0, targetArray.length)) != -1) {
				buffer.write(targetArray, 0, nRead);
			}

			SecretKeySpec sks = new SecretKeySpec("deveesseresedici".getBytes(), "AES");
			SecretKey key = sks;

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			outputBytes = cipher.doFinal(targetArray);
			
			result = new String(Base64.encodeBase64(outputBytes), "UTF-8");

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String decodificaFile(String file) {
		byte[] outputBytes = null;
		String result = null;
		try {
			byte[] inputBytes = Base64.decodeBase64(file);
			SecretKeySpec sks = new SecretKeySpec("deveesseresedici".getBytes(), "AES");
			SecretKey key = sks;

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			outputBytes = cipher.doFinal(inputBytes);

			result = new String(Base64.encodeBase64(outputBytes), "UTF-8");

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
