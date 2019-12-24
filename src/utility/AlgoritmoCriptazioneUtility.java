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

			//encodedfile = new String(Base64.encodeBase64(codificaFile("deveesseresedici", file)), "UTF-8");
			// la chiave deve
			// essere decisa
			// opterei per il CF
			
			
			//Eugenio, visto che non hai ancora messo la decodifica nel codice rimetto la versione vecchia per ora
			//Anche perchè come l'avevi lasciata tu manco mi funzionava l'upload di file.
			
			//In ogni caso non mi piace affatto come hai impostato la cosa qua
			//Non stai codificando in base64, quindi non ho capito perchè la tua codifica debba essere mischiata
			//con l'attività di questo metodo. Ci ho messo mezz'ora a capire che stavi facendo e perché non mi
			//funzionavano più cose che avrebbero dovuto funzionare. 
			//Almeno potevi scriverlo da qualche parte cosa hai fatto. 
			//Se i metodi cambiano funzione di colpo senza peraltro manco cambiare di nome 
			//metti i bastoni tra le ruote agli altri due volte. 
			
			//CodificaInBase64 non dovrebbe fare altro che prendere uno stream e trasformarlo in una stringa
			//in base 64. Se dopo con quella stringa vuoi farci altro dai quella stringa in pasto a un altro metodo.
			//Oppure se vuoi fare qualcosa con lo stream prima di renderlo una stringa in base 64,
			//allora lo fai e poi dai lo stream modificato a questo metodo. 
			//Poi se vuoi refactoriamo, cambiamo nome e mettiamo tutto insieme, 
			//basta parlare quando si toccano cose usate altrove, 
			//e nel caso non si riesca a parlare, documentare.
			
			byte[] targetArray = new byte[file.available()];
			file.read(targetArray);
			encodedfile = new String(Base64.encodeBase64(targetArray), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}

	private static byte[] codificaFile(String chiave, InputStream file) {

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
			return outputBytes;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static byte[] decodificaFile(String chiave, byte[] file) {

		try {
			Key secretKey = new SecretKeySpec(chiave.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			System.out.println("file codificato :" + file.toString());
			byte[] outputBytes = cipher.doFinal(file);
			System.out.println("file decodificato :" + outputBytes.toString());

			return outputBytes;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void decodificaDaBase64(File file, String string) {
		byte[] bytes = Base64.decodeBase64(string);
		byte[] decodifica = decodificaFile("deveesseresedici", bytes);
		FileOutputStream fileOuputStream = null;
		try {
			fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(decodifica);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
