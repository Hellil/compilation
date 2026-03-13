package sa;
import util.Type;

public class SaInstRetour implements SaInst{
    private SaExp val;

    public SaInstRetour(SaExp val){
	this.val = val;
    }

    public Type getType(){
	return val.getType();
    }
    public SaExp getVal(){return this.val;}

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + val + ")";
    }
}
