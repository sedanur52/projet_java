
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

	
	
	public static void main(String arg[]) throws ClassNotFoundException {
        try {
            // Establishing a connection with the server
            Socket s_client = new Socket("127.0.0.1", 12345);
            System.out.println("Connection [OK]");
        
            Scanner scan = new Scanner(System.in);
           
            BufferedReader in = new BufferedReader(new InputStreamReader(s_client.getInputStream()));
            PrintWriter out = new PrintWriter(s_client.getOutputStream(), true);

			boolean m=false;
			
			File localDirectory = null;
			
			
			while(!m) {
				 System.out.println("entrer 'connexion' pour vour connecter ou 'inscription' pour vour inscrire\n");
					String choix = scan.nextLine();
			
				switch(choix){

					case "connexion": 
						out.println(choix);
						System.out.println("CONNEXION");
						System.out.println("identifiant=");
						String identifiant = scan.nextLine();
						out.println(identifiant);
						System.out.println("mot de passe=");
						String mdp = scan.nextLine();
						out.println(mdp);
						String inputLine1 = in.readLine();
						if(inputLine1.equals("true")) {
							System.out.println("première connexion");
							//localDirectory = Rep.createDirectoryInCurrentPath("Rep"+identifiant);
						}
						localDirectory = Rep.createDirectoryInCurrentPath("Rep"+identifiant);
						if(!(inputLine1.equals("true")) && !(inputLine1.equals("false"))) {
							System.out.println(inputLine1);
							
						}
						else {
						 OutputStream os = s_client.getOutputStream();
				         DataOutputStream outToServer = new DataOutputStream(os);
				         InputStream is = s_client.getInputStream();
	                     DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(is));
				         System.out.println((localDirectory.getName()));
				         out.println((localDirectory.getName()));
				         Rep.envoyerFichier(os, outToServer, localDirectory);
				         System.out.println("avant recevoir fichier" + localDirectory.getParent());
				         Rep.recevoirFichier(is,inFromServer, localDirectory.getParent());
				        
				         os.close();
				         outToServer.close();
				         is.close() ;
	                     inFromServer.close();
						}
						m=true;
						break;
	            
					case "inscription":
						out.println(choix);
						System.out.println("INSCRIPTION");
						System.out.println("identifiant=");
						String identifiant2 = scan.nextLine();
						while (identifiant2.length()>20) {
							System.out.println("La longueur de l'identifiant doit être inférieur à 20.\n Veuillez reentrez un identifant.");
							System.out.println("identifiant=");
							identifiant2 = scan.nextLine();
								
						}
						out.println(identifiant2);
						System.out.println("mot de passe=");
						String mdp2 = scan.nextLine();
						while (mdp2.length()>20) {
							System.out.println("La longueur du mot de passe doit être inférieur à 20.\n Veuillez reentrez un mot de passe.");
							System.out.println("mot de passe=");
							identifiant2 = scan.nextLine();
								
						}
						 out.println(mdp2);
						  String inputLine2 = in.readLine();
						 System.out.println(inputLine2);
						m=true;
						break;

					default:
						System.out.println("Choix incorrect");
						break;
					}
			}
			
			
			
		
            s_client.close();
        } catch (IOException E) {
            E.printStackTrace();
        }
    }
	
}
