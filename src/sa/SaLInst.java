package sa;

public class SaLInst implements SaNode{
    private SaInst tete;
    private SaLInst queue;

    public SaLInst(SaInst tete, SaLInst queue){
	this.tete = tete;
	this.queue = queue;
    }

    public SaInst getTete(){return this.tete;}
    public SaLInst getQueue(){return this.queue;}
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    public String toString(){
        return "(" + this.getClass().getSimpleName() + " " + tete + " " + queue +")";
    }

}
