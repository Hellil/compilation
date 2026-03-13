package sa;
import ts.*;
import util.Type;


public class SaAppel implements SaExp, SaInst{
    private String nom;
    private SaLExp arguments;
    public TsItemFct tsItem;

    public SaAppel(String nom, SaLExp arguments){
	this.nom = nom;
	this.arguments = arguments;
	this.tsItem = null;
    }

    public String getNom(){return this.nom;}
    public SaLExp getArguments(){return this.arguments;}


    public Type getType(){
	return tsItem.typeRetour;
    }

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName()+ " " + nom + " " + arguments + ")";
    }
}
