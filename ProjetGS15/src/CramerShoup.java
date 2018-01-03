
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class CramerShoup {

	private static ArrayList<Long> nb_premiers = new ArrayList<Long>();
	private static int bitNumber = 128;

	public CramerShoup(){

	}

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

	public static void main(String[] args) {
		Random rnd = new Random();

		BigInteger p = new BigInteger(bitNumber*2, 100, rnd);
		BigInteger deriv = new BigInteger(p.toByteArray());
		deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
		boolean friable = true;

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

		String messageBrut = Utilitaires.Lecture();
		byte[] messageClair = messageBrut.getBytes();

		System.out.println(messageBrut);
		for (int i = 0 ; i<messageClair.length ; i++) {
			System.out.println(messageBrut.charAt(i)+" ["+messageClair[i]+"]");
		}

		BigInteger messageChiffrable = new BigInteger(messageClair);

		//Chiffrement
		BigInteger b = new BigInteger(bitNumber, rnd);
		BigInteger majB1 = a1.modPow(b, p);
		BigInteger majB2 = a2.modPow(b, p);
		BigInteger messageChiffre = majW.modPow(b, p).multiply(messageChiffrable).mod(p);

		System.out.println("p = "+p.toString());
		System.out.println("mes clair = "+messageChiffrable.toString());
		System.out.println("m chiffre = "+messageChiffre.toString());

		//verif
		BigInteger beta = Hashage.getHashFromSHA512(majB1.toString()+majB2.toString()+messageChiffre.toString());
		BigInteger v_verif = majX.modPow(b, p).multiply(majY.modPow(b.multiply(beta), p)).mod(p);

		//Dechiffrement
		BigInteger betaPrime = Hashage.getHashFromSHA512(majB1.toString()+majB2.toString()+messageChiffre.toString());
		BigInteger v_verifPrime = majB1.modPow(x1, p).multiply(majB2.modPow(x2, p)).multiply(majB1.modPow(y1, p).modPow(betaPrime, p)).mod(p).multiply(majB2.modPow(y2, p).modPow(betaPrime, p)).mod(p);

		if(v_verif.equals(v_verifPrime))
			System.out.println("Verif juste");
		else
			System.out.println("Verif fausse");

		BigInteger messageDechiffre = new BigInteger(messageChiffre.toByteArray());
		BigInteger invPower = p.subtract(BigInteger.ONE).subtract(w);
		BigInteger invB1poww = majB1.modPow(invPower, p);
		messageDechiffre = messageDechiffre.multiply(invB1poww).mod(p);

		System.out.println("m dechiffre = "+messageDechiffre.toString());
	}

}
