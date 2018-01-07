import java.io.BufferedReader;
import java.io.BufferedWriter;
//Packages à importer afin d'utiliser les objets
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Utilitaires {
	public static String SelectionFichier() { 
	    // Boîte de sélection de fichier à partir du répertoire courant
	    File repertoireCourant = null;
	    try {
	        // obtention d'un objet File qui désigne le répertoire courant. Le
	        // "getCanonicalFile" n'est pas absolument nécessaire mais permet
	        // d'éviter les /Truc/./Chose/ ...
	        repertoireCourant = new File(".").getCanonicalFile();
	        //System.out.println("Répertoire courant : " + repertoireCourant);
	    } catch(IOException e) {}
     
	    // création de la boîte de dialogue dans ce répertoire courant
	    // (ou dans "home" s'il y a eu une erreur d'entrée/sortie, auquel
	    // cas repertoireCourant vaut null)
	    JFileChooser dialogue = new JFileChooser(repertoireCourant);
	     
	    // affichage
	    dialogue.showOpenDialog(null);
	     
	    // récupération du fichier sélectionné
	    System.out.println("\nFichier choisi : " + dialogue.getSelectedFile());
		return dialogue.getSelectedFile().toString();

	}
	
	// Fonction de lecture sans entrée
	public static String Lecture() {
	    	String cible = SelectionFichier();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(cible));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line;
			String resultat = "";
			try {
				while ((line = br.readLine()) != null) {
				   // process the line.
					resultat += line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultat;
		}

	// Fonction de lecture avec en entrée le fichier à lire
	public static String Lecture(String cible){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(cible));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		String resultat = "";
		try {
			while ((line = br.readLine()) != null) {
			   // process the line.
				resultat += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultat;
	}
	
	
	// Fonction qui va permettre d'écrire dans un fichier
		public static void Ecriture(String texte, String cible) {	      
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(new File(cible));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		       byte[] texteAEcrire = texte.getBytes();
		       try {
					fos.write(texteAEcrire);
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		       
		 }
	

}
