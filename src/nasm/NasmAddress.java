package nasm;

public class NasmAddress extends NasmOperand {

    public NasmExp val;
    public NasmSize size;

    public NasmAddress(NasmExp val, NasmSize size){
    this.val = val;
    this.size = size;
    }

    public String toString(){
        return this.size.toString() + " [" + this.val.toString() + "]";
    }

    public boolean isGeneralRegister(){
	return false;
    }

    public <T> T accept(NasmVisitor <T> visitor) {
        return visitor.visit(this);
	}


}
