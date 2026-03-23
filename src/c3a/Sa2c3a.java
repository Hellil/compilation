package c3a;
import sa.SaAppel;
import sa.SaDecFonc;
import sa.SaDecTab;
import sa.SaDecVar;
import sa.SaDepthFirstVisitor;
import sa.SaExp;
import sa.SaExpAdd;
import sa.SaExpAnd;
import sa.SaExpAppel;
import sa.SaExpDiv;
import sa.SaExpEqual;
import sa.SaExpFaux;
import sa.SaExpInf;
import sa.SaExpInt;
import sa.SaExpLire;
import sa.SaExpMult;
import sa.SaExpNot;
import sa.SaExpOr;
import sa.SaExpSub;
import sa.SaExpVar;
import sa.SaExpVrai;
import sa.SaInstAffect;
import sa.SaInstBloc;
import sa.SaInstEcriture;
import sa.SaInstRetour;
import sa.SaInstSi;
import sa.SaInstTantQue;
import sa.SaLDecFonc;
import sa.SaLDecVar;
import sa.SaLExp;
import sa.SaLInst;
import sa.SaNode;
import sa.SaProg;
import sa.SaVarSimple;
import ts.Ts;

public class Sa2c3a extends SaDepthFirstVisitor <C3aOperand> {
    private C3a c3a;
    int indentation;
    public C3a getC3a(){return this.c3a;}
    
    public Sa2c3a(SaNode root, Ts tableGlobale){
	c3a = new C3a();
	C3aTemp result = c3a.newTemp();
	C3aFunction fct = new C3aFunction(tableGlobale.getFct("main"));
	c3a.ajouteInst(new C3aInstCall(fct, result, ""));
	c3a.ajouteInst(new C3aInstStop(result, ""));
	indentation = 0;

    }

    public void defaultIn(SaNode node)
    {
	//for(int i = 0; i < indentation; i++){System.out.print(" ");}
	//indentation++;
	//System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
	//indentation--;
	//	for(int i = 0; i < indentation; i++){System.out.print(" ");}
	//	System.out.println("</" + node.getClass().getSimpleName() + ">");
    }
    
	public C3aOperand visit(SaProg node) throws Exception
    {
	defaultIn(node);
	if(node.getVariables() != null)
	    node.getVariables().accept(this);
	if(node.getFonctions() != null)
	    node.getFonctions().accept(this);
	defaultOut(node);
	return null;
    }
    
    public C3aOperand visit(SaDecTab node) throws Exception{
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaExp node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaExpInt node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaExpVrai node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaExpFaux node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaExpVar node) throws Exception
    {
	defaultIn(node);
	node.getVar().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaInstEcriture node) throws Exception
    {
	defaultIn(node);
	node.getArg().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaInstTantQue node) throws Exception
    {
	defaultIn(node);
	node.getTest().accept(this);
	if (node.getFaire() != null)
  	node.getFaire().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaLInst node) throws Exception
    {
	defaultIn(node);
	if(node != null){
	    if(node.getTete() != null)node.getTete().accept(this);
	    if(node.getQueue() != null) node.getQueue().accept(this);
	}
	defaultOut(node);
	return null;
    }

	public T visit(SaDecFonc node) throws Exception
    {
	defaultIn(node);
	if(node.getParametres() != null) node.getParametres().accept(this);
	if(node.getVariable() != null) node.getVariable().accept(this);
	if(node.getCorps() != null) node.getCorps().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaDecVar node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

	public T visit(SaInstAffect node) throws Exception
    {
	defaultIn(node);
	node.getLhs().accept(this);
	node.getRhs().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaLDecVar node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
    }

	public T visit(SaLDecFonc node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
    }
    
    public T visit(SaVarSimple node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    public T visit(SaAppel node) throws Exception
    {
	defaultIn(node);
	if(node.getArguments() != null) node.getArguments().accept(this);
	defaultOut(node);
	return null;
    }
    
    public T visit(SaExpAppel node) throws Exception
    {
	defaultIn(node);
	node.getVal().accept(this);
	defaultOut(node);
	return null;
    }

    public C3aOperand visit(SaExpAdd node) throws Exception // apparement déjà correct
    {
	defaultIn(node);
	C3aOperand op1 = node.getOp1().accept(this);
	C3aOperand op2 = node.getOp2().accept(this);
	C3aOperand result = c3a.newTemp();

	c3a.ajouteInst(new C3aInstAdd(op1, op2, result, ""));
	defaultOut(node);
	return result;
    }

	public T visit(SaExpSub node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> mult EXP EXP
    public T visit(SaExpMult node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> div EXP EXP
    public T visit(SaExpDiv node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }
    
    // EXP -> inf EXP EXP
    public T visit(SaExpInf node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> eq EXP EXP
    public T visit(SaExpEqual node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> and EXP EXP
    public T visit(SaExpAnd node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }
    

    // EXP -> or EXP EXP
    public T visit(SaExpOr node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> not EXP
    public T visit(SaExpNot node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	defaultOut(node);
	return null;
    }


    public T visit(SaExpLire node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

    public T visit(SaInstBloc node) throws Exception
    {
	defaultIn(node);
	if ( node.getVal() != null )
	    {
		node.getVal().accept(this);
	    }
	defaultOut(node);
	return null;
    }
    
    public T visit(SaInstSi node) throws Exception
    {
	defaultIn(node);
	node.getTest().accept(this);
	if (node.getAlors() != null)
  	node.getAlors().accept(this);
	if(node.getSinon() != null) node.getSinon().accept(this);
	defaultOut(node);
	return null;
    }

// INST -> ret EXP 
    public T visit(SaInstRetour node) throws Exception
    {
	defaultIn(node);
	node.getVal().accept(this);
	defaultOut(node);
	return null;
    }

    
    public T visit(SaLExp node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null)
	    node.getQueue().accept(this);
	defaultOut(node);
	return null;
    }

	// tous exp et inst = à faire
	// tous liste L devrait etre rien à faire
}
