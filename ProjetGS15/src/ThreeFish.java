
import java.util.Scanner;

// Classe qui va permettre de réaliser un chiffrement / déchiffrement avec ThreeFish, algorithme de chiffrement symétrique.
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
	
	// Fonction qui va gérer le binaire to chaine
	public static String BinaryToString(String str1){
		String resultat = "";
		/* 
		 * System.out.println("En entrée on a : " + str1); 
		 * */
		
		String[] tab1 = new String[str1.length()/8];
		for(int i = 0; i < tab1.length; i++){			
			tab1[i] = str1.substring(i*8, (i+1)*8);
			int charCode = Integer.parseInt(tab1[i],2);
			String str = new Character((char)charCode).toString();
			resultat += str;
			/*
			System.out.println(tab1[i] + "(" + charCode + ")" + "[" + Character.toString((char)charCode) + "]");
			*/
		}
		return resultat;
	}
	
	// Fonction qui va gérer le chaine to binaire
	public static String StringToBinary(String str1){
		String resultat = "";
		//byte[] b = str1.getBytes(StandardCharsets.US_ASCII);
		char[] tab1 = new char[str1.length()];
		for (int i = 0; i < str1.length(); i++){
			tab1[i] = str1.charAt(i);
			// En fonction de la taille de la conversion binaire de l'int du char on bourre avec des 0 pour faire 8 bits
			switch(Integer.toBinaryString((int)tab1[i]).length()){
			case 7:
				resultat += "0";
				break;
			case 6:
				resultat += "00";
				break;
			case 5:
				resultat += "000";
				break;
			case 4:
				resultat += "0000";
				break;
			case 3:
				resultat += "00000";
				break;
			case 2:
				resultat += "000000";
				break;
			case 1:
				resultat += "0000000";
				break;
				default:
					break;
			}
			resultat += Integer.toBinaryString((int)tab1[i]);
			/*
			System.out.println(Integer.toBinaryString((int)tab1[i]) + "(" + (int)tab1[i] + ")" + "[" + tab1[i]+ "]");
			*/
		}
		return resultat;
	}
	
	
	// Fonction qui va générer les tweaks
	public static String[] GenerationTweaks(String chaine, int N) {
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
		}		
		// On calcule t2 en xorant t0 et t1
		tweaks[2] = xor(tweaks[0], tweaks[1]);
		return tweaks;
	}
	// Fonction qui va générer les sous clés en séparant la clé principale en N morceaux et les tweaks
	public static String[] GenerationSousCles(String chaine, String[] tweaks, int N){
		String[] cle = chaine.split("");
		String[] sousCles = new String[N+1];
		for (int i = 1; i < N +1; i++){
			String tampon = "";
			for (int j = 64*(i-1); j < 64*i; j++){
				tampon += cle[j];
			}
			sousCles[i-1] = tampon;
		}
		
		
		// On initialise sousCles[N] avec la valeur de C
		sousCles[N] = "0001101111010001000110111101101010101001111111000001101000100010";
		// On calcule kN en xorant k1, k2, ...., kN-1 et C
		for(int i = 0; i < N; i++){
			sousCles[N] = xor(sousCles[i],sousCles[N]);
		}
		// Affichage des sous clés et des tweaks
		/*System.out.println("Les sous clés :");
		for (int i = 0; i < sousCles.length; i++){
			System.out.println("tab[" + (i)  + "] : "+ sousCles[i] );
		}
		System.out.println("Les 3 tweaks");
		for (int i = 0; i < tweaks.length; i++){
			System.out.println("tweak[" + (i)  + "] : "+ tweaks[i] );
		}*/
		return sousCles;		
	}
	
	// Fonction qui va générer toutes les clés de tournée
	public static String[][] GenerationClesTournees (String[] sousCles, String[] tweaks){
		// sousCles est notre tableau qui contient les sous clés, sa taille est donc N + 1 actuellement
		int N = sousCles.length-1;
		// On créer un tableau [20][N] car il y'a N-1 sous clés par tournée et 20 tournée, on est sur le modèle kn(i).
		String[][] clesTournees = new String[20][N];
		
		for (int i = 0; i < 20; i++){
			for(int n = 0; n < N; n++){
				
				if (n == N-3){
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)],tweaks[i%3]);
				}
				else if (n == N-2){
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)],tweaks[(i+1)%3]);
				}
				else if (n == N-1){
					clesTournees[i][n] = AdditionModulaire(sousCles[(i+n)%(N+1)], StringToBinary(Integer.toString(i)).toString());
				}
				else {
					clesTournees[i][n] = sousCles[(i+n)%(N+1)];
				}
			}
		}
		return clesTournees;
		// Affichage des clés de tournées
		/*for (int i = 0; i < 20; i++){
			System.out.println("Tournée n°" + i);
			for(int n = 0; n < N; n++){
				System.out.println("Cle["+n+"] : " + clesTournees[i][n]);
			}
		}*/
	}
	
	public static void ChiffrementThreeFish(int N, String[][] clesTournees, int mode){
				// Message à chiffrer
				System.out.println("Selectionnez le fichier à chiffrer !");
				String messageAChiffrerChaine = Utilitaires.Lecture();				
				// On le passe en binaire
				String messageAChiffrer = StringToBinary(messageAChiffrerChaine).toString();
				// On le découpe en blocs de 64 bits
				String[] tabTempo = messageAChiffrer.split("");				
				// On calcule la taille de notre message à chiffrer pour savoir comment le stocker
				int tailleMessage = tabTempo.length;
				// On regarde la taille des mots qu'on souhaite obtenir (256, 512 ou 1024)
				int tailleSousMessage = N * 64;
				// On calcule le nombre de sous message que l'on va avoir
				int nombreSousMessage = tailleMessage / tailleSousMessage;
				if((tailleMessage%tailleSousMessage) != 0){
					nombreSousMessage += 1;
				}
				// On créer le tableau qui va stocker le message découpé en sousMessage et blocks de 64bits
				String[][] tabAChiffrer = new String[nombreSousMessage][N];
				// On déclare une variable qui va compter le nombre de bits bourrés
				int bourrage = 0;
				// On rempli le tableau avec le message à chiffrer
				// Pour chaque sous message, on découpe en blocs de 64bits et on fait du bourrage si necessaire
				for (int i = 0; i < nombreSousMessage; i++){
					for(int j = 0; j < N; j++){
						String tempo = "";
						for(int k = (j*64)+(i*64*N); k < 64*(j+1)+(i*64*N); k++){
							// S'il reste quelque chose dans tabTempo
							if(k <= tabTempo.length-1){
								tempo += tabTempo[k];
							}
							// Sinon on bourre avec des 0
							else{
								// On incrémente le nombre de bits bourrés
								bourrage ++;
								tempo += 0;
							}
						}
						tabAChiffrer[i][j] = tempo;
					}			
				}				
				// Boucle qui va gérer les 76 tournées avec les 20 ajouts de clés
				// Pour chaque mot on va appliquer le chiffrement : ECB
				if(mode == 0){					
					for (int i = 0; i < nombreSousMessage; i++){
						// Pour chaque tournée
						for(int j = 0; j < 20; j++){
							for(int k = 0; k <N; k++){
								// On xor le message avec les clés de tournées
								tabAChiffrer[i][k] = xor(tabAChiffrer[i][k],clesTournees[j][k]);
							}
							// On effectue 4 mix + Permute
							for(int l = 0; l < 4; l++){
								tabAChiffrer[i] = Substitution(tabAChiffrer[i]);
								for(int m = 0; m < N; m++){
									tabAChiffrer[i][m] = Permutation(tabAChiffrer[i][m]);
								}
							}
						}
					}
				}
				// Pour chaque mot on va appliquer le chiffrement : CBC
				else{
					String vecteurInit = "On va générer un vecteur d'initialisation de 256, 512 ou 1024 bits en fonction "
							+ "de la taille des blocs de texte clair. On va ensuite ce servir de ce vecteur d'initialisation pour"
							+ "xorer a l'etape 1";
					// En fonction de la taille des blocs la taille du vecteur va varier
					vecteurInit = vecteurInit.substring(0,8*N);
					String vecteurInitBinaire = StringToBinary(vecteurInit).toString();
					// Pour chaque bloc en clair on xor avec le chiffré precedent (ou avec IV si premier bloc)
					for (int i = 0; i < nombreSousMessage; i++){
						String[] tempo = new String[N];
						if(i == 0){
							// On xor le bloc clair #0 avec le vecteur d'initialisation
							tempo = BlocEnMots(xor(MotsEnBloc(tabAChiffrer[i], N),vecteurInitBinaire), N);
							for(int j = 0; j < N; j++){
								tabAChiffrer[i][j] = tempo[j];
							}
						}
						else{
							// On xor le bloc clair #i avec le chiffré (i-1)
							tempo = BlocEnMots(xor(MotsEnBloc(tabAChiffrer[i], N), MotsEnBloc(tabAChiffrer[i-1], N)), N);
							for(int j = 0; j < N; j++){
								tabAChiffrer[i][j] = tempo[j];
							}
						}
						// Pour chaque tournée
						for(int j = 0; j < 20; j++){
							// On xor le message avec les clés de tournées
							for(int k = 0; k <N; k++){								
								tabAChiffrer[i][k] = xor(tabAChiffrer[i][k],clesTournees[j][k]);
							}
							// On effectue 4 mix + Permute
							for(int l = 0; l < 4; l++){
								tabAChiffrer[i] = Substitution(tabAChiffrer[i]);
								for(int m = 0; m < N; m++){
									tabAChiffrer[i][m] = Permutation(tabAChiffrer[i][m]);
								}
							}
						}
					}
				}
				
				
				// On remet le tabAChiffrer sous forme de String
				messageAChiffrer = "";
				for(int i = 0; i < nombreSousMessage; i++){
					for (int j = 0; j < N; j++){				
							messageAChiffrer += tabAChiffrer[i][j];
						}
				}
				
				// On ajoute la taille du bourrage sur 10 bits, maximum de bits bourrés qu'on peut avoir => 1023 char
				String bourrageString = Integer.toBinaryString(bourrage);
				// On fait en sorte que la bourrage soit toujours sur 10 bits;
				while(bourrageString.length() < 10){
					bourrageString = "0" + bourrageString;
				}
				// On ajoute le bourrage
				messageAChiffrer += bourrageString;

				System.out.println("Message avant chiffrement : \n" + messageAChiffrerChaine);
				// System.out.println("\nLe message chiffré est : " + messageAChiffrer);
				Utilitaires.Ecriture(messageAChiffrer, "Chiffré.txt");
	}
		
	
	public static void DechiffrementThreeFish(int N, String[][] clesTournees, int mode){		
		// On lit le message binaire chiffré
		System.out.println("Selectionnez le message à dechiffrer !");
		String messageADechiffrer = Utilitaires.Lecture();
		// On récupère le bourrage binaire
		String bourrageString = messageADechiffrer.substring(messageADechiffrer.length()-10, messageADechiffrer.length());
		// On le transforme en int
		int bourrage = Integer.parseInt(bourrageString, 2);
		// On enleve les bits bourrés au message initial
		messageADechiffrer = messageADechiffrer.substring(0, messageADechiffrer.length()-10);
		System.out.println("Message à déchiffrer : " + messageADechiffrer);
		// On le découpe en blocs de 64 bits
		String[] tabTempo2 = messageADechiffrer.split("");		
		
		// On calcule la taille de notre message à chiffrer pour savoir comment le stocker
		int tailleMessageADechiffrer = tabTempo2.length;
		//System.out.println("Taille du message : "+ tailleMessageADechiffrer);
		// On regarde la taille des mots qu'on souhaite obtenir (256, 512 ou 1024)
		int tailleSousMessageADechiffrer = N * 64;
		//System.out.println("Taille des mots : " + tailleSousMessageADechiffrer);
		// On calcule le nombre de sous message que l'on va avoir
		int nombreSousMessageADechiffrer = tailleMessageADechiffrer / tailleSousMessageADechiffrer;
		if((tailleMessageADechiffrer%tailleSousMessageADechiffrer) != 0){
			nombreSousMessageADechiffrer += 1;
		}
		
		// On créer le tableau qui va stocker le message découpé en sousMessage et blocks de 64bits
		String[][] tabADechiffrer = new String[nombreSousMessageADechiffrer][N];
		
		// On rempli le tableau avec le message à déchiffrer
		// Pour chaque sous message, on découpe en blocs de 64bits et on fait du bourrage si necessaire
		for (int i = 0; i < nombreSousMessageADechiffrer; i++){
			for(int j = 0; j < N; j++){
				String tempo2 = "";
				for(int k = (j*64)+(i*64*N); k < 64*(j+1)+(i*64*N); k++){
					// S'il reste quelque chose dans tabTempo
					if(k <= tabTempo2.length-1){
						tempo2 += tabTempo2[k];
					}
					// Sinon on bourre avec des 0
					else{
						tempo2 += "0";
					}
				}
				tabADechiffrer[i][j] = tempo2;
			}			
		}	
		
		// Boucle qui va gérer les 76 tournées avec les 20 ajouts de clés
		// Pour chaque mot on va appliquer le déchiffrement : ECB
		if(mode == 0){
			for (int i = 0; i < nombreSousMessageADechiffrer; i++){
				for(int j = 19; j >= 0; j--){				
					// On effectue 4 mix + Permute
					for(int l = 0; l < 4; l++){
						for(int m = 0; m < N; m++){
							tabADechiffrer[i][m] = Permutation(tabADechiffrer[i][m]);
						}
						tabADechiffrer[i] = AntiSubstitution(tabADechiffrer[i]);
						
					}
					
					for(int k = 0; k <N; k++){
						// On xor le message avec les clés de tournées
						tabADechiffrer[i][k] = xor(tabADechiffrer[i][k],clesTournees[j][k]);
					}
				}
			}
		}
		// Pour chaque mot on va appliquer le déchiffrement CBC
		else{
			String vecteurInit = "On va générer un vecteur d'initialisation de 256, 512 ou 1024 bits en fonction "
					+ "de la taille des blocs de texte clair. On va ensuite ce servir de ce vecteur d'initialisation pour"
					+ "xorer a l'etape 1";
			// En fonction de la taille des blocs la taille du vecteur va varier
			vecteurInit = vecteurInit.substring(0,8*N);
			String vecteurInitBinaire = StringToBinary(vecteurInit).toString();
			for (int i = nombreSousMessageADechiffrer-1; i >= 0 ; i--){
				for(int j = 19; j >= 0; j--){				
					// On effectue 4 mix + Permute
					for(int l = 0; l < 4; l++){
						for(int m = 0; m < N; m++){
							tabADechiffrer[i][m] = Permutation(tabADechiffrer[i][m]);
						}
						tabADechiffrer[i] = AntiSubstitution(tabADechiffrer[i]);
						
					}
					
					for(int k = 0; k <N; k++){
						// On xor le message avec les clés de tournées
						tabADechiffrer[i][k] = xor(tabADechiffrer[i][k],clesTournees[j][k]);
					}
				}
				String[] tempo = new String[N];
				if(i == 0){
					tempo = BlocEnMots(xor(MotsEnBloc(tabADechiffrer[i], N), vecteurInitBinaire), N);
					for(int z = 0; z < N; z++){
						tabADechiffrer[i][z] = tempo[z];
					}
				}
				else{
					tempo = BlocEnMots(xor(MotsEnBloc(tabADechiffrer[i], N),MotsEnBloc(tabADechiffrer[i-1], N)), N);
					for(int z = 0; z < N; z++){
						tabADechiffrer[i][z] = tempo[z];
					}
				}
			}
		}
		
		
		// On remet le tabAChiffrer sous forme de String
		messageADechiffrer = "";
		for(int i = 0; i < nombreSousMessageADechiffrer; i++){
			for (int j = 0; j < N; j++){
				messageADechiffrer += tabADechiffrer[i][j];
			}
		}
		// On s'occupe d'enlever le bourrage
		messageADechiffrer = messageADechiffrer.substring(0, messageADechiffrer.length()- bourrage);
		
		// On peut convertir en chaine de caractères le message chiffré
		String messageDechiffré = BinaryToString(messageADechiffrer);
		System.out.println("\nLe message déchiffré est : ");
		System.out.println(messageDechiffré);
		Utilitaires.Ecriture(messageDechiffré, "Déchiffré.txt");
	}
	
	// Fonction qui va transformer un bloc en N mots de 64 bits
	public static String[] BlocEnMots(String bloc, int N){
		String[] mots = new String[N];
		for (int i = 0; i < N; i++){
			mots[i] = bloc.substring(i*64, (i+1)*64);
		}
		return mots;		
	}
	
	// Fonction qui va transformer N mots de 64 bits en un bloc
	public static String MotsEnBloc(String[] mots, int N){
		String bloc = "";
		for (int i = 0; i < N; i++){
			bloc += mots[i];
		}
		return bloc;		
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
	
	// Fonction qui va gérer la substitution inverse entre 2 mots de 64 bits
	public static String[] AntiSubstitution(String[] tabMessage){
		// Pour inverser la Substitution, il faut commencer par trouvrer m2 en xorant puis appliquant l'anti permut circulaire (voir sujet)
		for(int i = 0; i < tabMessage.length-1; i += 2){
			// On trouve m2
			tabMessage[i+1] = xor(tabMessage[i], tabMessage[i+1]);
			tabMessage[i+1] = AntiPermutationCirculaire(tabMessage[i+1]);
			// Pour inverser une addition modulaire, on peut ajouter l'opposé.
			// On dispose de m'1 = tab[i] et de m2 = tab[i+1]. Et m'1 = AdditionModulaire(m1,m2).
			// On peut retrouver m1 en faisant m1 = AdditionModulaire(m'1, not(m2))
			// On cherche l'opposé de m2 dans notre ensemble Z
			String opposé = CalculOpposé(tabMessage[i+1]);
			tabMessage[i] = AdditionModulaire(tabMessage[i], opposé);
		}
		return tabMessage;
	}
	
	// Fonction qui va chercher l'opposé d'un element
	public static String CalculOpposé(String str1){
		String[] tab1 = str1.split("");
		String[] tab2 = new String[tab1.length];
		boolean retenue = false;
		int taille = tab1.length - 1;
		// On fait au cas pas cas pour calculé l'opposé
		for (int i = taille; i >= 0 ; i--){
			if(Integer.parseInt(tab1[i]) == 0 && retenue){
				tab2[taille-i] = "1";
				retenue = true;
			}
			else if (Integer.parseInt(tab1[i]) == 0 && !retenue){
				tab2[taille-i] = "0";
				retenue = false;
			}
			else if (Integer.parseInt(tab1[i]) == 1 && !retenue){
				tab2[taille-i] = "1";
				retenue = true;
			}
			else{
				tab2[taille-i] = "0";
				retenue = true;
			}			
		}
		String str2 = "";
		for(int i = tab2.length-1; i >=  0; i--){
			str2 += tab2[i];
		}
		// On va vérifier que AdditionModulaire(tab1, tab2) = 0
		String test = AdditionModulaire(str1, str2);
		//System.out.println("Opposé trouvé : " + str2);
		//System.out.println("Résultat de l'addition modulaire : " + test);
		if (Integer.parseInt(test) == 0){
			return str2;
		}
		return null;
	}
	
	// Fonction qui va réaliser une permutation circulaire sur un mot, cette permutation s'opposera à la fct PermutationCirculaire
	private static String AntiPermutationCirculaire(String str1){
		String[] tab1 = str1.split("");
		String result = "";
		for(int i = 0; i < tab1.length; i++){
			result += tab1[(i-49+tab1.length)%tab1.length];
		}
		return result;
	}
	
	private static String AdditionModulaire(String str1, String str2) {
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
			System.out.println("La premiere chaine fait : " + str1.length() + " alors que la deuxième fait : " + str2.length());
			return null;
		}
		
	}
	
	public static void Initialisation(Scanner scan){
		System.out.println("Bonjour et bienvenue dans le chiffrement symétrique ThreeFish.\n");
		// On choisi ici entre 256, 512 ou 1024 pour la génération de la clé
		System.out.println("Vous souhaitez utiliser une clé de 256, 512 ou 1024 bits ?");
		int tailleCle = scan.nextInt();
		int user_input = 0;
		do{
			System.out.println("\nQue voulez vous faire ?\n1-Chiffrer en mode ECB\n2-Chiffrer en mode CBC\n3-Dechiffrer en mode ECB\n4-Dechiffrer en mode CBC\n5-Chiffrer et Dechiffrer en mode ECB\n6-Chiffrer et Dechiffrer en mode CBC\n7-Revenir au menu principal");
			user_input = scan.nextInt();
			int N = tailleCle/64;
			// On calcule et on stocke les cles de tournees
			String[][] clesTournees = new String[20][N];
			if ( 0 < user_input && user_input<7){
				clesTournees = generationCles(scan,tailleCle);
			}
			
			
			switch (user_input) {
			case 1:
				ChiffrementThreeFish(N, clesTournees, 0);
				break;
			case 2:
				ChiffrementThreeFish(N, clesTournees, 1);
				break;
			case 3:
				DechiffrementThreeFish(N, clesTournees, 0);
				break;
			case 4:
				DechiffrementThreeFish(N, clesTournees, 1);
				break;
			case 5: 
				ChiffrementThreeFish(N, clesTournees, 0);
				DechiffrementThreeFish(N, clesTournees, 0);
				break;
			case 6: 
				ChiffrementThreeFish(N, clesTournees, 1);
				DechiffrementThreeFish(N, clesTournees, 1);
				break;
			case 7:
				System.out.println("Retour au menu principal !");
			default:
				System.out.println("Entree non reconnue, fin du chiffrement de ThreeFish !");
				break;
			}
		}
		while (user_input <= 6 && user_input >= 1);
	}
	
	// Fonction qui va s'occuper de generer une cle à partir d'un mot de passe et generer par la suite les cles de tournée et les tweaks
	public static String[][] generationCles(Scanner scan, int tailleCle){
		// On genere un hash de taille correspondant
		String cle = Hashage.generationHashThreeFish(scan, tailleCle);
		System.out.println(cle.length());
		// Le nombre de découpage qu'on va faire sur la clé
		int N = 0;
		// En fonction de la taille de la clé on en déduit le nombre de découpage de la clé
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
		String cleBinary = StringToBinary(cle).toString();
		// On génère les tweaks
		String [] tweaks = new String[3];
		tweaks = GenerationTweaks(cleBinary, N);
		// On génère toutes les clés
		String[] sousCles = GenerationSousCles(cleBinary, tweaks ,N);
		// On génère les clés de tournées
		String[][] clesTournees = new String[20][N];
		clesTournees = GenerationClesTournees(sousCles, tweaks);
		return clesTournees;
	}
	
	//Utilisé pour des besoins de tests
	public static void main(String[] args) {
		Scanner scan = new Scanner( System.in );
		Initialisation(scan);
		
		// On ferme le scanner
		scan.close();
	}
}
