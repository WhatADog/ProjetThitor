
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
	    // Bo�te de s�lection de fichier � partir du r�pertoire courant
	    File repertoireCourant = null;
	    try {
	        // obtention d'un objet File qui d�signe le r�pertoire courant. Le
	        // "getCanonicalFile" n'est pas absolument n�cessaire mais permet
	        // d'�viter les /Truc/./Chose/ ...
	        repertoireCourant = new File(".").getCanonicalFile();
	        System.out.println("R�pertoire courant : " + repertoireCourant);
	    } catch(IOException e) {}

	    // cr�ation de la bo�te de dialogue dans ce r�pertoire courant
	    // (ou dans "home" s'il y a eu une erreur d'entr�e/sortie, auquel
	    // cas repertoireCourant vaut null)
	    JFileChooser dialogue = new JFileChooser(repertoireCourant);

	    // affichage
	    dialogue.showOpenDialog(null);

	    // r�cup�ration du fichier s�lectionn�
	    System.out.println("Fichier choisi : " + dialogue.getSelectedFile());
		return dialogue.getSelectedFile().toString();

	}


	public static String Lecture() {
	    // Nous d�clarons nos objets en dehors du bloc try/catch
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

	// Fonction qui va permettre d'�crire dans un fichier
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

	       System.out.println("Ecriture termin�e");

	 }
}
