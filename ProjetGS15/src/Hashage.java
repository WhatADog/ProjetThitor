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

	public static void generationHashSHA512fromFileBIformat(){
		String messageBrut = Utilitaires.Lecture();
		BigInteger hash = getHashFromSHA512(messageBrut);

		Utilitaires.Ecriture(
			hash.toString()+"\n",
			"./hash_sha512.txt"
		);
	}

	public static void verificationHashSHA512fromFileBIformat(){
		String messageBrut = Utilitaires.Lecture();
		BigInteger hash = getHashFromSHA512(messageBrut);

		BigInteger hashTeste = new BigInteger(Utilitaires.Lecture().split("\n")[0]);

		if(hash.equals(hashTeste))
			System.out.println("Hash valide");
		else
			System.out.println("Hash non valide");

	}

	public static void main(String[] args) {
		generationHashSHA512fromFileBIformat();
		verificationHashSHA512fromFileBIformat();
	}

}
