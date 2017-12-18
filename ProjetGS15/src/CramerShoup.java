
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

		BigInteger test = new BigInteger(256, 100, rnd);
		BigInteger deriv = new BigInteger(test.toByteArray());
		deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
		boolean friable = true;

		if (deriv.isProbablePrime(100)) {
			friable = false;
		}
		else{
			while(friable){
				test = new BigInteger(256, 100, rnd);
				deriv = new BigInteger(test.toByteArray());
				deriv = deriv.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
				if(deriv.isProbablePrime(100)){
					friable = false;
				}
			}
		}

		System.out.println(test.toString());
		System.out.println(deriv.toString());

		ArrayList<Long> premElemGenerateur = genElementGenerateurSurNonFriable(test);
		System.out.println(premElemGenerateur.get(premElemGenerateur.size()-1)+" ; "+premElemGenerateur.get(premElemGenerateur.size()-2));
	}

}
