package sa;
import util.Type;
import util.Error;
import ts.*;

public class SaTypeCheck extends SaDepthFirstVisitor <Void>{
    private TsItemFct fonctionCourante;

    public SaTypeCheck(SaNode root)
    {
		try{
			root.accept(this);
		} catch(ErrorException e){
			System.err.print("ERREUR DE TYPAGE : ");
			System.err.println(e.getMessage());
			System.exit(e.getCode());
		} catch(Exception e){}
	}

    public void defaultIn(SaNode node)
    {
		//			System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
		//		System.out.println("</" + node.getClass().getSimpleName() + ">");
    }

    public Void visit(SaExpAdd node) throws Exception
    {
		defaultIn(node);
		node.getOp1().accept(this);
		node.getOp2().accept(this);
		if(!Type.checkCompatibility(node.getOp1().getType(), Type.ENTIER) ||
				!Type.checkCompatibility(node.getOp2().getType(),Type.ENTIER))
			throw new ErrorException(Error.TYPE, "exp addition");
		defaultOut(node);
		return null;
    }

}
