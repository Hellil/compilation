package nasm;
import java.util.ArrayList;

public class NasmLabel extends NasmOperand implements NasmExp {
    public String val;

    public NasmLabel(String val){
	this.val = val;
    }
    public String toString(){
	return this.val;
    }
    public <T> T accept(NasmVisitor <T> visitor) {
        return visitor.visit(this);
    }
    public boolean isStackAddress(){return false;}

        public ArrayList<NasmRegister> getRegisters()
    {
	ArrayList<NasmRegister> registerList = new ArrayList<NasmRegister>();
	this.getRegisters(registerList);
	return registerList;
    }
    public ArrayList<NasmRegister> getRegisters(ArrayList<NasmRegister> registerList)
    {
	return registerList;
    }
}
