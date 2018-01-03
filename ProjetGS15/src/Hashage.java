import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashage {

	public static BigInteger getHashFromSHA512(String message){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(message.getBytes());
			BigInteger hash = new BigInteger(md.digest());
			System.out.println(hash.toString());

			byte[] byteData = hash.toByteArray();

			StringBuilder sb = new StringBuilder(byteData.length * 2);
   		for(byte b: byteData)
      	sb.append(String.format("%02x", b));

      System.out.println("Hex format : " + sb.toString());

			return hash;

		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String message = Utilitaires.Lecture();
		getHashFromSHA512(message);
	}

}
