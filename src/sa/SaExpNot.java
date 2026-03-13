package sa;
import util.Type;

public class SaExpNot implements SaExp{
    private SaExp op1;

    public SaExpNot(SaExp op1){
	this.op1 = op1;
    }

    public SaExp getOp1(){return this.op1;}
    public SaExp getOp2(){return null;}
    
    public Type getType(){
	return Type.BOOL;
    }

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + op1 + ")";
    }
}
