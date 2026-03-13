package sa;

import util.Error;

public class SaCheckLinkage extends SaDepthFirstVisitor <Void>{

    public Void visit(SaDecFonc node) throws Exception {
        defaultIn(node);
        if (node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaDecFonc");
        if(node.getParametres() != null) node.getParametres().accept(this);
        if(node.getVariable() != null) node.getVariable().accept(this);
        if(node.getCorps() != null) node.getCorps().accept(this);
        defaultOut(node);
        return null;

    }

    public Void visit(SaDecVarSimple node) throws ErrorException {
        if (node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaDecVarSimple");
        return null;
    }

    public Void visit(SaDecTab node) throws ErrorException {
        if(node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaDecTab");
        return null;
    }

    public Void visit(SaAppel node) throws ErrorException{
        if(node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaAppel");
        return null;
    }

    public Void visit(SaVarSimple node) throws ErrorException{
        if(node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaVarSimple");
        return null;
    }
    public Void visit(SaVarIndicee node) throws ErrorException{
        if(node.tsItem == null) throw new ErrorException(Error.TS, "linkage error in SaVarIndicee");
        return null;
    }
}

