package ts;
import sa.SaAppel;
import sa.SaDecFonc;
import sa.SaDecTab;
import sa.SaDecVar;
import sa.SaDepthFirstVisitor;
import sa.SaNode;
import sa.SaVarIndicee;
import sa.SaVarSimple;

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

    public Void visit(SaDecVar node) throws Exception
    {
        defaultIn(node);
        if(context == Context.GLOBAL){
            tableGlobale.addVar(node.getNom(), node.getType());
        }
        else{
            tableLocaleCourante.addVar(node.getNom(), node.getType());
        }
        defaultOut(node);
        return null;
    }

    public Void visit(SaDecTab node) throws Exception{
        defaultIn(node);
        if(context == Context.GLOBAL){
            tableGlobale.addTab(node.getNom(), node.getType(), node.getTaille());
        }
        else{
            tableLocaleCourante.addTab(node.getNom(), node.getType(), node.getTaille());
        }
        defaultOut(node);
        return null;
    }

    public Void visit(SaDecFonc node) throws Exception
    {
        defaultIn(node);
        Ts tableLocale = new Ts();
        tableGlobale.addFct(node.getNom(), node.getTypeRetour(), node.getParametres().length(), tableLocale, node);
        tableLocaleCourante = tableLocale;
        context = Context.PARAM;
        if(node.getParametres() != null) node.getParametres().accept(this);
        context = Context.LOCAL;
        if(node.getVariable() != null) node.getVariable().accept(this);
        if(node.getCorps() != null) node.getCorps().accept(this);
        context = Context.GLOBAL;
        tableLocaleCourante = null;
        defaultOut(node);
        return null;
    }

    public Void visit(SaVarSimple node) throws Exception
    {
        defaultIn(node);
        TsItemVar var = null;

        if(context != Context.GLOBAL && tableLocaleCourante != null){
            var = tableLocaleCourante.getVar(node.getNom());
        }

        if(var == null){
            var = tableGlobale.getVar(node.getNom());
        }

        if(var == null){
            throw new Exception("Variable non déclarée : " + node.getNom());
        }

        defaultOut(node);
        return null;
    }

    public Void visit(SaVarIndicee node) throws Exception
    {
        defaultIn(node);
        TsItemVar tab = null;

        if(context != Context.GLOBAL && tableLocaleCourante != null){
            tab = tableLocaleCourante.getVar(node.getNom());
        }

        if(tab == null){
            tab = tableGlobale.getVar(node.getNom());
        }

        if(tab == null){
            throw new Exception("Tableau non déclaré : " + node.getNom());
        }

        node.getIndice().accept(this);
        defaultOut(node);
        return null;
    }

    public Void visit(SaAppel node) throws Exception
    {
        defaultIn(node);
        TsItemFct fct = tableGlobale.getFct(node.getNom());

        if(fct == null){
            throw new Exception("Fonction non déclarée : " + node.getNom());
        }

        if(node.getArguments() != null) node.getArguments().accept(this);
        defaultOut(node);
        return null;
    }

}
