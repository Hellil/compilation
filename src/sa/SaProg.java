package sa;

public class SaProg implements SaNode{
    private SaLDecVar variables;
    private SaLDecFonc fonctions;

    public SaProg(SaLDecVar variables, SaLDecFonc fonctions){
	this.variables = variables;
	this.fonctions = fonctions;
    }

    public SaLDecVar getVariables(){return this.variables;}
    public SaLDecFonc getFonctions(){return this.fonctions;}
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }
    public String toString(){
        return "(" + this.getClass().getSimpleName() + " " + variables  + " " + fonctions +")";
    }
}
