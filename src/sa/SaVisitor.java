package sa;

interface SaVisitor <T> {
    public T visit(SaProg node) throws Exception;
    public T visit(SaInstEcriture node) throws Exception;
    public T visit(SaInstTantQue node) throws Exception;
    public T visit(SaInstBloc node) throws Exception;
    public T visit(SaInstSi node) throws Exception;
    public T visit(SaInstAffect node) throws Exception;
    public T visit(SaInstRetour node) throws Exception;
    public T visit(SaLInst node) throws Exception;
    public T visit(SaDecFonc node) throws Exception;
    public T visit(SaDecVar node) throws Exception;
    public T visit(SaDecTab node) throws Exception;
    //    public T visit(SaLDec node) throws Exception;
    public T visit(SaLDecVar node) throws Exception;
    public T visit(SaLDecFonc node) throws Exception;
    public T visit(SaVarSimple node) throws Exception;
    public T visit(SaVarIndicee node) throws Exception;
    public T visit(SaAppel node) throws Exception;
    public T visit(SaExp node) throws Exception;
    public T visit(SaExpLire node) throws Exception;
    public T visit(SaExpInt node) throws Exception;
    public T visit(SaExpVrai node) throws Exception;
    public T visit(SaExpFaux node) throws Exception;
    public T visit(SaExpVar node) throws Exception;
    public T visit(SaExpAppel node) throws Exception;
    public T visit(SaExpAdd node) throws Exception;
    public T visit(SaExpSub node) throws Exception;
    public T visit(SaExpMult node) throws Exception;
    public T visit(SaExpDiv node) throws Exception;
    public T visit(SaExpInf node) throws Exception;
    public T visit(SaExpEqual node) throws Exception;
    public T visit(SaExpAnd node) throws Exception;
    public T visit(SaExpOr node) throws Exception;
    public T visit(SaExpNot node) throws Exception;
    public T visit(SaLExp node) throws Exception;
}
