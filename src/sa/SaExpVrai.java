package sa;
import util.Type;

public class SaExpVrai implements SaExp{
    private final boolean val;

    public SaExpVrai(){
	this.val = true;
    }

    public boolean getVal(){
	return this.val;
    }
    
    public Type getType(){
	return Type.BOOL;
    }

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "vrai";
    }
}
