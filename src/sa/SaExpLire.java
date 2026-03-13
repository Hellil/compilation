package sa;
import util.Type;

public class SaExpLire implements SaExp{

    public SaExpLire(){
    }

    public Type getType(){
	return Type.ENTIER;
    }

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
