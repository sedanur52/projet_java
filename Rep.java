
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;



public class Rep {
	
	public  static String TypeFile(File f){
		if(f.isDirectory()) {
			return "Directory";
		}
		else return "File";
	}
	
	
	/**
	 * fonction qui prend en paramètre un nom de répertoire et 
	 * crée un répertoire à partir du chemin courant
	 * @param directoryName
	 * 
	 */
	
	public static File createDirectoryInCurrentPath(String directoryName) {
	    String currentPath = System.getProperty("user.dir");
	    File directory = new File(currentPath + "/" + directoryName);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }
	    return directory;
	}
	

	
	public static boolean exist(File f, File rep) {

		if(Files.exists(new File(rep, f.getName()).toPath())) {
 		   return TypeFile(f).equals(TypeFile(new File(rep, f.getName())));
 	   }
		return false;
		
    }
	
	
	  public static void copyFileToDirectory(File sourceFile, File destinationDir) throws IOException {
		    
		  	System.out.println("Inside copyFileToDirectory");
		  	Path sourcePath = sourceFile.toPath();
	  		Path destinationPath = new File(destinationDir, sourceFile.getName()).toPath();
		  	if (sourceFile.isFile()) {
		  		// System.out.println("Inside copyFileToDirectory if");
		  		Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		  		// System.out.println("Inside copyFileToDirectory end if");
		  	}
		  	if (sourceFile.isDirectory()) {
		  		//System.out.println("Inside copyFileToDirectory second if");
		  		Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		  		 File[] files1 = sourceFile.listFiles();
			        
			        
			        // Convertir le tableau de fichiers en une liste ArrayList
			        List<File> fileList = new ArrayList<>(Arrays.asList(files1));
			       
			        
			        // Parcourir la liste des fichiers 
			        for (File file : fileList) {
			        	copyFileToDirectory(file, new File(destinationDir, sourceFile.getName()));
			            
			           
			        }
			        // System.out.println("Inside copyFileToDirectory end second if");
			        
		  	}
	    }
	
	
	
	    
	    public  synchronized static void syncRep(File rep1, File rep2) throws IOException, NoSuchAlgorithmException {
	    	
	    	   File[] files1 = rep1.listFiles();
		       File[] files2 = rep2.listFiles();
		        
		       // Convertir le tableau de fichiers en une liste ArrayList
		       List<File> fileList = new ArrayList<>(Arrays.asList(files1));
		       List<File> fileList2 = new ArrayList<>(Arrays.asList(files2));
		       
	        //List<File> listeFichierModifieServer = new ArrayList<File>();
		       
		       for (File file : fileList) {
		    		   if(!(exist(file,rep2))) {
		    			   System.out.println("Inside sync if");
		    			   copyFileToDirectory(file, rep2);
		    			   System.out.println("Inside sync end if");
		    		   }
		    		   else {
		    			   System.out.println("Inside sync else");
		    			   if(file.isFile()) {
		    				   String hex = new BigInteger(1, Digest.md5(file.getAbsolutePath())).toString(16);
		    				   String d=new File(rep2, file.getName()).getAbsolutePath();
		    				   String hex2 = new BigInteger(1, Digest.md5(d)).toString(16);
		    				   
		    				   if(!(hex.equals(hex2))) {
		    					   //System.out.println(file+ " et" + new File(rep2, file.getName()) + " sont pas pareil");
		    					   //System.out.println(file +" : "+ Files.getLastModifiedTime(file.toPath()));
		    					   //System.out.println(file +" : "+ file.lastModified());
		    					   //System.out.println(new File(rep2, file.getName()) +" : "+ Files.getLastModifiedTime(new File(rep2, file.getName()).toPath()));
		    					   //System.out.println(new File(rep2, file.getName()) +" : "+ new File(rep2, file.getName()).lastModified());
		    					   
		    					   if (file.lastModified() > new File(rep2, file.getName()).lastModified()) {
		    					         //System.out.println(file+" a été modifié plus récemment que "+ new File(rep2, file.getName()));
		    					         copyFileToDirectory(file, rep2);
		    					         
		    					   } else {
		    					         //System.out.println(new File(rep2, file.getName())+" a été modifié plus récemment que "+ file);
		    					         copyFileToDirectory(new File(rep2, file.getName()), rep1);
		    					         //listeFichierModifieServer.add(new File(rep2, file.getName()));
		    					         
		    					     }
		    					   
		    				   }
		    			   } 
		    			   else {
	    					   syncRep(file, new File(rep2, file.getName()));
	    					   
	    				   }
		    		   }
		        }
		       
		       
		       for (File file : fileList2) {
	    		   if(!(exist(file,rep1))) {
	    			   copyFileToDirectory(file, rep1);
	    			   //listeFichierModifieServer.add(file);
	    		   }
		       }
		       
		       //return listeFichierModifieServer;
	    	
	    }
	
	    public static void deleteDirectory(Path directory) throws IOException {
	        Files.walk(directory)
	                .sorted(Comparator.reverseOrder())
	                .map(Path::toFile)
	                .forEach(File::delete);
	    }
	  
	    
	    public  static void recevoirFichier(InputStream is,DataInputStream inFromClient, String Pathrep) throws IOException {

			boolean isDirectory=inFromClient.readBoolean();
			long fileLength = inFromClient.readLong();
			String fileName =inFromClient.readUTF();
			long fileLasModified = inFromClient.readLong();
			String Pathfichier=Pathrep+"/"+fileName;
			if(isDirectory) {
				  if (Files.exists(Paths.get(Pathfichier))) {
		            	deleteDirectory(Paths.get(Pathfichier));
					}
				Files.createDirectory(Paths.get(Pathfichier));
		        int nbFile = inFromClient.readInt();
				new File(Pathfichier).setLastModified(fileLasModified);
				for (int i=0; i<nbFile;i++){
					boolean isDirectory2=inFromClient.readBoolean();
					recevoirFichier(is, inFromClient, Pathfichier);
				}
			}
			else {
				Files.createFile(Paths.get(Pathfichier));
		        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Pathfichier));
		        byte[] buffer = new byte[4096];
		        int numBytes;
		        while (fileLength > 0) {
		        	if (fileLength < buffer.length) {
		                numBytes = inFromClient.read(buffer, 0, (int) fileLength);
		            }
		            else {
		            	numBytes = inFromClient.read(buffer, 0, buffer.length);
		            }
		        	
		        	if (numBytes < 1) {
		                break;
		            }

		            try {
		            	bos.write(buffer, 0, numBytes);
		            }
		            catch (Exception e) {
		                break;
		            }

		            fileLength -= numBytes;
		        }
				new File(Pathfichier).setLastModified(fileLasModified);
		        bos.flush();
		        bos.close();
			}
			
		}
	    
	    
	    
	    public  static void  envoyerFichier(OutputStream os,DataOutputStream outToServer, File f) throws IOException {
	    	System.out.println("inside envoyerFichier " + f.getPath());
			boolean isDirectory=f.isDirectory();
			int fileLength=(int) f.length();
			String fileName = f.getName();
			long fileLasModified = f.lastModified();
			outToServer.writeBoolean(isDirectory);
			outToServer.writeLong(fileLength);
			outToServer.writeUTF(fileName);
			outToServer.writeLong(fileLasModified);
			
			if(f.isFile()) {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			    byte[] buffer = new byte[4096];
		        int bytesRead;
		        while ((bytesRead = bis.read(buffer)) != -1) {
		            os.write(buffer, 0, bytesRead);
		        }
		        bis.close();
			}
			else {
				File[] files1 = f.listFiles();
				List<File> fileList = new ArrayList<>(Arrays.asList(files1));
				int nbFile=fileList.size();
				outToServer.writeInt(nbFile);
				
				for (File file : fileList) {
					outToServer.writeBoolean(file.isDirectory());
					envoyerFichier(os, outToServer, file);
	            }
				
			}
	    
	        
		}
	    
	    

		 
}

