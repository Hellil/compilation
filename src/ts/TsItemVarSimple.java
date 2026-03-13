package ts;
import util.Type;

public class TsItemVarSimple extends TsItemVar{
    public TsItemVarSimple(String identif, Type type){
	this.identif = identif;
	this.adresse = 0;
	this.portee =  null;
	this.taille =  1;
	this.isParam = false;
	this.type =    type;
    }

    public String toString(){
	if(this.isParam)
	    return this.identif + "\tPARAM\t" + this.type + "\t" + this.adresse;
	else
	    return this.identif + "\tVAR  \t" + this.type + "\t" + this.adresse;
    }
    
}

