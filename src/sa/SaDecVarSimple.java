package sa;
import ts.*;
import util.Type;

public class SaDecVarSimple implements SaDecVar{
    private String          nom;
    private Type            type;
    public  TsItemVar       tsItem;

    public SaDecVarSimple(String nom, Type type){
	this.nom = nom;
	this.type = type;
	this.tsItem = null;
    }

    public String          getNom(){return this.nom;}
    public Type            getType(){return this.type;}
    public TsItemVar       getTsItem(){return this.tsItem;}
    public void            setTsItem(TsItemVar tsItem){this.tsItem = tsItem;}

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + nom + " " + type + ")";
    }
}
