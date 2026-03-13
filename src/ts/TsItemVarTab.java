package ts;
import util.Type;

public class TsItemVarTab  extends TsItemVar{
    public TsItemVarTab(String identif, Type type, int taille){
	this.identif = identif;
	this.adresse = 0;
	this.portee = null;
	this.taille = taille;
	this.isParam = false;
	this.type = type;
    }

    public String toString(){
	    return this.identif + "\tTAB  \t" + this.type + "\t" + this.adresse + "\t" + this.taille;
    }
    
}

