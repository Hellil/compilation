package sa;
import ts.*;
import util.Type;

public class SaDecTab implements SaDecVar{
    private String       nom;
    private int          taille;
    private Type         type;
    public  TsItemVar    tsItem;

    public SaDecTab(String nom, Type type, int taille){
	this.nom = nom;
	this.type = type;
	this.taille = taille;
	this.tsItem = null;
    }

    public String       getNom(){return this.nom;}
    public int          getTaille(){return this.taille;}
    public Type         getType(){return this.type;}
    public TsItemVar    getTsItem(){return this.tsItem;}
    public void         setTsItem(TsItemVar tsItem){this.tsItem = tsItem;}
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + nom + " " + type + " " + taille + ")";
    }
}
