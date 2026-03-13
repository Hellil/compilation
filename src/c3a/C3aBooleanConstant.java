package c3a;

public class C3aBooleanConstant extends C3aOperand{
    public boolean val;

    public C3aBooleanConstant(boolean val){
	this.val = val;
    }

    public String toString(){return String.valueOf(this.val);}

    public <T> T accept(C3aVisitor <T> visitor) {
        return visitor.visit(this);
    }
}
