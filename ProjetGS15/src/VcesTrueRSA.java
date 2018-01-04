import java.util.InputMismatchException;
import java.util.Scanner;
public class VcesTrueRSA {

	// Fonction pour r�cuperer un entier
	public static int recupererInt() {
        int userValue = 0;
        boolean erreur = false;
        Scanner scan = new Scanner(System.in);

        //Bloc d'essai (pour s'assurer qu'on r�cup�re bien un entier
        do {
            try {
                userValue = scan.nextInt();
                erreur = false;
            } catch (InputMismatchException e) {
                System.out.println("\nCe n'est pas une valeur prise en charge, veuillez r�essayer.");
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
		System.out.println("->1<- Chiffrement sym�trique ThreeFish\t");
		System.out.println("->2<- Chiffrement de Cramer-Shoup\t");
		System.out.println("->3<- Hashage d'un message\t");
		System.out.println("->4<- D�chiffrement sym�trique ThreeFish\t");
		System.out.println("->5<- D�chiffrement de Cramer-Shoup\t");
		System.out.println("->6<- V�rification d'un hash\t");
		System.out.println("->7<- Génération de clés pour Cramer-Shoup\t");

		int iChoixUser = recupererInt();
		switch (iChoixUser) {
		case 1: System.out.println("Appel de la fonction de Chiffrement sym�trique ThreeFish");
		break;
		case 2: System.out.println("Appel de la fonction de Chiffrement Cramer-Shoup \nVeuillez selectionnez le fichier de clé publique, puis le message à chiffrer");
		CramerShoup.chiffrementCramerShoup();
		break;
		case 3: System.out.println("Appel de la fonction Hashage d'un message \nVeuillez sélectionner le fichier à Hasher");
		Hashage.generationHashSHA512fromFileBIformat();
		break;
		case 4: System.out.println("Appel de la fonction de d�chiffrement sym�trique ThreeFish");
		break;
		case 5: System.out.println("Appel de la fonction de d�chiffrement de Cramer-Shoup \nVeuillez selectionnez le fichier de clé privée, puis le message à déchiffrer");
		CramerShoup.dechiffrementCramerShoup();
		break;
		case 6: System.out.println("Appel de la fonction de v�rification d'un hash \nVeuillez selectioner le fichier à vérifier, et le fichier de Hash");
		Hashage.verificationHashSHA512fromFileBIformat();
		break;
		case 7: System.out.println("Appel de la fonction de génération de clé pour Cramer-Shoup");
		CramerShoup.generationClePubliquePrivee();
		break;
		default: System.out.println("Entr�e invalide veuillez recommencer !");
		break;
		}
		user_input.close();

	}

}
