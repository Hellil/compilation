package nasm;
import java.util.*;
import ts.*;
import c3a.*;

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
    
    public NasmOperand visit(C3a c3a){return null;}
    public NasmOperand visit(C3aInstAdd inst){return null;}
    public NasmOperand visit(C3aInstCall inst){return null;}
    public NasmOperand visit(C3aInstFBegin inst){return null;}
    public NasmOperand visit(C3aInst inst){return null;}
    public NasmOperand visit(C3aInstJumpIfLess inst){return null;}
    public NasmOperand visit(C3aInstMult inst){return null;}
    public NasmOperand visit(C3aInstRead inst){return null;}
    public NasmOperand visit(C3aInstSub inst){return null;}
    public NasmOperand visit(C3aInstAffect inst){return null;}
    public NasmOperand visit(C3aInstDiv inst){return null;}
    public NasmOperand visit(C3aInstFEnd inst){return null;}
    public NasmOperand visit(C3aInstJumpIfEqual inst){return null;}
    public NasmOperand visit(C3aInstJumpIfNotEqual inst){return null;}
    public NasmOperand visit(C3aInstJump inst){return null;}
    public NasmOperand visit(C3aInstParam inst){return null;}
    public NasmOperand visit(C3aInstReturn inst){return null;}
    public NasmOperand visit(C3aInstWrite inst){return null;}
    public NasmOperand visit(C3aInstStop inst){return null;}

    public NasmOperand visit(C3aConstant oper){return null;}
    public NasmOperand visit(C3aBooleanConstant oper){return null;}
    public NasmOperand visit(C3aLabel oper){return null;}
    public NasmOperand visit(C3aTemp oper){return null;}
    public NasmOperand visit(C3aVar oper){return null;}
    public NasmOperand visit(C3aFunction oper){return null;}
    
}
