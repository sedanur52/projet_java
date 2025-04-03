
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**/

class ServerTask extends Thread{
    private Socket client;
    public ServerTask(Socket client){
        this.client = client;
    }

    public void run(){
        try{
        	
        	  BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
              PrintWriter out = new PrintWriter(client.getOutputStream(), true);
              
              
              String inputLine0 = in.readLine(); // read string from client */ CONNEXION - INSCRIPTION */
              String inputLine1 = in.readLine(); // read string from client  */IDENTIFIANT */
              String inputLine2 = in.readLine(); // read string from client  */ PASSWORDS */

              System.out.println("Received from client: " + inputLine0 + inputLine1 + ", " + inputLine2);
           
              Long k= Server.database.stream()
                      .filter( p -> p.getName().contains(inputLine1))
                      .collect(Collectors.counting())
                      ;
              
              //gestion inscription
              if(inputLine0.equals("inscription")) {
            	  
            	  
                  
                  
                  if (k==0) {
                	
                	  User P = new User(inputLine1, inputLine2);
                	  Server.database.add(P);
                	  // 4. Send to the client the unique identifier
                      out.println("Welcolme "+ inputLine1 + "Your id: " + P.getId()+ "Votre inscription a bien été effectuée");
                      
          
                	  
                	
                  }
                  else {
                	  out.println("Un compte avec cet identifiant existe déjà.Veuillez réessayer avec un autre identifiant.");
                      
                  }
                    
              }
              
              //gestion connexion 
              else {
            	  
            		
    			  List<User> user_exists =Server.database.stream()
    					.filter( p -> p.getName().equals(inputLine1) && p.getPass().equals(inputLine2) )
    			        .collect(Collectors.toList())
    			        ;
            
            	  if(!(user_exists.isEmpty())) {
            		  
            		  boolean is_first_connection=user_exists.get(0).getIsFirstConnexion();
            		  if (is_first_connection) {
            			  out.println("true");
            			  user_exists.get(0).setNotFirstConnexion();
            		  }
            		  else {
            			  out.println("false");
            		  }
            		
            		  InputStream is = client.getInputStream();
                      DataInputStream inFromClient = new DataInputStream(new BufferedInputStream(is));
                      OutputStream os = client.getOutputStream();
				      DataOutputStream outToClient = new DataOutputStream(os);
                      String nomFileClient = in.readLine();
            		  System.out.println("Server side "+ nomFileClient);
                      
            		  Rep.recevoirFichier(is,inFromClient, Server.localDirectoryS.getAbsolutePath());
            		  
            		  System.out.println("Server side "+ Server.localDirectoryS.getAbsolutePath()+"/" + "repServeur");
            		  System.out.println("Server side "+ Server.localDirectoryS.getAbsolutePath()+"/"+ nomFileClient);
            		  Rep.syncRep(new File(Server.localDirectoryS.getAbsolutePath()+"/"+"repServeur"), new File(Server.localDirectoryS.getAbsolutePath()+"/"+nomFileClient));
            		  File d1=new File(Server.localDirectoryS.getAbsolutePath()+"/" + "repServeur");
                      File d2=new File(Server.localDirectoryS.getAbsolutePath()+"/"+ nomFileClient);
                      
                      d1.renameTo(new File(Server.localDirectoryS.getAbsolutePath()+"/"+ nomFileClient));
                      d2.renameTo(new File(Server.localDirectoryS.getAbsolutePath()+"/" + "repServeur"));
            		  System.out.println("Envoi fichier vers le client");
            		  Rep.envoyerFichier(os, outToClient, new File(Server.localDirectoryS.getAbsolutePath()+"/"+nomFileClient));
            		  is.close();
                      inFromClient.close();
                      os.close();
				      outToClient.close();
            		  
            	  }
            		  
            	  
            	  else if(k==1) {
            		  out.println("mot de passe incorrect");
            	  }
            	  else {
            		  out.println("identifiant incorrect, un tel compte n'existe pas");
            	  }
            	
              }
              
                
                
              this.client.close();    
              
            
          }
          catch(Exception E){
              System.out.println(E);
          }
          
        

    }
    
}


class WatchServiceTask extends Thread {
    private Path dir;
    private WatchService watcher;

    public WatchServiceTask(Path dir) throws IOException {
        this.dir = dir;
        this.watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
    }

    public void run() {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                return;
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                Path fileName = (Path) event.context();
                // Handle the created file here
                System.out.println("New file created: " + fileName);
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}









public class Server {
	
	
	
	

	static List<User> database = new ArrayList<>();
	static File localDirectoryS;
	
	

	  public static void main(String arg[]) throws IOException {
		  localDirectoryS = Rep.createDirectoryInCurrentPath("ServerRep");
		  if (Files.exists(Paths.get(localDirectoryS.getAbsolutePath()+"/"+"repServeur"))) {
          	Rep.deleteDirectory(Paths.get(localDirectoryS.getAbsolutePath()+"/"+"repServeur"));
		  }
		  Files.createDirectory(Paths.get(localDirectoryS.getAbsolutePath()+"/"+"repServeur"));
		  
	        ServerSocket server;
	        try {
	            //  Opening the port
	            server = new ServerSocket(12345);

	            System.out.println("Waiting for connections");
	            // Accept blocks until a new connection arrives
	            while(true){
	                Socket client = server.accept();
	                // Print some information from the client
	                System.out.println("Client [connected]: ");
	                System.out.println("Address: " +  client.getInetAddress().getHostAddress());
	                System.out.println("Port: " +  client.getPort());
	                
	                new ServerTask(client).start();
	                

	                System.out.println(Server.database);
	                // Ending the service
	                
	               
	            }
	        } catch (IOException E) {
	            E.printStackTrace();
	        }
	    }
	 
	 
	// Créer une instance de la classe WatchServiceTask pour surveiller le répertoire
	Path dir = Paths.get("chemin/vers/votre/répertoire");
	WatchServiceTask watchServiceTask = new WatchServiceTask(dir);
	// Démarrer la surveillance du répertoire dans un thread séparé
	

	public void watchDirectory(Path path) throws Exception {
	    WatchService watchService = FileSystems.getDefault().newWatchService();
	    path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
	    while (true) {
		WatchKey key = watchService.take();
		for (WatchEvent<?> event : key.pollEvents()) {
		    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
		        continue;
		    }
		    Path fileName = (Path) event.context();
		    System.out.println("File " + fileName + " has been added or modified.");
		}
		boolean reset = key.reset();
		if (!reset) {
		    break;
		}
	    }
	}

	watchServiceTask.start();
	 
	 
}



























































