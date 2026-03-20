package ts;
import sa.ErrorException;
import sa.SaAppel;
import sa.SaDecFonc;
import sa.SaDecTab;
import sa.SaDecVar;
import sa.SaDepthFirstVisitor;
import sa.SaNode;
import sa.SaVarIndicee;
import sa.SaVarSimple;
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

    public Sa2ts(SaNode root) // param dans CM mais pas dans TP
    {
        tableGlobale = new Ts();
        tableLocaleCourante = null;
        context = Context.GLOBAL;

        // suite dans le CM mais pas dans TP
        try{
            root.accept(this);
            if(tableGlobale.getFct("main") == null)
                throw new ErrorException(Error.TS, "la fonction main n'existe pas");
        }
        catch(ErrorException e){
            System.err.print("ERREUR TABLE DES SYMBOLES : ");
            System.err.println(e.getMessage());
            System.exit(e.getCode());
        }
        catch(Exception e){}
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
        Ts table = (tableLocaleCourante != null)? tableLocaleCourante : tableGlobale;
        TsItemVar item = null;
        if(context == Context.GLOBAL){ // si globale et decl var, vérifie si existe déja, sinon crée (rechDecl)
            if (tableGlobale.getVar(node.getNom()) == null){
                item = tableGlobale.addVar(node.getNom(), node.getType());
            }
        }
        else if(context == Context.LOCAL){ // si locale et decl var, vérifie si existe déja, sinon crée (rechDecl)
            if (tableLocaleCourante.getVar(node.getNom()) == null){
                item = tableLocaleCourante.addVar(node.getNom(), node.getType());
            }
        }
        // TsItemVar item = rechercheDeclarative(node.getNom()); 
        if(item == null || item.portee != table){
            if(context == Context.PARAM)
                node.setTsItem(table.addParam(node.getNom(), node.getType()));
            else
                node.setTsItem(table.addVar(node.getNom(), node.getType()));
        }
        else{
            throw new ErrorException(Error.TS, "il existe déjà une variable " + node.getNom());
        }
        /* old code
        if(context == Context.GLOBAL){
            tableGlobale.addVar(node.getNom(), node.getType());
        }
        else{
            tableLocaleCourante.addVar(node.getNom(), node.getType());
        }
        */
        defaultOut(node);
        return null;
    }

    /*
    

    */

    public Void visit(SaDecTab node) throws Exception{
        defaultIn(node);
        Ts table = (tableLocaleCourante != null)? tableLocaleCourante : tableGlobale;
        TsItemVar item = null;
        if(context == Context.GLOBAL){ // si globale et decl var, vérifie si existe déja, sinon crée (rechDecl)
            if (tableGlobale.getVar(node.getNom()) == null){
                item = tableGlobale.addTab(node.getNom(), node.getType(), node.getTaille());
            }
        }
        else if(context == Context.LOCAL){ // si locale et decl var, vérifie si existe déja, sinon crée (rechDecl)
            if (tableLocaleCourante.getVar(node.getNom()) == null){
                item = tableLocaleCourante.addTab(node.getNom(), node.getType(), node.getTaille());
            }
        }
        // TsItemVar item = rechercheDeclarative(node.getNom()); 
        if(item == null || item.portee != table){ // TODO: code ok pour var, peut etre pas pour tab
            if(context == Context.PARAM)
                node.setTsItem(table.addParam(node.getNom(), node.getType()));
            else
                node.setTsItem(table.addTab(node.getNom(), node.getType(), node.getTaille()));
        } // TODO: fin du todo
        else{
            throw new ErrorException(Error.TS, "il existe déjà une variable " + node.getNom());
        }
        /* old code
        if(context == Context.GLOBAL){
            tableGlobale.addTab(node.getNom(), node.getType(), node.getTaille());
        }
        else{
            tableLocaleCourante.addTab(node.getNom(), node.getType(), node.getTaille());
        }
        */
        defaultOut(node);
        return null;
    }

    public Void visit(SaDecFonc node) throws Exception
    {
        defaultIn(node);
        Ts tableLocale = new Ts();
        TsItemFct item = tableGlobale.addFct(node.getNom(), node.getTypeRetour(), node.getParametres().length(), tableLocale, node);
        node.tsItem = item;
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
        node.tsItem = (TsItemVarSimple) var;
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
        node.tsItem = tab;
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

        node.tsItem = fct;

        int argLen = 0;
        if(node.getArguments() != null) node.getArguments().accept(this);
        argLen = node.getArguments().length();
        if(argLen != fct.getNbArgs()){
            throw new ErrorException(Error.TS, "nombre d'arguments incorrect pour la fonction " + node.getNom());
        }
        
        defaultOut(node);
        return null;
    }

}
