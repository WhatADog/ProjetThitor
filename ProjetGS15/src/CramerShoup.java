
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class CramerShoup {

	private static int bitNumber = 128;

	public CramerShoup(){}

	public static ArrayList<Long> genElementGenerateurSurNonFriable(BigInteger bi){
		BigInteger ordreRecherche = new BigInteger(bi.toByteArray());
		ordreRecherche = ordreRecherche.subtract(BigInteger.valueOf(1));
		BigInteger secondFacteur = new BigInteger(ordreRecherche.toByteArray());
		secondFacteur = secondFacteur.divide(BigInteger.valueOf(2));

		BigInteger test;
		ArrayList<Long> result = new ArrayList<Long>();

		for (long i = 2 ; i <= 101 ; i++) {
			test = BigInteger.valueOf(i);
			if(test.modPow(secondFacteur, bi).compareTo(BigInteger.ONE) != 0 && test.modPow(BigInteger.valueOf(2), bi).compareTo(BigInteger.ONE) != 0){
				result.add(i);
			}
		}

		return result;
	}

	public static void generationClePubliquePrivee(){
		Random rnd = new Random(); //Creation generateur de nombres pseudo alétoire

		BigInteger p = new BigInteger(bitNumber*2, 100, rnd); //p est créé 2 fois plus grand pour être de manière sur plus grand que toutes les autres variables
		BigInteger deriv = new BigInteger(p.toByteArray()); //deriv permet de vérifier que p est non friable
		deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
		boolean friable = true;

		//ici, on va tester si p est friable, et s'il l'est, alors on regénère jusqu'à  ce qu'il soit non friable
		if (deriv.isProbablePrime(100)) {
			friable = false;
		}
		else{
			while(friable){
				p = new BigInteger(bitNumber*2, 100, rnd);
				deriv = new BigInteger(p.toByteArray());
				deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
				if(deriv.isProbablePrime(100)){
					friable = false;
				}
			}
		}

		System.out.println("p = "+p.toString());

		//Ici on va générer les autres éléments pour nos clés privée et publique
		ArrayList<Long> premElemGenerateur = genElementGenerateurSurNonFriable(p);

		BigInteger a1 = new BigInteger(premElemGenerateur.get(premElemGenerateur.size()-1)+"");
		BigInteger a2 = new BigInteger(premElemGenerateur.get(premElemGenerateur.size()-2)+"");

		BigInteger x1 = new BigInteger(bitNumber, rnd);
		BigInteger x2 = new BigInteger(bitNumber, rnd);
		BigInteger y1 = new BigInteger(bitNumber, rnd);
		BigInteger y2 = new BigInteger(bitNumber, rnd);
		BigInteger w = new BigInteger(bitNumber, rnd);

		BigInteger majX = a1.modPow(x1, p).multiply(a2.modPow(x2, p)).mod(p);
		BigInteger majY = a1.modPow(y1, p).multiply(a2.modPow(y2, p)).mod(p);
		BigInteger majW = a1.modPow(w, p);

		System.out.println("a1 = "+a1.toString());
		System.out.println("a2 = "+a2.toString());
		System.out.println("X = "+majX.toString());
		System.out.println("Y = "+majY.toString());
		System.out.println("W = "+majW.toString());

		//On écrit ici le fichier de clé public (key.pub dans le répertoire courant), puis le fichier de clé privée ()

		Utilitaires.Ecriture(
		p.toString()+" "+a1.toString()+" "+a2.toString()+" "+majX.toString()+" "+majY.toString()+" "+majW.toString()+" ",
		"./key.pub"
		);

		Utilitaires.Ecriture(
		p.toString()+" "+a1.toString()+" "+a2.toString()+" "+x1.toString()+" "+x2.toString()+" "+y1.toString()+" "+y2.toString()+" "+w.toString()+" ",
		"./key.prv"
		);
	}

	public static void chiffrementCramerShoup(){
		Random rnd = new Random(); //Creation generateur de nombres pseudo alétoire

		//on récupère ici les différentes variables de clé publique
		System.out.println("Sélectionnez le fichier de clés publiques !");
		String[] variables = Utilitaires.Lecture().split(" ");
		BigInteger p = new BigInteger(variables[0]);
		BigInteger a1 = new BigInteger(variables[1]);
		BigInteger a2 = new BigInteger(variables[2]);
		BigInteger majX = new BigInteger(variables[3]);
		BigInteger majY = new BigInteger(variables[4]);
		BigInteger majW = new BigInteger(variables[5]);

		//on récupère ici le message à  chiffrer
		System.out.println("Sélectionnez le fichier à chiffrer");
		String messageBrut = Utilitaires.Lecture();
		byte[] messageClair = messageBrut.getBytes();

		System.out.println(messageBrut);
		for (int i = 0 ; i<messageClair.length ; i++) {
			System.out.println(messageBrut.charAt(i)+" ["+messageClair[i]+"]");
		}

		BigInteger messageChiffrable = new BigInteger(messageClair);

		BigInteger b = new BigInteger(bitNumber, rnd);
		BigInteger majB1 = a1.modPow(b, p);
		BigInteger majB2 = a2.modPow(b, p);
		BigInteger messageChiffre = majW.modPow(b, p).multiply(messageChiffrable).mod(p);

		System.out.println("p = "+p.toString());
		System.out.println("mes clair = "+messageChiffrable.toString());
		System.out.println("m chiffre = "+messageChiffre.toString());

		//Génération des variables de vérification du chiffré
		BigInteger beta = Hashage.getHashFromSHA512(majB1.toString()+majB2.toString()+messageChiffre.toString());
		BigInteger v_verif = majX.modPow(b, p).multiply(majY.modPow(b.multiply(beta), p)).mod(p);

		Utilitaires.Ecriture(
			majB1.toString()+" "+majB2.toString()+" "+messageChiffre.toString()+" "+v_verif.toString()+" ",
			"messageChiffreCS.txt"
		);
	}

	public static void dechiffrementCramerShoup(){
		//On récupère ici les différentes variables qui composent la clé privée
		System.out.println("Sélectionnez le fichier de clés privées !");
		String[] variables = Utilitaires.Lecture().split(" ");
		BigInteger p = new BigInteger(variables[0]);
		BigInteger a1 = new BigInteger(variables[1]);
		BigInteger a2 = new BigInteger(variables[2]);
		BigInteger x1 = new BigInteger(variables[3]);
		BigInteger x2 = new BigInteger(variables[4]);
		BigInteger y1 = new BigInteger(variables[5]);
		BigInteger y2 = new BigInteger(variables[6]);
		BigInteger w = new BigInteger(variables[7]);
		
		//On récupère ici les différentes variables qui composent notre message chiffré
		System.out.println("Sélectionnez le fichier à déchiffrer!");
		variables = Utilitaires.Lecture().split(" ");
		BigInteger majB1 = new BigInteger(variables[0]);
		BigInteger majB2 = new BigInteger(variables[1]);
		BigInteger messageChiffre = new BigInteger(variables[2]);
		BigInteger v_verif = new BigInteger(variables[3]);

		// On recalcule ici le hash pour ensuite vérifier l'intégrité du message chiffré et ensuite le déchiffrer s'il n'a pas été altéré
		BigInteger betaPrime = Hashage.getHashFromSHA512(majB1.toString()+majB2.toString()+messageChiffre.toString());
		BigInteger v_verifPrime = majB1.modPow(x1, p).multiply(majB2.modPow(x2, p)).multiply(majB1.modPow(y1, p).modPow(betaPrime, p)).mod(p).multiply(majB2.modPow(y2, p).modPow(betaPrime, p)).mod(p);

		if(v_verif.equals(v_verifPrime)){
			System.out.println("Verif juste");
			BigInteger messageDechiffre = new BigInteger(messageChiffre.toByteArray());
			BigInteger invPower = p.subtract(BigInteger.ONE).subtract(w);
			BigInteger invB1poww = majB1.modPow(invPower, p);
			messageDechiffre = messageDechiffre.multiply(invB1poww).mod(p);
			
			//On affiche le message déchiffré dans l'invité de commandes
			System.out.println("m dechiffre = "+messageDechiffre.toString());
	
			byte[] messageDechiffreByte = messageDechiffre.toByteArray();
			for (byte b :messageDechiffreByte ) {
				System.out.print((char)b);
			}
		}
		else {
			System.out.println("Verif fausse");
		}

		
		System.out.println("\nFin message");
	}
	//Utilisé pour des besoins de tests
	public static void main(String[] args) {
		generationClePubliquePrivee();
		chiffrementCramerShoup();
		dechiffrementCramerShoup();
	}
}
