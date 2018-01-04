import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashage {

	//Permet de générer une chaine de caractères correspondant au Hash d'un fichier digéré en utilisant SHA512

	public static BigInteger getHashFromSHA512(String message){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512"); //instanciation de l'algorithme SHA-512
			md.update(message.getBytes());
			BigInteger hash = new BigInteger(md.digest());
			System.out.println(hash.toString());

			byte[] byteData = hash.toByteArray();

			return hash; //retourne le Hash du message sous la forme d'un BigInteger pour des raisons pratiques (utilisé dans CramerShoup)

		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}


	//Genere le hash d'un fichier en entrée, puis écrit le hash dans le fichier hash_sha512.txt du répertoire courant

	public static void generationHashSHA512fromFileBIformat(){
		String messageBrut = Utilitaires.Lecture();
		BigInteger hash = getHashFromSHA512(messageBrut);

		Utilitaires.Ecriture(
			hash.toString()+"\n",
			"./hash_sha512.txt"
		);
	}

	//Permet de comparer le Hash généré d'un fichier, par rapport à un hash enregistré dans un fichier
	//Et renvoie dans le terminal si les deux Hash sont les mêmes

	public static void verificationHashSHA512fromFileBIformat(){
		String messageBrut = Utilitaires.Lecture();
		BigInteger hash = getHashFromSHA512(messageBrut);

		BigInteger hashTeste = new BigInteger(Utilitaires.Lecture().split("\n")[0]);

		if(hash.equals(hashTeste))
			System.out.println("Hash valide");
		else
			System.out.println("Hash non valide");

	}

	//Utilisé pour des besoins de tests

	public static void main(String[] args) {
		generationHashSHA512fromFileBIformat();
		verificationHashSHA512fromFileBIformat();
	}

}
