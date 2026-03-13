package ts;
import sa.*;
import util.Type;


public class TsItemFct {
    public String identif;
    public int nbArgs;
    public Ts table;
    public SaDecFonc saDecFonc;
    public Type typeRetour;
    
    public TsItemFct(String identif, Type typeRetour, int nbArgs, Ts table, SaDecFonc saDecFonc){
	this.identif = identif;
	this.nbArgs = nbArgs;
	this.table = table;
	this.saDecFonc = saDecFonc;
	this.typeRetour = typeRetour;
    }

    public int getNbArgs(){return this.nbArgs;}
    public Ts getTable(){return this.table;}
    public Type getTypeRetour(){return this.typeRetour;}
    public String getIdentif(){return this.identif;}
    public String toString(){
    	return this.identif +  "\tFCT\t" + typeRetour + "\t" + this.nbArgs;
    }

}

