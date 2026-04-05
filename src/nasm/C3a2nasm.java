package nasm;
import c3a.C3a;
import c3a.C3aBooleanConstant;
import c3a.C3aConstant;
import c3a.C3aFunction;
import c3a.C3aInst;
import c3a.C3aInstAdd;
import c3a.C3aInstAffect;
import c3a.C3aInstCall;
import c3a.C3aInstDiv;
import c3a.C3aInstFBegin;
import c3a.C3aInstFEnd;
import c3a.C3aInstJump;
import c3a.C3aInstJumpIfEqual;
import c3a.C3aInstJumpIfLess;
import c3a.C3aInstJumpIfNotEqual;
import c3a.C3aInstMult;
import c3a.C3aInstParam;
import c3a.C3aInstRead;
import c3a.C3aInstReturn;
import c3a.C3aInstStop;
import c3a.C3aInstSub;
import c3a.C3aInstWrite;
import c3a.C3aLabel;
import c3a.C3aTemp;
import c3a.C3aVar;
import c3a.C3aVisitor;
import ts.Ts;
import ts.TsItemFct;
import ts.TsItemVar;

public class C3a2nasm implements C3aVisitor <NasmOperand> {
    private C3a c3a;
    private Nasm nasm;
    private Ts tableGlobale;
    private TsItemFct currentFct;
    private NasmRegister esp;
    private NasmRegister ebp;

    public C3a2nasm(C3a c3a, Ts tableGlobale){
        this.c3a = c3a;
        nasm = new Nasm(tableGlobale);
        nasm.setTempCounter(c3a.getTempCounter());

        this.tableGlobale = tableGlobale;
        this.currentFct = null;

        esp = new NasmRegister(-1);
        esp.colorRegister(Nasm.REG_ESP);

        ebp = new NasmRegister(-1);
        ebp.colorRegister(Nasm.REG_EBP);
    }

    public Nasm getNasm(){return nasm;}

    public NasmOperand getLabelFromC3aInst(C3aInst inst){
        return (inst.label != null) ? inst.label.accept(this) : null;
    }

    public NasmOperand visit(C3a c3a){
        for(C3aInst inst : c3a.listeInst){
            inst.accept(this);
        }
        return null;
    }

    public NasmOperand visit(C3aInstAdd inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmMov(label,
            inst.result.accept(this),
            inst.op1.accept(this), ""));

        nasm.ajouteInst(new NasmAdd(null,
            inst.result.accept(this),
            inst.op2.accept(this), ""));

        return null;
    }

    

    public NasmOperand visit(C3aInstSub inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmMov(label,
            inst.result.accept(this),
            inst.op1.accept(this), ""));

        nasm.ajouteInst(new NasmSub(null,
            inst.result.accept(this),
            inst.op2.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstMult inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmMov(label,
            inst.result.accept(this),
            inst.op1.accept(this), ""));

        nasm.ajouteInst(new NasmMul(null,
            inst.result.accept(this),
            inst.op2.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstDiv inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        NasmRegister eax = new NasmRegister(-1);
        eax.colorRegister(Nasm.REG_EAX);

        nasm.ajouteInst(new NasmMov(label,
            eax,
            inst.op1.accept(this), ""));

        nasm.ajouteInst(new NasmDiv(null,
            inst.op2.accept(this), ""));

        nasm.ajouteInst(new NasmMov(null,
            inst.result.accept(this),
            eax, ""));

        return null;
    }

    public NasmOperand visit(C3aInstAffect inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmMov(label,
            inst.result.accept(this),
            inst.op1.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstParam inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand op = inst.op1.accept(this);

        NasmRegister r1 = nasm.newRegister();
        nasm.ajouteInst(new NasmMov(label, r1, op, ""));
        nasm.ajouteInst(new NasmPush(label, inst.op1.accept(this), ""));

        return null;
    }

    

    public NasmOperand visit(C3aInstCall inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        int nbArgs = inst.op1.val.getNbArgs();

        nasm.ajouteInst(new NasmSub(label, esp, new NasmConstant(4), ""));

        nasm.ajouteInst(new NasmCall(label, inst.op1.accept(this), ""));

        if(inst.result != null){
            nasm.ajouteInst(new NasmPop(null, inst.result.accept(this), ""));
        } else {
            nasm.ajouteInst(new NasmAdd(null, esp, new NasmConstant(4), ""));
        }

        if (nbArgs > 0) {
            nasm.ajouteInst(new NasmAdd(null, esp, new NasmConstant(nbArgs * 4), ""));
        }

        return null;
    }

    public NasmOperand visit(C3aInstReturn inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        if(inst.op1 != null){
            NasmAddress adress = new NasmAddress(
                new NasmExpPlus(ebp, new NasmConstant(8)), NasmSize.DWORD);
            NasmRegister r1 = nasm.newRegister();
            nasm.ajouteInst(new NasmMov(label, r1, inst.op1.accept(this), ";ecriture de la valeur de retour"));
            nasm.ajouteInst(new NasmMov(null, adress, r1, ""));
        }

        return null;
    }

    

    public NasmOperand visit(C3aInstJump inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmJmp(label,
            inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstJumpIfEqual inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand op1 = inst.op1.accept(this);
        NasmOperand op2 = inst.op2.accept(this);
        NasmRegister r1 = nasm.newRegister();
        
        nasm.ajouteInst(new NasmMov(label, r1, op1, ""));
        nasm.ajouteInst(new NasmCmp(null, r1, op2, ""));
        nasm.ajouteInst(new NasmJe(null, inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstJumpIfNotEqual inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand op1 = inst.op1.accept(this);
        NasmOperand op2 = inst.op2.accept(this);
        NasmRegister r1 = nasm.newRegister();
        
        nasm.ajouteInst(new NasmMov(label, r1, op1, ""));
        nasm.ajouteInst(new NasmCmp(null, r1, op2, ""));
        nasm.ajouteInst(new NasmJne(null, inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstJumpIfLess inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand op1 = inst.op1.accept(this);
        NasmOperand op2 = inst.op2.accept(this);
        NasmRegister r1 = nasm.newRegister();

        nasm.ajouteInst(new NasmMov(label, r1, op1, ""));
        nasm.ajouteInst(new NasmCmp(null, r1, op2, ""));
        nasm.ajouteInst(new NasmJl(null, inst.result.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstFBegin inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        int taille = currentFct.getTable().getAdrVarCourante() * 4;
        currentFct = inst.val;

        nasm.ajouteInst(new NasmPush(label, ebp, ""));
        nasm.ajouteInst(new NasmMov(null, ebp, esp, ""));
        nasm.ajouteInst(new NasmSub(null, esp, new NasmConstant(taille), ""));

        return null;
    }

    public NasmOperand visit(C3aInstFEnd inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        int taille = currentFct.getTable().getAdrVarCourante() * 4;

        nasm.ajouteInst(new NasmAdd(label, esp, new NasmConstant(taille), ""));
        nasm.ajouteInst(new NasmPop(null, ebp, ""));
        nasm.ajouteInst(new NasmRet(null, ""));
        currentFct = null;

        return null;
    }

    

    public NasmOperand visit(C3aInstRead inst){
        nasm.ajouteInst(new NasmMov (getLabelFromC3aInst(inst),
        nasm.newEaxRegister(),
        new NasmLabel("sinput"), ""));
        nasm.ajouteInst(new NasmCall(null,
        new NasmLabel("readline"), ""));
        nasm.ajouteInst(new NasmCall(null,
        new NasmLabel("atoi"), ""));
        nasm.ajouteInst(new NasmMov (null,
        inst.result.accept(this),
        nasm.newEaxRegister() , ""));
        return null;
    }

    public NasmOperand visit(C3aInstWrite inst){
        nasm.ajouteInst(new NasmMov (getLabelFromC3aInst(inst),
        nasm.newEaxRegister(),
        inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmCall(null,
        new NasmLabel("iprintLF"), ""));
        return null;
    }

    public NasmOperand visit(C3aInstStop inst){
        NasmOperand label = getLabelFromC3aInst(inst);

        nasm.ajouteInst(new NasmMov(label, nasm.newEbxRegister(), new NasmConstant(0), ""));
        nasm.ajouteInst(new NasmMov(null, nasm.newEaxRegister(), new NasmConstant(1), ""));
        nasm.ajouteInst(new NasmInt(null, ""));
        return null;
    }

    public NasmOperand visit(C3aInst inst){ return null; }

    public NasmOperand visit(C3aConstant oper){
        return new NasmConstant(oper.val);
    }

    public NasmOperand visit(C3aBooleanConstant oper){
        return new NasmConstant(oper.val ? 1 : 0);
    }

    public NasmOperand visit(C3aLabel oper){
        return new NasmLabel(oper.toString());
    }

    public NasmOperand visit(C3aTemp oper){
        return new NasmRegister(oper.num);
    }

    public NasmOperand visit(C3aVar oper){
        TsItemVar var = oper.item;

        // ----------- VARIABLE GLOBALE -----------
        if(var.getPortee() == tableGlobale){

            // Cas tableau : var[index]
            if(oper.index != null){
                NasmOperand indexVal = oper.index.accept(this);

                NasmRegister tmp = nasm.newRegister();
                nasm.ajouteInst(new NasmMov(null, tmp, indexVal, ""));

                // multiplication par 4 (taille d’un int)
                nasm.ajouteInst(new NasmMul(null, tmp, new NasmConstant(4), ""));

                NasmExp addr = new NasmExpPlus(
                    new NasmLabel(var.getIdentif()),
                    tmp
                );

                return new NasmAddress(addr, NasmSize.DWORD);
            }

            // Cas simple : variable globale
            return new NasmAddress(
                new NasmLabel(var.getIdentif()),
                NasmSize.DWORD
            );
        }

        // ----------- VARIABLE LOCALE / PARAM -----------

        // Paramètre de fonction
        if(var.isParam){
            int dep = (currentFct.getNbArgs() * 4) + 8 - var.getAdresse();

            return new NasmAddress(
                new NasmExpPlus(ebp, new NasmConstant(dep)),
                NasmSize.DWORD
            );
        }

        // Variable locale
        int dep = (var.getAdresse() + 1) * 4;

        return new NasmAddress(
            new NasmExpMinus(ebp, new NasmConstant(dep)),
            NasmSize.DWORD
        );
    }

    public NasmOperand visit(C3aFunction oper){
        return new NasmLabel(oper.val.identif);
    }

    
}