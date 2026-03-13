package sa;

public class SaInstEcriture implements SaInst{
    private SaExp arg;

    public SaInstEcriture(SaExp arg){
	this.arg = arg;
    }

    public SaExp getArg(){return this.arg;}

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + arg + ")";
    }
}
