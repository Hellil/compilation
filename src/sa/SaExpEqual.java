package sa;
import util.Type;

public class SaExpEqual implements SaExp{
    private SaExp op1;
    private SaExp op2;

    public SaExpEqual(SaExp op1, SaExp op2){
	this.op1 = op1;
	this.op2 = op2;
    }

    public SaExp getOp1(){return this.op1;}
    public SaExp getOp2(){return this.op2;}
    
    public Type getType(){
	return Type.BOOL;
    }
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + op1 + " " + op2 + ")";
    }
}
