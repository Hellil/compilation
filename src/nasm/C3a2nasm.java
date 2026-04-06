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

public class C3a2nasm implements C3aVisitor<NasmOperand> {

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

    private NasmOperand getLabel(C3aInst inst){
        return (inst.label != null) ? inst.label.accept(this) : null;
    }

    public NasmOperand visit(C3a c3a){
        for(C3aInst inst : c3a.listeInst){
            inst.accept(this);
        }
        return null;
    }

    public NasmOperand visit(C3aInstAdd inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), inst.result.accept(this), inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmAdd(null, inst.result.accept(this), inst.op2.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstSub inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), inst.result.accept(this), inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmSub(null, inst.result.accept(this), inst.op2.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstMult inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), inst.result.accept(this), inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmMul(null, inst.result.accept(this), inst.op2.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstDiv inst){
        NasmRegister eax = new NasmRegister(-1);
        eax.colorRegister(Nasm.REG_EAX);

        nasm.ajouteInst(new NasmMov(getLabel(inst), eax, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmDiv(null, inst.op2.accept(this), ""));
        nasm.ajouteInst(new NasmMov(null, inst.result.accept(this), eax, ""));
        return null;
    }

    public NasmOperand visit(C3aInstAffect inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), inst.result.accept(this), inst.op1.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstParam inst){
        nasm.ajouteInst(new NasmPush(getLabel(inst), inst.op1.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstCall inst){
        int nbArgs = inst.op1.val.getNbArgs();

        nasm.ajouteInst(new NasmSub(getLabel(inst), esp, new NasmConstant(4), ""));
        nasm.ajouteInst(new NasmCall(null, inst.op1.accept(this), ""));

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
        if(inst.op1 != null){
            NasmAddress addr = new NasmAddress(
                new NasmExpPlus(ebp, new NasmConstant(8)),
                NasmSize.DWORD);

            NasmRegister r = nasm.newRegister();
            nasm.ajouteInst(new NasmMov(getLabel(inst), r, inst.op1.accept(this), ""));
            nasm.ajouteInst(new NasmMov(null, addr, r, ""));
        }
        return null;
    }

    // ✅ FIX JUMP (result, pas op1)
    public NasmOperand visit(C3aInstJump inst){
        nasm.ajouteInst(new NasmJmp(getLabel(inst),
                inst.result.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstJumpIfEqual inst){
        NasmRegister r = nasm.newRegister();

        nasm.ajouteInst(new NasmMov(getLabel(inst), r, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmCmp(null, r, inst.op2.accept(this), ""));
        nasm.ajouteInst(new NasmJe(null, inst.result.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstJumpIfNotEqual inst){
        NasmRegister r = nasm.newRegister();

        nasm.ajouteInst(new NasmMov(getLabel(inst), r, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmCmp(null, r, inst.op2.accept(this), ""));
        nasm.ajouteInst(new NasmJne(null, inst.result.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstJumpIfLess inst){
        NasmRegister r = nasm.newRegister();

        nasm.ajouteInst(new NasmMov(getLabel(inst), r, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmCmp(null, r, inst.op2.accept(this), ""));
        nasm.ajouteInst(new NasmJl(null, inst.result.accept(this), ""));
        return null;
    }

    public NasmOperand visit(C3aInstFBegin inst){
        currentFct = inst.val;
        int taille = currentFct.getTable().getAdrVarCourante() * 4;

        nasm.ajouteInst(new NasmPush(getLabel(inst), ebp, ""));
        nasm.ajouteInst(new NasmMov(null, ebp, esp, ""));
        nasm.ajouteInst(new NasmSub(null, esp, new NasmConstant(taille), ""));
        return null;
    }

    public NasmOperand visit(C3aInstFEnd inst){
        int taille = currentFct.getTable().getAdrVarCourante() * 4;

        nasm.ajouteInst(new NasmAdd(getLabel(inst), esp, new NasmConstant(taille), ""));
        nasm.ajouteInst(new NasmPop(null, ebp, ""));
        nasm.ajouteInst(new NasmRet(null, ""));
        currentFct = null;
        return null;
    }

    // ✅ OBLIGATOIRES POUR COMPILER

    public NasmOperand visit(C3aInstRead inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), nasm.newEaxRegister(), new NasmLabel("sinput"), ""));
        nasm.ajouteInst(new NasmCall(null, new NasmLabel("readline"), ""));
        nasm.ajouteInst(new NasmCall(null, new NasmLabel("atoi"), ""));
        nasm.ajouteInst(new NasmMov(null, inst.result.accept(this), nasm.newEaxRegister(), ""));
        return null;
    }

    public NasmOperand visit(C3aInstWrite inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), nasm.newEaxRegister(), inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmCall(null, new NasmLabel("iprintLF"), ""));
        return null;
    }

    public NasmOperand visit(C3aInstStop inst){
        nasm.ajouteInst(new NasmMov(getLabel(inst), nasm.newEbxRegister(), new NasmConstant(0), ""));
        nasm.ajouteInst(new NasmMov(null, nasm.newEaxRegister(), new NasmConstant(1), ""));
        nasm.ajouteInst(new NasmInt(null, ""));
        return null;
    }

    public NasmOperand visit(C3aInst inst){ return null; }

    public NasmOperand visit(C3aConstant oper){ return new NasmConstant(oper.val); }
    public NasmOperand visit(C3aBooleanConstant oper){ return new NasmConstant(oper.val ? 1 : 0); }
    public NasmOperand visit(C3aLabel oper){ return new NasmLabel(oper.toString()); }
    public NasmOperand visit(C3aTemp oper){ return new NasmRegister(oper.num); }

    public NasmOperand visit(C3aVar oper){
        return new NasmAddress(new NasmLabel(oper.item.getIdentif()), NasmSize.DWORD);
    }

    public NasmOperand visit(C3aFunction oper){
        return new NasmLabel(oper.val.identif);
    }
}
