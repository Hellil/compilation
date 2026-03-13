package sa;
import ts.*;

public class SaVarSimple implements SaVar{
    public String nom;
    public TsItemVarSimple tsItem;

    public SaVarSimple(String nom){
	this.nom = nom;
	this.tsItem = null;
    }

    public String getNom(){return this.nom;}
    public TsItemVarSimple getTsItem(){return this.tsItem;}
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName()+ " " + nom + ")";
    }
}
