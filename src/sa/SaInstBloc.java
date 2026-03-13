package sa;

public class SaInstBloc implements SaInst{
    private SaLInst val;

    public SaInstBloc(SaLInst val){
	this.val = val;
    }

    public SaLInst getVal(){return this.val;}
    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + val + ")";
    }
}
