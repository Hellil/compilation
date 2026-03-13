package sa;

public class SaInstTantQue implements SaInst{
    private SaExp test;
    private SaInst faire;

    public SaInstTantQue(SaExp test, SaInst faire){
	this.test = test;
	this.faire = faire;
    }

    public SaExp getTest(){return this.test;}
    public SaInst getFaire(){return this.faire;}

    public <T> T accept(SaVisitor <T> visitor) throws Exception{
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + test + " " +  faire + ")";
    }
}
