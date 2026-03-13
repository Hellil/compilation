package sa;
import ts.*;

public class SaVarIndicee implements SaVar{
    private String nom;
    private SaExp indice;
    public TsItemVar tsItem;
    
    public SaVarIndicee(String nom, SaExp indice){
	this.nom = nom;
	this.indice = indice;
	this.tsItem = null;
    }

    public TsItemVar getTsItem(){return this.tsItem;}
    public String getNom(){return this.nom;}
    public SaExp getIndice(){return this.indice;}
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + nom + " " + indice +")";
    }
}
