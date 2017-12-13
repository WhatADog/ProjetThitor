package GUnGrosPaquet;
//Packages à importer afin d'utiliser les objets
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
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
	        System.out.println("Répertoire courant : " + repertoireCourant);
	    } catch(IOException e) {}
     
	    // création de la boîte de dialogue dans ce répertoire courant
	    // (ou dans "home" s'il y a eu une erreur d'entrée/sortie, auquel
	    // cas repertoireCourant vaut null)
	    JFileChooser dialogue = new JFileChooser(repertoireCourant);
	     
	    // affichage
	    dialogue.showOpenDialog(null);
	     
	    // récupération du fichier sélectionné
	    System.out.println("Fichier choisi : " + dialogue.getSelectedFile());
		return dialogue.getSelectedFile().toString();

	}
	
	
	public static String Lecture() {
	    // Nous déclarons nos objets en dehors du bloc try/catch
		String cible = SelectionFichier();
	    FileInputStream fis = null;
	    String result = "";
	
	    try {
	       fis = new FileInputStream(new File(cible));
	       byte[] buf = new byte[8];
	       int n = 0;
	       while ((n = fis.read(buf)) >= 0) {    
	          for (byte bit : buf) {
	             result += (char)bit;
	          }
	          buf = new byte[8];	
	       }
	    } catch (FileNotFoundException e) {
	       e.printStackTrace();
	    } catch (IOException e) {
	       e.printStackTrace();
	    } finally {
	       try {
	          if (fis != null)
	             fis.close();
	       } catch (IOException e) {
	          e.printStackTrace();
	       }
	    }
		return result;		
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
	      
	       System.out.println("Ecriture terminée");
	       
	 }
}
