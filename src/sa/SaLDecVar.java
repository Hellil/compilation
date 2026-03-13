package sa;

public class SaLDecVar implements SaNode{
    private SaDecVar tete;
    private SaLDecVar queue;

    public SaLDecVar(SaDecVar tete, SaLDecVar queue){
	this.tete = tete;
	this.queue = queue;
    }

    public SaDecVar getTete(){return this.tete;}
    public SaLDecVar getQueue(){return this.queue;}

    public int length(){
	if(this.queue == null) return 1;
	return 1 + this.queue.length() ;
    }
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception {
        return visitor.visit(this);
    }
    public String toString(){
        return "(" + this.getClass().getSimpleName() + " " + tete + " " + queue + ")";
    }

}
