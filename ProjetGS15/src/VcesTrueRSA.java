import java.util.InputMismatchException;
import java.util.Scanner;
public class VcesTrueRSA {
	
	// Fonction pour récupérer un entier
	public static int recupererInt() {
        int userValue = 0;
        boolean erreur = false;
        Scanner scan = new Scanner(System.in);
        
        //Bloc d'essai (pour s'assurer qu'on récupère bien un entier
        do {
            try {
                userValue = scan.nextInt();
                scan.next();
                erreur = false;
            } catch (InputMismatchException e) {
                System.out.println("\nCe n'est pas une valeur prise en charge, veuillez réessayer.");
                erreur = true;
                scan.next();
            }
        } while (erreur);
        scan.close();
        return userValue;
    }
	
	public static void main (String[] args){
		Scanner user_input = new Scanner( System.in );
		System.out.println("Selectionner votre fonction de chiffrement : \t");
		System.out.println("->1<- Chiffrement symétrique ThreeFish\t");
		System.out.println("->2<- Chiffrement de Cramer-Shoup\t");
		System.out.println("->3<- Hashage d'un message\t");
		System.out.println("->4<- Déchiffrement symétrique ThreeFish\t");
		System.out.println("->5<- Déchiffrement de Cramer-Shoup\t");
		System.out.println("->6<- Vérification d'un hash\t");
		
		int iChoixUser = recupererInt();
		switch (iChoixUser) {
		case 1: System.out.println("Appel de la fonction de Chiffrement symétrique ThreeFish");
		break;
		case 2: System.out.println("Appel de la fonction de Chiffrement Cramer-Shoup");
		break;
		case 3: System.out.println("Appel de la fonction Hashage d'un message");
		break;
		case 4: System.out.println("Appel de la fonction de déchiffrement symétrique ThreeFish");
		break;
		case 5: System.out.println("Appel de la fonction de déchiffrement de Cramer-Shoup");
		break;
		case 6: System.out.println("Appel de la fonction de vérification d'un hash");
		break;
		default: System.out.println("Entrée invalide veuillez recommencer !");
		break;
		}
		user_input.close();
		
	}
	
}
