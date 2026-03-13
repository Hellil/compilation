package sa;


// P -> LDEC LDEC 

// DEC -> var id taille 
// DEC -> fct id LDEC LDEC LINST 
// DEC -> var id 

// LDEC -> DEC LDEC 
// LDEC -> null 

// VAR  ->simple id 
// VAR  ->indicee id EXP

// LINST -> INST LINST 
// LINST -> null 

// INST -> aff VAR EXP 
// INST -> si EXP LINST LINST 
// INST -> tq EXP LINST 
// INST -> app APP 
// INST -> ret EXP 
// INST -> ecr EXP 

// APP -> id LEXP 

// LEXP -> EXP LEXP 
// LEXP -> null 

// EXP -> op2 EXP EXP 
// EXP -> op1 EXP 
// EXP -> VAR 
// EXP -> entier 
// EXP -> APP 
// EXP -> lire


//**********

// VAR  ->simple id 
// VAR  ->indicee id EXP

// LINST -> INST LINST 
// LINST -> null 

// INST -> aff VAR EXP 
// INST -> si EXP LINST LINST 
// INST -> tq EXP LINST 
// INST -> app APP 
// INST -> ecr EXP 

// APP -> id LEXP 

// LEXP -> EXP LEXP 
// LEXP -> null 


// EXP -> op1 EXP 
// EXP -> VAR 

// EXP -> lire


public class SaDepthFirstVisitor <T> implements SaVisitor <T>{
    
    //   private NouvelleClasse x;
    public void defaultIn(SaNode node)
    {
    }

    public void defaultOut(SaNode node)
    {
    }

    // P -> LDEC LDEC 
    public T visit(SaProg node) throws Exception
    {
	defaultIn(node);
	if(node.getVariables() != null)
	    node.getVariables().accept(this);
	if(node.getFonctions() != null)
	    node.getFonctions().accept(this);
	defaultOut(node);
	return null;
    }
    
    // DEC -> var id taille 
    public T visit(SaDecTab node) throws Exception{
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
    
    // EXP -> entier
    public T visit(SaExpInt node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    // EXP -> vrai
    public T visit(SaExpVrai node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    // EXP -> faux
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

    // DEC -> fct id LDEC LDEC LINST 
    public T visit(SaDecFonc node) throws Exception
    {
	defaultIn(node);
	if(node.getParametres() != null) node.getParametres().accept(this);
	if(node.getVariable() != null) node.getVariable().accept(this);
	if(node.getCorps() != null) node.getCorps().accept(this);
	defaultOut(node);
	return null;
    }
    
    // DEC -> var id 
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
    
    // LDEC -> DEC LDEC 
    // LDEC -> null 
    /*    public T visit(SaLDec node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
	}*/
    
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

    // EXP -> add EXP EXP
    public T visit(SaExpAdd node) throws Exception
    {
	defaultIn(node);
	node.getOp1().accept(this);
	node.getOp2().accept(this);
	defaultOut(node);
	return null;
    }

    // EXP -> sub EXP EXP
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
    public T visit(SaVarIndicee node) throws Exception
    {
	defaultIn(node);
	node.getIndice().accept(this);
	defaultOut(node);
	return null;
    }
    
}
