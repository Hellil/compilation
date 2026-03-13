package nasm;
import java.util.ArrayList;

public interface NasmExp {
    //    public boolean isStackAddress();
    public ArrayList<NasmRegister> getRegisters();
    public ArrayList<NasmRegister> getRegisters(ArrayList<NasmRegister> registerList);


    public <T> T accept(NasmVisitor <T> visitor);
}
