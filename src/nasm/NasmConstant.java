package nasm;
import java.util.ArrayList;

public class NasmConstant extends NasmOperand implements NasmExp{
    public int val;
    public NasmConstant(int val){
	this.val = val;
    }

    public String toString(){
	return Integer.toString(this.val);
    }

    public <T> T accept(NasmVisitor <T> visitor) {
        return visitor.visit(this);
    }

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
