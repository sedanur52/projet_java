

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {

	private UUID ID;
	private String name;
	private String password;
	private boolean isFirstConnexion;
	
	
	public User(String name, String password){
        this.name = name;
        this.password = password;
        this.ID = UUID.randomUUID();
        this.isFirstConnexion=true;
    }

    public String getName(){return this.name;}
    public String getPass(){return this.password;}
    public UUID getId(){return this.ID;}
    public boolean getIsFirstConnexion(){return this.isFirstConnexion;}
    public String toString(){
        return "[" + this.ID + "]: " + this.name + ")";
    }
    protected void setNotFirstConnexion() {
    	this.isFirstConnexion=false;
    }
    
   
}
