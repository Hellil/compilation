package ts;
import sa.*;
import util.Error;

public class Sa2ts extends SaDepthFirstVisitor <Void> {
    enum Context {
	LOCAL,
	GLOBAL,
	PARAM
    }
    
    private Ts tableGlobale;
    private Ts tableLocaleCourante;
    private Context context;
    
    public Ts getTableGlobale(){return this.tableGlobale;}

    public Sa2ts()
    {
	tableGlobale = new Ts();
	tableLocaleCourante = null;
	context = Context.GLOBAL;
    }

    public void defaultIn(SaNode node)
    {
	//	System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
	//	System.out.println("</" + node.getClass().getSimpleName() + ">");
    }

}
