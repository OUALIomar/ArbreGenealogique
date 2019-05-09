import java.io.Serializable;

import java.util.ArrayList;



public class Personne implements Serializable{


    private String nom;

    private String prenom;

    private String maidenName;

    private Sexe sexe;

    private String dateNaissance;

    private String dateDeces;

    //exemple de nom valide Arthur Henri Jr. ou L'ourve D'Marche

    private final String nameRegex = "^[\\p{L} .'-]+$";

    private Personne mere;

    private Personne pere;

    private Personne spouse;

    private ArrayList<Personne> enfants;

    


    public Personne(String nom, String prenom, Sexe sexe, String dateNaissance, String dateDeces) {

		

		this.nom = nom;

		this.prenom = prenom;

		this.sexe = sexe;

		this.dateNaissance = dateNaissance;

		this.dateDeces = dateDeces;

		this.mere = null;

		this.pere = null;

		this.spouse = null;

		this.enfants = new ArrayList<Personne>();

	}

    



	public String getNom() {

		return nom;

	}



	public void setNom(String nom) {

		 if (nom.trim().matches(nameRegex)) {

	            this.nom = nom.trim();

	        }else{

	            throw new IllegalArgumentException("Nom invalide");

	        }

	}



	public String getPrenom() {

		return prenom;

	}



	public void setPrenom(String prenom) {

		   if (prenom.trim().matches(nameRegex)) {

	            this.prenom = prenom.trim();

	        }else{

	            throw new IllegalArgumentException("Prénopm invalide");

	        }

	}



	public Sexe getSexe() {

		return sexe;

	}



	public void setSexe(Sexe sexe) {

		this.sexe = sexe;

	}



	public String getDateNaissance() {

		return dateNaissance;

	}



	public void setDateNaissance(String dateNaissance) {

		this.dateNaissance = dateNaissance;

	}



	public String getDateDeces() {

		return dateDeces;

	}



	public void setDateDeces(String dateDeces) {

		this.dateDeces = dateDeces;

	}



	public Personne getMere() {

		return mere;

	}



	public void setMere(Personne mere) {

        if (!this.has(Attribute.MERE)) {

            if (mere.getSexe() == Sexe.FEMME) {

                if (!mere.getEnfants().contains(this)){

                	mere.getEnfants().add(this);

                }

                this.mere = mere;



                

            }else{

                throw new IllegalArgumentException("La mère doit être une femme");

            }

           

        }else{

            throw new IllegalArgumentException("La mère a déjà été saisie");

        }

        

	}



	public Personne getPere() {

		return pere;

	}



	public void setPere(Personne pere) {

		 if (!this.has(Attribute.PERE)) {

	            if (pere.getSexe() == Sexe.HOMME) {

	                if (!pere.getEnfants().contains(this)){

	                	pere.getEnfants().add(this);

	                }

	                this.pere = pere;

	                

	                

	            }else{

	                throw new IllegalArgumentException("Le père doit être un mâle");

	            }

	            

	        }else{

	            throw new IllegalArgumentException("Le père a déjà été saisi");

	        }

	}



	public ArrayList<Personne> getEnfants() {

		return enfants;

	}



	public void setEnfants(ArrayList<Personne> enfants) {

		this.enfants = enfants;

	}

	public Personne getSpouse() {

        return spouse;

    }



	public void setSpouse(Personne spouse) {

	       if (!this.has(Attribute.SPOUSE)) {

	            if(spouse.getSexe() != this.getSexe()){

	                spouse.setEnfants(this.getEnfants());

	                this.spouse = spouse;

	                if (!this.getSpouse().has(Attribute.SPOUSE)) {

	                    spouse.setSpouse(this);

	                }



	            }else{

	                throw new IllegalArgumentException("L'époux doit être du sexe opposé");

	            }

	        }else{

	            throw new IllegalArgumentException("Epoux existe déjà");

	        }

	}
	

//énumération des différents type de proches

    public enum Attribute {

        PERE,

        MERE,

        ENFANT,

        SPOUSE,

        PARENTS;

    }



    public enum RelativeType {

        PERE,

        MERE,

        ENFANT,

        SPOUSE;

    } 



    public enum Sexe {

        HOMME,

        FEMME;

    }

  
    public void addEnfant(Personne enfant) {

        //pere

        if (this.sexe == Sexe.HOMME) {

            

            if (!enfant.has(Attribute.PERE)) {

            	enfant.setPere(this);

            }

            

            if (this.has(Attribute.SPOUSE)) {

                if (!enfant.has(Attribute.MERE)) {

                	enfant.setMere(this.getSpouse());

                }

            }

        //mere

        }else if (this.sexe == Sexe.FEMME){

            

            if (!enfant.has(Attribute.MERE)) {

            	enfant.setMere(this);

            }


            if (this.has(Attribute.SPOUSE)) {

                if (!enfant.has(Attribute.PERE)) {

                	enfant.setPere(this.getSpouse());

                }

            }

        }


        if(!this.getEnfants().contains(enfant)){

            this.getEnfants().add(enfant);

        }

    }

 

    public int numEnfant(){

        return this.getEnfants().size();

    }

    

    /**

      verifie si la personne a une attribution specifique*/



    public boolean has(Personne.Attribute type){

        switch(type){

            case PERE:

                return this.getPere() != null;

            case ENFANT:

                return !this.getEnfants().isEmpty();

            case MERE:

                return this.getMere() != null;

            case SPOUSE:

                return this.getSpouse() != null;

           

            case PARENTS:

                return this.has(Attribute.PERE) || this.has(Attribute.MERE);

        }

        return false;

    }


    public void addRelative(Personne.RelativeType type, Personne member){

        switch(type){

            case PERE:

                this.setPere(member);

                return;

            case ENFANT:

                this.addEnfant(member);

                return;

            case MERE:

                this.setMere(member);

                return;

            case SPOUSE:

                this.setSpouse(member);

                return;

        }

    }

    @Override

    public String toString() {

        String s = null;

        if (this.sexe == Sexe.HOMME){

            s = " M   ";

        }else if (this.sexe == Sexe.FEMME){

            s = " F   ";

        }

        s += this.getNom() + " " + this.getPrenom(); 

       

        return s;

    }

}