import java.io.Serializable;
public class ArbreFamille implements Serializable {
	   private static final long serialVersionUID = 1;
	   private Personne racine;
	   
	    
	    public ArbreFamille() {
	        this.racine = null;        
	    }
	    
	    public void setRoot(Personne newRacine){
	        this.racine = newRacine;
	    }
	    public boolean hasRoot(){
	        return this.racine !=null;
	    }
	    public Personne getRoot(){
	        return this.racine;
	    }
	}
