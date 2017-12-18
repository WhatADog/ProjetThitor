
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class CramerShoup {

	private static ArrayList<Long> nb_premiers = new ArrayList<Long>();

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

		BigInteger p = new BigInteger(256, 100, rnd);
		BigInteger deriv = new BigInteger(p.toByteArray());
		deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
		boolean friable = true;

		if (deriv.isProbablePrime(100)) {
			friable = false;
		}
		else{
			while(friable){
				p = new BigInteger(256, 100, rnd);
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

		BigInteger x1 = new BigInteger(128, rnd);
		BigInteger x2 = new BigInteger(128, rnd);
		BigInteger y1 = new BigInteger(128, rnd);
		BigInteger y2 = new BigInteger(128, rnd);
		BigInteger w = new BigInteger(128, rnd);

		BigInteger majX = a1.modPow(x1, p).multiply(a2.modPow(x2, p)).mod(p);
		BigInteger majY = a1.modPow(y1, p).multiply(a2.modPow(y2, p)).mod(p);
		BigInteger majW = a1.modPow(w, p);

		System.out.println("a1 = "+a1.toString());
		System.out.println("a2 = "+a2.toString());
		System.out.println("X = "+majX.toString());
		System.out.println("Y = "+majY.toString());
		System.out.println("W = "+majW.toString());
	}

}
