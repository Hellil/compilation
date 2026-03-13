package sa;
import util.Type;

public class SaExpInt implements SaExp{
    private int val;

    public SaExpInt(int val){
	this.val = val;
    }

    public int getVal(){
	return this.val;
    }
    
    public Type getType(){
	return Type.ENTIER;
    }
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
