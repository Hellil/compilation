package sa;
import util.Type;

public class SaExpAppel implements SaExp{
    private SaAppel val;

    public SaExpAppel(SaAppel val){
	this.val = val;
    }

    public SaAppel getVal(){return this.val;}

    public Type getType(){
	return val.getType();
    }
    
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + val + ")";
    }
}
