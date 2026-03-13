package sa;
import ts.*;
import util.Type;

public class SaDecFonc implements SaDec{
    private String    nom;
    private SaLDecVar parametres;
    private SaLDecVar variables;
    private SaInst    corps;
    private Type      typeRetour;
    public  TsItemFct tsItem;

    public SaDecFonc(String nom, Type typeRetour, SaLDecVar parametres, SaLDecVar variables, SaInst corps){
	this.nom = nom;
	this.typeRetour = typeRetour;
	this.parametres = parametres;
	this.variables = variables;
	this.corps = corps;
	this.tsItem = null;
    }

    public String    getNom(){return this.nom;}
    public Type      getTypeRetour(){return this.typeRetour;}
    public SaLDecVar getParametres(){return this.parametres;}
    public SaLDecVar getVariable(){return this.variables;}
    public SaInst    getCorps(){return this.corps;}
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }
    public String toString(){
        return "(" + this.getClass().getSimpleName() + " " + nom + " " + typeRetour + " " + parametres + " " + variables + " " + corps + ")";
    }
}
