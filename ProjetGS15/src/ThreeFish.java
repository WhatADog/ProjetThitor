
public class ThreeFish {
	
	// Fonction qui va generer la clé
	public static String GenerationCle(int size){
		if (size == 256){
			return "Un texte qui va faire 32 chars !";
		}
		else if(size == 512){
			return "Cette fois on va essayer d'avoir 64chars pour atteindre 512 bits";
		}
		else if(size == 1024){
			return "Aie aie cette fois il faut arriver à atteindre 128 chars pour obtenir une clé de 1024 bits, c'est vraiment difficile par moment.";
		}
		else {
			System.out.println("La taille de la clé demandée est invalide");
		}
		return null;
		
	}
	
	// Fonction qui transforme un string en binaire
	static StringBuilder ChaineToBinaire(String chaine){
	    byte[] bytes = chaine.getBytes();
	    StringBuilder binary = new StringBuilder();
	    for (byte b : bytes) {
	        int val = b;
	        for (int i = 0; i < 8; i++) {
	            binary.append((val & 128) == 0 ? 0 : 1);
	            val <<= 1;
	        }
	        //binary.append(' ');
	    }
	    return binary;
	}
	
	// Fonction qui transforme du binaire en chaine
	static String BinaireTochaine(StringBuilder a2){	     
	    String res="";
        for(int i=1;i<a2.length();i++){
            if(i%8==0){
                char a=(char)Integer.parseInt(a2.substring(i-8,i),2);
                res=res+a;
            }  
        }
        res=res+(char)Integer.parseInt(a2.substring(a2.length()-8,a2.length()),2);
        return res;
    }
	
	// Fonction qui va générer les sous clés en séparant la clé principale en N morceaux et les tweaks
	public static String[] GenerationSousCles(String chaine, int N){
		String[] cle = chaine.split("");
		String[] sousCles = new String[N+1];
		String[] tweaks = new String[3];
		for (int i = 1; i < N +1; i++){
			String tampon = "";
			for (int j = 64*(i-1); j < 64*i; j++){
				tampon += cle[j];
			}
			sousCles[i-1] = tampon;
			// On choisit arbitrairement les deux dernier mots de 64 bits comme étant les tweaks
			if(i == N-1){
				tweaks[0] = tampon;
			}
			else if (i == N){
				tweaks[1] = tampon;
			}
			//System.out.println("tab[" + (i-1)  + "] : "+ tab[i-1] );
		}
		
		// On calcule kN en xorant k1, k2, ...., kN-1 et C
		sousCles[N] = "0001101111010001000110111101101010101001111111000001101000100010";
		for(int i = 0; i < N; i++){
			sousCles[N] = xor(sousCles[i],sousCles[N]);
		}
		
		// On calcule t2 en xorant t0 et t1
		tweaks[2] = xor(tweaks[0], tweaks[1]);
		
		// Affichage des sous clés et des tweaks
		System.out.println("Les sous clés :");
		for (int i = 0; i < sousCles.length; i++){
			System.out.println("tab[" + (i)  + "] : "+ sousCles[i] );
		}
		System.out.println("Les 3 tweaks");
		for (int i = 0; i < tweaks.length; i++){
			System.out.println("tweak[" + (i)  + "] : "+ tweaks[i] );
		}
		
		GenerationClesTournees(sousCles, tweaks);
		return sousCles;		
	}
	
	// Fonction qui va générer toutes les clés de tournée
	public static String[] GenerationClesTournees (String[] sousCles, String[] tweaks){
		// sousCles est notre tableau qui contient les sous clés, sa taille est donc N + 1 actuellement
		int N = sousCles.length-1;
		System.out.println(N);
		// On créer un tableau [20][N] car il y'a N-1 sous clés par tournée et 20 tournée, on est sur le modèle kn(i).
		String[][] clesTournees = new String[20][N];
		
		for (int i = 0; i < 20; i++){
			for(int n = 0; n < N; n++){
				
				if (n == N-3){
					// Dans l'attente de la compréhension de l'additionModulaire on va xorer
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)],tweaks[i%3]);
					// clesTournees[i][n] = xor(sousCles[(i+n)%(N+1)],tweaks[i%3]);
				}
				else if (n == N-2){
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)],tweaks[(i+1)%3]);
					// clesTournees[i][n] = xor(sousCles[(i+n)%(N+1)],tweaks[(i+1)%3]);
				}
				else if (n == N-1){
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)], ChaineToBinaire(Integer.toString(i)).toString());
					// Impossible de xorer ici car les deux elements n'ont pas la meme taille
					//clesTournees[i][n] = "coucou";
				}
				else {
					clesTournees[i][n] = sousCles[(i+n)%(N+1)];
				}
			}
		}
		
		for (int i = 0; i < 20; i++){
			System.out.println("Tournée n°" + i);
			for(int n = 0; n < N; n++){
				System.out.println("Cle["+n+"] : " + clesTournees[i][n]);
			}
		}
		// Message à chiffrer
		String messageAChiffrerChaine = "Un texte qui va faire 32 chars !";
		// On le passe en binaire
		String messageAChiffrer = ChaineToBinaire(messageAChiffrerChaine).toString();
		// On le découpe en blocs de 64 bits
		String[] tabTempo = messageAChiffrer.split("");
		String[] tabAChiffrer = new String[N];
		
		for(int i = 0; i < N; i++){
			String tempo = "";
			for(int j = i*64; j < 64*(i+1); j++){
				tempo += tabTempo[j];
			}
			tabAChiffrer[i] = tempo;
		}
		
		// Boucle qui va gérer les 76 tournées
		for(int i = 0; i < 20; i++){
			for(int j = 0; j <N; j++){
				// On xor le message avec les clés de tournées
				tabAChiffrer[j] = xor(tabAChiffrer[j],clesTournees[i][j]);				
			}
			// On effectue 4 mix + Permute
			for(int k = 0; k < 4; k++){
				Substitution(tabAChiffrer);
				for(int l = 0; l < tabAChiffrer.length; l++){
					Permutation(tabAChiffrer[l]);
				}
			}
		}
		// On remet le tabAChiffrer sous forme de String
		messageAChiffrer = "";
		for (int i = 0; i < tabAChiffrer.length; i++){
			messageAChiffrer += tabAChiffrer[i];
		}
		// On peut convertir en chaine de caractères le message chiffré
		StringBuilder sbChiffré = new StringBuilder(messageAChiffrer);
		String messageChiffré = BinaireTochaine(sbChiffré);
		System.out.println("Message avant chiffrement : " + messageAChiffrerChaine);
		System.out.println("Le message chiffré est : " + messageChiffré);
		return null;
	}
	// Fonction qui va gérer la substitution entre 2 mots de 64 bits
	public static String[] Substitution(String[] tabMessage){
		for(int i = 0; i < tabMessage.length-1; i += 2){
			tabMessage[i] = AdditionModulaire(tabMessage[i], tabMessage[i+1]);
			tabMessage[i+1] = xor(tabMessage[i], PermutationCirculaire(tabMessage[i+1]));
		}
		return tabMessage;
	}
	
	// Fonction qui va réaliser une permutation circulaire sur un mot
	private static String PermutationCirculaire(String str1) {
		String[] tab1 = str1.split("");
		String result = "";
		for(int i = 0; i < tab1.length; i++){
			result += tab1[(i+49)%tab1.length];
		}
		return result;
	}
	
	// Fonction de permutation qui va inverser les elements du mot ([1 ... 64] => [64 ... 1])
	private static String Permutation(String str1){
		String[] tab1 = str1.split("");
		String resultat = "";
		for (int i = 0; i < tab1.length; i++){
			resultat += tab1[tab1.length-1-i];
		}
		return resultat;
	}

	private static String AdditionModulaire(String str1, String str2) {
		// TODO Auto-generated method stub
		boolean retenue = false;
		// On stocke le string le plus long dans str1
		if(str1.length() < str2.length()){
			String tampon = str1;
			str1 = str2;
			str2 = tampon;
		}
		String[] tab1 = str1.split("");
		String[] tab2 = str2.split("");
		String str3 = "";
		// On boucle sur le plus grand tableau tab1
		for (int i = 0; i < tab1.length; i++){
			// On vérifie qu'on soit toujours dans le tableau tab2
			if(i < tab2.length){
				// Si les deux tableaux contiennent un 1
				if(Integer.parseInt(tab1[tab1.length-1-i]) == Integer.parseInt(tab2[tab2.length-1-i]) && Integer.parseInt(tab1[tab1.length-1-i])==1){
					// Si y'avait déjà une retenue, la case vaudra 1
					if (retenue){
						str3 +="1";
					}
					// Sinon elle vaudra 0
					else {
						str3 +="0";
					}
					// On retient
					retenue = true;				
				}
				// Si les deux tableaux contiennent 0
				else if (Integer.parseInt(tab1[tab1.length-1-i]) == Integer.parseInt(tab2[tab2.length-1-i]) && Integer.parseInt(tab1[tab1.length-1-i])==0){
					// Si y'avait déjà une retenue, la case vaudra 1
					if (retenue){
						str3 +="1";
					}
					// Sinon elle vaudra 0
					else {
						str3 +="0";
					}
					// On retient pas
					retenue = false;
				}
				// Le cas où on a un 1 et un 0
				else{
					// Si y'avait déjà une retenue, la case vaudra 0 et on retiendra
					if (retenue){
						str3 +="0";
						retenue = true;
					}
					// Sinon elle vaudra 1 et on ne retiendra pas
					else {
						str3 +="1";
						retenue = false;
					}
				}
			}
			// Si le tab2 est plus petit alors le résultat dependra seulement de tab1
			else {
				if (retenue){
					switch(Integer.parseInt(tab1[tab1.length-1-i])){
					case 0: str3 += "1";
						retenue = false;
						break;
					case 1: str3 += "0";
						retenue = true;
						break;
					default: System.out.println("Error ?");
					}
				}
				else {
					str3 += tab1[tab1.length-1-i];
				}				
			}
		}
		// Il faut retourner le résultat obtenu
		String [] tab3 = str3.split("");
		String resultat = "";
		for(int i = tab3.length-1; i >=  0; i--){
			resultat += tab3[i];
		}
		//System.out.println("Str1 :" + str1 + "\nStr2 :" + str2 + "\nRésultat :" + resultat);
		return resultat;
	}

	public static String xor(String str1, String str2){
		try {
			String[] tab1 = str1.split("");
			String[] tab2 = str2.split("");
			String str3 = "";
			for (int i = 0; i < tab1.length; i++){
				if(Integer.parseInt(tab1[i]) == Integer.parseInt(tab2[i])){
					str3 += "0";
				}
				else{
					str3 += "1";
				}
			}
			//System.out.println(str1 + "\n" + str2 + "\n" + str3);
			return str3;
			
		}
		catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("Les chaines envoyées n'ont pas la meme taille, impossible de xorer");
			return null;
		}
		
	}

	public static void main(String[] args) {
		// On choisi ici entre 256, 512 ou 1024 pour la génération de la clé
		String cle = GenerationCle(256);
		// Le nombre de découpage qu'on va faire sur la clé
		int N = 0;
		switch(cle.length()){
		case 32: N=4;
		break;
		case 64: N=8;
		break;
		case 128: N=16;
		break;
		default: System.out.println("La taille de la clé n'est pas correcte");
		break;
		}
		// On rend la clé binaire
		String cleBinary = ChaineToBinaire(cle).toString();
		// On génère toutes les clés
		GenerationSousCles(cleBinary,N);
	}

}
