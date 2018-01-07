import java.util.InputMismatchException;
import java.util.Scanner;
public class VcesTrueRSA {

	public static void main (String[] args){
		int iChoixUser = 0;
		Scanner scan = new Scanner( System.in );		
		while(iChoixUser != 7){
			System.out.println("Que souhaitez vous faire ?\t");
			System.out.println("->1<- Chiffrement symétrique ThreeFish\t");
			System.out.println("->2<- Chiffrement de Cramer-Shoup\t");
			System.out.println("->3<- Hashage d'un message\t");
			System.out.println("->4<- Déchiffrement de Cramer-Shoup\t");
			System.out.println("->5<- Vérification d'un hash\t");
			System.out.println("->6<- Génération de clés pour Cramer-Shoup\t");
			System.out.println("->7<- Fin du programme");
			iChoixUser = scan.nextInt();
			switch (iChoixUser) {
			case 1: System.out.println("Appel de la fonction de Chiffrement symétrique ThreeFish");
			ThreeFish.Initialisation(scan);
			break;
			case 2: System.out.println("Appel de la fonction de Chiffrement Cramer-Shoup \nVeuillez selectionnez le message à  chiffrer");
			CramerShoup.chiffrementCramerShoup();
			break;
			case 3: System.out.println("Appel de la fonction Hashage d'un message \nVeuillez sélectionner le fichier à  Hasher");
			Hashage.generationHashSHA512fromFileBIformat();
			break;
			case 4: System.out.println("Appel de la fonction de déchiffrement de Cramer-Shoup \nVeuillez selectionnez le fichier de clé privÃ©e, puis le message Ã  dÃ©chiffrer");
			CramerShoup.dechiffrementCramerShoup();
			break;
			case 5: System.out.println("Appel de la fonction de vérification d'un hash \nVeuillez selectioner le fichier à  vérifier, et le fichier de Hash");
			Hashage.verificationHashSHA512fromFileBIformat();
			break;
			case 6: System.out.println("Appel de la fonction de génération de clé pour Cramer-Shoup");
			CramerShoup.generationClePubliquePrivee();
			break;
			case 7: System.out.println("Fin du programme, bonne journée");
			break;			
			default: System.out.println("Entrée invalide veuillez recommencer !");
			break;
			}
		}
		
		scan.close();

	}

}
