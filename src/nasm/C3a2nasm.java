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

        nasm.ajouteInst(new NasmMov(label,
            inst.result.accept(this),
            inst.op1.accept(this), ""));

        nasm.ajouteInst(new NasmDiv(null,
            inst.result.accept(this),
            inst.op2.accept(this), ""));

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

        nasm.ajouteInst(new NasmPush(label,
            inst.op1.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstCall inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmCall(label,
            inst.op1.accept(this), ""));

        if(inst.result != null){
            NasmRegister eax = new NasmRegister(-1);
            eax.colorRegister(Nasm.REG_EAX);

            nasm.ajouteInst(new NasmMov(null,
                inst.result.accept(this),
                eax, ""));
        }

        return null;
    }

    public NasmOperand visit(C3aInstReturn inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        if(inst.op1 != null){
            NasmRegister eax = new NasmRegister(-1);
            eax.colorRegister(Nasm.REG_EAX);

            nasm.ajouteInst(new NasmMov(label,
                eax,
                inst.op1.accept(this), ""));
        }

        nasm.ajouteInst(new NasmRet(null, ""));
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

        nasm.ajouteInst(new NasmCmp(label,
            inst.op1.accept(this),
            inst.op2.accept(this), ""));

        nasm.ajouteInst(new NasmJe(null,
            inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstJumpIfNotEqual inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmCmp(label,
            inst.op1.accept(this),
            inst.op2.accept(this), ""));

        nasm.ajouteInst(new NasmJne(null,
            inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstJumpIfLess inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmCmp(label,
            inst.op1.accept(this),
            inst.op2.accept(this), ""));

        nasm.ajouteInst(new NasmJl(null,
            inst.label.accept(this), ""));

        return null;
    }

    public NasmOperand visit(C3aInstFBegin inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        currentFct = inst.val;

        nasm.ajouteInst(new NasmPush(label, ebp, ""));
        nasm.ajouteInst(new NasmMov(null, ebp, esp, ""));

        return null;
    }

    public NasmOperand visit(C3aInstFEnd inst){
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        nasm.ajouteInst(new NasmLeave(label, ""));
        nasm.ajouteInst(new NasmRet(null, ""));

        return null;
    }

    public NasmOperand visit(C3aInstRead inst){ return null; }
    public NasmOperand visit(C3aInstWrite inst){ return null; }
    public NasmOperand visit(C3aInstStop inst){ return null; }
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
        return new NasmAddress(oper.tsItem);
    }

    public NasmOperand visit(C3aFunction oper){
        return new NasmLabel(oper.val.identif);
    }
}