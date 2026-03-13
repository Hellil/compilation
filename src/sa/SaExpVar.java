package sa;
import util.Type;

public class SaExpVar implements SaExp{
    private SaVar var;

    public SaExpVar(SaVar var){
	this.var = var;
    }

    public SaVar getVar(){return this.var;}

    public Type getType(){
	return this.var.getTsItem().getType();
    }
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + var + ")";
    }
}
