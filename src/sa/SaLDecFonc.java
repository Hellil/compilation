package sa;

public class SaLDecFonc implements SaNode{
    private SaDecFonc tete;
    private SaLDecFonc queue;

    public SaLDecFonc(SaDecFonc tete, SaLDecFonc queue){
	this.tete = tete;
	this.queue = queue;
    }

    public SaDecFonc getTete(){return this.tete;}
    public SaLDecFonc getQueue(){return this.queue;}

    public int length(){
	if(this.queue == null) return 1;
	return 1 + this.queue.length() ;
    }
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + tete + " " + queue + ")";
    }

}
