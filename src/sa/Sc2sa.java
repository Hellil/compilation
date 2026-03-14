package sa;
import lParser.analysis.DepthFirstAdapter;
import lParser.node.AAddExp3;
import lParser.node.AAf;
import lParser.node.AAffectI;
import lParser.node.AAppfExp6;
import lParser.node.AAppfI;
import lParser.node.ABi;
import lParser.node.ABoolType;
import lParser.node.ADf;
import lParser.node.ADivisExp4;
import lParser.node.AEcrireI;
import lParser.node.AEgalExp2;
import lParser.node.AElemLdf;
import lParser.node.AEps;
import lParser.node.AEtExp1;
import lParser.node.AFauxExp6;
import lParser.node.AIntType;
import lParser.node.ALessExp2;
import lParser.node.AMultiExp4;
import lParser.node.ANextExp;
import lParser.node.ANextExp1;
import lParser.node.ANextExp2;
import lParser.node.ANextExp3;
import lParser.node.ANextExp4;
import lParser.node.ANextExp5;
import lParser.node.ANonExp5;
import lParser.node.ANumExp6;
import lParser.node.AOuExp;
import lParser.node.AParExp6;
import lParser.node.APremLdv;
import lParser.node.APremLe;
import lParser.node.AProgramme;
import lParser.node.ARecurLi;
import lParser.node.ARetourI;
import lParser.node.ASiI;
import lParser.node.ASinonI;
import lParser.node.ASubExp3;
import lParser.node.ASuiteLdvb;
import lParser.node.ASuiteLeb;
import lParser.node.ATabDv;
import lParser.node.ATabV;
import lParser.node.ATqI;
import lParser.node.ATypeTypeopt;
import lParser.node.AVarDv;
import lParser.node.AVarExp6;
import lParser.node.AVarV;
import lParser.node.AVideLdf;
import lParser.node.AVideLdv;
import lParser.node.AVideLdvb;
import lParser.node.AVideLe;
import lParser.node.AVideLeb;
import lParser.node.AVideLi;
import lParser.node.AVideTypeopt;
import lParser.node.AVraiExp6;
import lParser.node.Node;
import util.Type;

public class Sc2sa extends DepthFirstAdapter
{
    private SaNode returnValue;
    private Type returnType;
    private SaProg saRoot = null;

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
	//System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
	//System.out.println("</" + node.getClass().getSimpleName() + ">");
    }

    //TODO:enlever tous les if des méthodes case

    @Override
    public void caseAProgramme(AProgramme node) {
    SaLDecVar op1 = null;
    SaLDecFonc op2 = null;
    
    if(node.getLdv() != null) {
        node.getLdv().apply(this);
        op1 = (SaLDecVar) this.returnValue;
    }
    if(node.getLdf() != null) {
        node.getLdf().apply(this);
        op2 = (SaLDecFonc) this.returnValue;
    }
    
    this.saRoot = new SaProg(op1 , op2) ;
}

    @Override
    public void caseAOuExp(AOuExp node)
    {
        inAOuExp(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getOu() != null)
        {
            node.getOu().apply(this);
        }
        if(node.getExp1() != null)
        {
            node.getExp1().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpOr(op1, op2);
        outAOuExp(node);
    }

    @Override
    public void caseANextExp(ANextExp node)
    {
        inANextExp(node);
        if(node.getExp1() != null)
        {
            node.getExp1().apply(this);
        }
        outANextExp(node);
    }

    @Override
    public void caseAEtExp1(AEtExp1 node)
    {
        inAEtExp1(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp1() != null)
        {
            node.getExp1().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getEt() != null)
        {
            node.getEt().apply(this);
        }
        if(node.getExp2() != null)
        {
            node.getExp2().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpAnd(op1, op2);
        outAEtExp1(node);
    }

    @Override
    public void caseANextExp1(ANextExp1 node)
    {
        inANextExp1(node);
        if(node.getExp2() != null)
        {
            node.getExp2().apply(this);
        }
        outANextExp1(node);
    }

    @Override
    public void caseAEgalExp2(AEgalExp2 node)
    {
        inAEgalExp2(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp2() != null)
        {
            node.getExp2().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getEgal() != null)
        {
            node.getEgal().apply(this);
        }
        if(node.getExp3() != null)
        {
            node.getExp3().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpEqual(op1, op2);
        outAEgalExp2(node);
    }

 public void caseALessExp2(ALessExp2 node) {
    SaExp op1 = null;
    SaExp op2 = null;
    
    if(node.getExp2() != null) {
        node.getExp2().apply(this);
        op1 = (SaExp) this.returnValue;
    }
    if(node.getExp3() != null) {
        node.getExp3().apply(this);
        op2 = (SaExp) this.returnValue;
    }
    this.returnValue = new SaExpInf(op1, op2);
}
    @Override
    public void caseANextExp2(ANextExp2 node)
    {
        inANextExp2(node);
        if(node.getExp3() != null)
        {
            node.getExp3().apply(this);
        }
        outANextExp2(node);
    }

    @Override
    public void caseAAddExp3(AAddExp3 node)
    {
        inAAddExp3(node);
        SaExp op1 = null; //expression 1
        SaExp op2 = null;
        if(node.getExp3() != null)
        {
            node.getExp3().apply(this);
            op1 = (SaExp) this.returnValue; // on récupère l'expression dans Exp3
        }
        if(node.getPlus() != null)
        {
            node.getPlus().apply(this); // inutile ?
        }
        if(node.getExp4() != null)
        {
            node.getExp4().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpAdd(op1, op2); // on utilise le Add du dossier sa
        outAAddExp3(node);
    }

    @Override
    public void caseASubExp3(ASubExp3 node)
    {
        inASubExp3(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp3() != null)
        {
            node.getExp3().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getMoins() != null)
        {
            node.getMoins().apply(this);
        }
        if(node.getExp4() != null)
        {
            node.getExp4().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpSub(op1, op2);
        outASubExp3(node);
    }

    @Override
    public void caseANextExp3(ANextExp3 node)
    {
        inANextExp3(node);
        if(node.getExp4() != null)
        {
            node.getExp4().apply(this);
        }
        outANextExp3(node);
    }

    @Override
    public void caseAMultiExp4(AMultiExp4 node)
    {
        inAMultiExp4(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp4() != null)
        {
            node.getExp4().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getMulti() != null)
        {
            node.getMulti().apply(this);
        }
        if(node.getExp5() != null)
        {
            node.getExp5().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpMult(op1, op2);
        outAMultiExp4(node);
    }

    @Override
    public void caseADivisExp4(ADivisExp4 node)
    {
        inADivisExp4(node);
        SaExp op1 = null;
        SaExp op2 = null;
        if(node.getExp4() != null)
        {
            node.getExp4().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getDivis() != null)
        {
            node.getDivis().apply(this);
        }
        if(node.getExp5() != null)
        {
            node.getExp5().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpDiv(op1, op2);
        outADivisExp4(node);
    }

    @Override
    public void caseANextExp4(ANextExp4 node)
    {
        inANextExp4(node);
        if(node.getExp5() != null)
        {
            node.getExp5().apply(this);
        }
        outANextExp4(node);
    }

    @Override
    public void caseANonExp5(ANonExp5 node)
    {
        defaultIn(node);
        SaExp exp = null ; 
        node.getExp6().apply(this);
        exp = (SaExp) returnValue ; 
        returnValue = new SaExpNot(exp); 
        defaultOut(node);
    }

    @Override
    public void caseANextExp5(ANextExp5 node)
    {
        inANextExp5(node);
        if(node.getExp6() != null)
        {
            node.getExp6().apply(this);
        }
        outANextExp5(node);
    }

    @Override
    public void caseAParExp6(AParExp6 node) // parenthèses
    {
        inAParExp6(node);
        if(node.getPo() != null)
        {
            node.getPo().apply(this);
        }
        if(node.getExp1() != null)
        {
            node.getExp1().apply(this); //TODO: not sure about the code
        }
        if(node.getPf() != null)
        {
            node.getPf().apply(this); // askip rien faire ?
        }
        outAParExp6(node);
    }

    @Override
    public void caseANumExp6(ANumExp6 node)
    {
        inANumExp6(node);
        int entier = 0;
        if(node.getNombre() != null)
        {
            node.getNombre().apply(this);
            entier = Integer.parseInt(node.getNombre().getText());
        }
        this.returnValue = new SaExpInt(entier); //TODO: not sure about the code
        outANumExp6(node);
    }

    @Override
    public void caseAVarExp6(AVarExp6 node)
    {
        inAVarExp6(node);
        if(node.getV() != null)
        {
            node.getV().apply(this);
            this.returnValue = new SaExpVar((SaVar) this.returnValue); // TODO: not sure about the code
        }
        outAVarExp6(node);
    }

    public void caseAAppfExp6(AAppfExp6 node) {
    if(node.getAf() != null) {
        node.getAf().apply(this);
        // returnValue contient déjà SaAppel qui est une SaExp !
    }
}

    @Override
    public void caseAVraiExp6(AVraiExp6 node)
    {
        inAVraiExp6(node);
        if(node.getVrai() != null)
        {
            node.getVrai().apply(this);
        }
        this.returnValue = new SaExpVrai(); // TODO: not sure about the code
        outAVraiExp6(node);
    }

    @Override
    public void caseAFauxExp6(AFauxExp6 node)
    {
        inAFauxExp6(node);
        if(node.getFaux() != null)
        {
            node.getFaux().apply(this);
        }
        this.returnValue = new SaExpFaux(); // TODO: not sure about the code
        outAFauxExp6(node);
    }

    @Override
    public void caseAAffectI(AAffectI node)
    {
        inAAffectI(node);
        SaVar op1 = null;
        SaExp op2 = null;
        if(node.getV() != null)
        {
            node.getV().apply(this);
            op1 = (SaVar) this.returnValue;
        }
        if(node.getEgal() != null)
        {
            node.getEgal().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        if(node.getPv() != null)
        {
            node.getPv().apply(this);
        }
        this.returnValue = new SaInstAffect(op1, op2); // TODO: not sure about the code
        outAAffectI(node);
    }

    @Override
    public void caseATqI(ATqI node) // tant que faire
    {
        inATqI(node);
        SaExp op1 = null;
        SaInstBloc op2 = null;
        if(node.getTq() != null)
        {
            node.getTq().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getFaire() != null)
        {
            node.getFaire().apply(this);
        }
        if(node.getBi() != null)
        {
            node.getBi().apply(this);
            op2 = (SaInstBloc) this.returnValue;
        }
        this.returnValue = new SaInstTantQue(op1, op2);
        outATqI(node);
    }

    @Override
    public void caseASiI(ASiI node) // si alors
    {
        inASiI(node);
        SaExp op1 = null;
        SaInstBloc op2 = null;
        if(node.getSi() != null)
        {
            node.getSi().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getAlors() != null)
        {
            node.getAlors().apply(this);
        }
        if(node.getBi() != null)
        {
            node.getBi().apply(this);
            op2 = (SaInstBloc) this.returnValue;
        }
        this.returnValue = new SaInstSi(op1, op2, null);
        outASiI(node);
    }

    @Override
    public void caseASinonI(ASinonI node)
    {
        inASinonI(node);
        SaExp op1 = null;
        SaInstBloc op2 = null;
        SaInstBloc op3 = null;
        if(node.getSi() != null)
        {
            node.getSi().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getAlors() != null)
        {
            node.getAlors().apply(this);
        }
        if(node.getOui() != null)
        {
            node.getOui().apply(this);
            op2 = (SaInstBloc) this.returnValue;
        }
        if(node.getSinon() != null)
        {
            node.getSinon().apply(this);
        }
        if(node.getNon() != null)
        {
            node.getNon().apply(this);
            op3 = (SaInstBloc) this.returnValue;
        }
        this.returnValue = new SaInstSi(op1, op2, op3);
        outASinonI(node);
    }

    @Override
    public void caseARetourI(ARetourI node) // retour
    {
        inARetourI(node);
        SaExp op1 = null;
        if(node.getRetour() != null)
        {
            node.getRetour().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getPv() != null)
        {
            node.getPv().apply(this); //TODO: not sure about the code
        }
        this.returnValue = new SaInstRetour(op1);
        outARetourI(node);
    }

    @Override
    public void caseAAppfI(AAppfI node) {
        if(node.getAf() != null) {
        node.getAf().apply(this);
        // returnValue contient déjà un SaAppel, c'est suffisant 
    }
}
    

public void caseAEcrireI(AEcrireI node) {
    SaExp exp = null;
    if(node.getExp() != null) {
        node.getExp().apply(this);
        exp = (SaExp) this.returnValue;
    }
    this.returnValue = new SaInstEcriture(exp);
}
   public void caseABi(ABi node) {
    SaLInst l = null;
    
    if(node.getLi() != null) {
        node.getLi().apply(this);
        l = (SaLInst) this.returnValue;
    }
    
    this.returnValue = (l == null)? null : new SaInstBloc(l);
}

   public void caseARecurLi(ARecurLi node) {
    SaLInst queue = null;
    SaInst tete = null;
    
    if(node.getLi() != null) {
        node.getLi().apply(this);
        queue = (SaLInst) this.returnValue;
    }
    if(node.getI() != null) {
        node.getI().apply(this);
        tete = (SaInst) this.returnValue;
    }
    
    this.returnValue = new SaLInst(tete,queue);
}
    @Override
    public void caseAVideLi(AVideLi node)
    {
        inAVideLi(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; 
        outAVideLi(node);
    }

    @Override
    public void caseAEps(AEps node)
    {
        inAEps(node);
        this.returnValue = null; 
        outAEps(node);
    }

    @Override
    public void caseAVarDv(AVarDv node) // variable ?
    {
        inAVarDv(node);
        String op1 = "";
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
            op1 = node.getId().getText();
        }
        this.returnValue = new SaDecVarSimple(op1, this.returnType);
        outAVarDv(node);
    }

    @Override
    public void caseATabDv(ATabDv node) // tableau
    {
        inATabDv(node);
        String op1 = "";
        int op2 = -1;
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
            op1 = node.getId().getText();
            op2 = Integer.parseInt(node.getNombre().getText());
        }
        if(node.getCro() != null)
        {
            node.getCro().apply(this);
        }
        if(node.getNombre() != null)
        {
            node.getNombre().apply(this);
        }
        if(node.getCrf() != null)
        {
            node.getCrf().apply(this); //TODO: not sure about the code
        }
        this.returnValue = new SaDecTab(op1, this.returnType, op2);
        outATabDv(node);
    }

    @Override
    public void caseADf(ADf node) // déclaration fonction ?
    {
        inADf(node);
        Type op1 = null;
        String op2 = null;
        SaLDecVar op3 = null;
        SaLDecVar op4 = null;
        SaInstBloc op5 = null;
        if(node.getTypeopt() != null)
        {
            node.getTypeopt().apply(this);
            op1 = this.returnType;
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
            op2 = node.getId().getText();
        }
        if(node.getPo() != null)
        {
            node.getPo().apply(this);
        }
        if(node.getParam() != null)
        {
            node.getParam().apply(this);
            op3 = (SaLDecVar) this.returnValue;
        }
        if(node.getPf() != null)
        {
            node.getPf().apply(this);
        }
        if(node.getVarloc() != null)
        {
            node.getVarloc().apply(this);
            op4 = (SaLDecVar) this.returnValue;
        }
        if(node.getBi() != null)
        {
            node.getBi().apply(this);
            op5 = (SaInstBloc) this.returnValue; //TODO: not sure about the code
        }
        this.returnValue = new SaDecFonc(op2, op1, op3, op4, op5);
        outADf(node);
    }

    @Override
    public void caseAVideLdv(AVideLdv node)
    {
        inAVideLdv(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; //TODO: not sure about the code
        outAVideLdv(node);
    }
    
    @Override
    public void caseAPremLdv(APremLdv node) // premier élément de liste déclaration variable
    {
        inAPremLdv(node);
        SaDecVar op1 = null;
        SaLDecVar op2 = null;
        if(node.getDv() != null)
        {
            node.getDv().apply(this);
            op1 = (SaDecVar) this.returnValue;
        }
        if(node.getLdvb() != null)
        {
            node.getLdvb().apply(this);
            op2 = (SaLDecVar) this.returnValue;
        }
        this.returnValue = new SaLDecVar(op1, op2);
        outAPremLdv(node);
    }
    
    @Override
    public void caseAVideLdvb(AVideLdvb node)
    {
        inAVideLdvb(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; //TODO: not sure about the code
        outAVideLdvb(node);
    }

    @Override
    public void caseASuiteLdvb(ASuiteLdvb node) // reste éléments après permier de liste ?
    {
        inASuiteLdvb(node);
        SaDecVar op1 = null;
        SaLDecVar op2 = null;
        if(node.getVir() != null)
        {
            node.getVir().apply(this);
        }
        if(node.getDv() != null)
        {
            node.getDv().apply(this);
            op1 = (SaDecVar) this.returnValue;
        }
        if(node.getLdvb() != null)
        {
            node.getLdvb().apply(this);
            op2 = (SaLDecVar) this.returnValue; //TODO: not sure about the code
        }
        this.returnValue = new SaLDecVar(op1, op2);
        outASuiteLdvb(node);
    }

    @Override
    public void caseAVideTypeopt(AVideTypeopt node)
    {
        inAVideTypeopt(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnType = Type.NUL; //TODO: not sure about the code
        outAVideTypeopt(node);
    }

     @Override
    public void caseATypeTypeopt(ATypeTypeopt node) // type ?
    {
        inATypeTypeopt(node);
        if(node.getType() != null)
        {
            node.getType().apply(this); //TODO: not sure about the code
        }
        outATypeTypeopt(node);
    }

    @Override
    public void caseAIntType(AIntType node)
    {
        inAIntType(node);
        if(node.getEntier() != null)
        {
            node.getEntier().apply(this);
            this.returnType = Type.ENTIER; //TODO: not sure about the code
        }
        outAIntType(node);
    }

    @Override
    public void caseABoolType(ABoolType node)
    {
        inABoolType(node);
        if(node.getBool() != null)
        {
            node.getBool().apply(this);
            this.returnType = Type.BOOL; //TODO: not sure about the code
        }
        outABoolType(node);
    }

     @Override
    public void caseAVarV(AVarV node) // variable ?
    {
        inAVarV(node);
        String op1 = "";
        if(node.getId() != null)
        {
            node.getId().apply(this); //TODO: not sure about the code
            op1 = node.getId().getText();
        }
        this.returnValue = new SaVarSimple(op1);
        outAVarV(node);
    }
    @Override
    public void caseATabV(ATabV node) // tableau ?
    {
        inATabV(node);
        String op1 = "";
        SaExp op2 = null;
        if(node.getId() != null)
        {
            node.getId().apply(this);
            op1 = node.getId().getText();
        }
        if(node.getCro() != null)
        {
            node.getCro().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op2 = (SaExp) this.returnValue;
        }
        if(node.getCrf() != null)
        {
            node.getCrf().apply(this); 
        }
        this.returnValue = new SaVarIndicee(op1, op2);
        outATabV(node);
    }


   @Override
    public void caseAAf(AAf node) // appel fonction ?
    {
        inAAf(node);
        String op1 = "";
        SaLExp op2 = null;
        if(node.getId() != null)
        {
            node.getId().apply(this);
            op1 = node.getId().getText();
        }
        if(node.getPo() != null)
        {
            node.getPo().apply(this);
        }
        if(node.getLe() != null)
        {
            node.getLe().apply(this);
            op2 = (SaLExp) this.returnValue;
        }
        if(node.getPf() != null)
        {
            node.getPf().apply(this); //TODO: not sure about the code
        }
        this.returnValue = new SaAppel(op1, op2);
        outAAf(node);
    }

    @Override
    public void caseAVideLe(AVideLe node)
    {
        inAVideLe(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; //TODO: not sure about the code
        outAVideLe(node);
    }

    @Override
    public void caseAPremLe(APremLe node) // premier élément de liste expressions
    {
        inAPremLe(node);
        SaExp op1 = null;
        SaLExp op2 = null;
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getLeb() != null)
        {
            node.getLeb().apply(this);
            op2 = (SaLExp) this.returnValue;
        }
        this.returnValue = new SaLExp(op1, op2);
        outAPremLe(node);
    }

    @Override
    public void caseAVideLeb(AVideLeb node)
    {
        inAVideLeb(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; //TODO: not sure about the code
        outAVideLeb(node);
    }

    @Override
    public void caseASuiteLeb(ASuiteLeb node) // continue liste expression
    {
        inASuiteLeb(node);
        SaExp op1 = null;
        SaLExp op2 = null;
        if(node.getVir() != null)
        {
            node.getVir().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if(node.getLeb() != null)
        {
            node.getLeb().apply(this);
            op2 = (SaLExp) this.returnValue;
        }
        this.returnValue = new SaLExp(op1, op2); //TODO: not sure about the code
        outASuiteLeb(node);
    }

    @Override
    public void caseAVideLdf(AVideLdf node)
    {
        inAVideLdf(node);
        if(node.getEps() != null)
        {
            node.getEps().apply(this);
        }
        this.returnValue = null; //TODO: not sure about the code
        outAVideLdf(node);
    }

     @Override
    public void caseAElemLdf(AElemLdf node) // idk 3
    {
        inAElemLdf(node);
        SaDecFonc op1 = null;
        SaLDecFonc op2 = null;
        if(node.getDf() != null)
        {
            node.getDf().apply(this);
            op1 = (SaDecFonc) this.returnValue;
        }
        if(node.getLdf() != null)
        {
            node.getLdf().apply(this);
            op2 = (SaLDecFonc) this.returnValue; //TODO: not sure about the code
        }
        this.returnValue = new SaLDecFonc(op1, op2);
        outAElemLdf(node);
    }

    public SaProg getRoot()
    {
	return this.saRoot;
    }
}
