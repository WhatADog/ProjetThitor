import java.util.ArrayList;

public class CramerShoup {

	private static ArrayList<Integer> nb_premiers = new ArrayList<Integer>();

	public CramerShoup(){

	}

	public static ArrayList<Integer> cribleErathostene(int max){
		nb_premiers.clear();
		for (int i = 2 ; i*i <= max ; i++) {
			nb_premiers.add(i);
		}

		int j;
		for (int i = 0 ; i < nb_premiers.size() ; i++) {
			j = i+1;
			while(j < nb_premiers.size()) {
				if(nb_premiers.get(j)%nb_premiers.get(i)==0){
					nb_premiers.remove(j);
				}
				else{
					j++;
				}
			}
		}
		/*
		for (int i=0; i<nb_premiers.size(); i++) {
			System.out.println(nb_premiers.get(i));
		}
		*/
		return nb_premiers;

	}

	public static boolean testPrimalite(int n){
		ArrayList<Integer> nb_testes = cribleErathostene(n);

		for (int i = 0 ; i < nb_testes.size() ; i++) {
			if(n%nb_testes.get(i)==0){
				return false;
			}
		}

		return true;
	}

	public static boolean verifElemGenerateur(int n, int a){

		if (a==0) {
			return false;
		}

		int p = n-1;
		ArrayList<Integer> nb_composants = cribleErathostene(p);
		int limit = p / getPlusPetitFacteurPremier(p, nb_composants);

		int a_Generated = a;
		for (int i=1; i<=limit ; i++) {
			System.out.println(a_Generated);
			a_Generated = (a_Generated*a)%n;
			if(a_Generated==1){
				return false;
			}
		}
		return true;
	}

	public static int getPlusPetitFacteurPremier(int p, ArrayList<Integer> facteursPremiers){
		for (int i =0; i<facteursPremiers.size() ; i++) {
			if (p%facteursPremiers.get(i)==0) {
				return facteursPremiers.get(i);
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
