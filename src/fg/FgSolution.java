package fg;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import nasm.Nasm;
import nasm.NasmAddress;
import nasm.NasmExpMinus;
import nasm.NasmExpPlus;
import nasm.NasmExpTimes;
import nasm.NasmInst;
import nasm.NasmRegister;
import util.graph.Node;
import util.graph.NodeList;
import util.intset.IntSet;

public class FgSolution{
    int iterNum = 0;
    public Nasm nasm;
    Fg fg;
    public Map<NasmInst, IntSet> use;
    public Map<NasmInst, IntSet> def;
    public Map<NasmInst, IntSet> in;
    public Map<NasmInst, IntSet> out;

    public FgSolution(Nasm nasm, Fg fg){
        this.nasm = nasm;
        this.fg = fg;
        this.use = new HashMap<>();
        this.def = new HashMap<>();
        this.in = new HashMap<>();
        this.out = new HashMap<>();

        int size = nasm.getTempCounter();

        // Initialisation use / def / in / out
        for(NasmInst inst : nasm.sectionText){
            use.put(inst, new IntSet(size));
            def.put(inst, new IntSet(size));
            in.put(inst, new IntSet(size));
            out.put(inst, new IntSet(size));

            extractUseDef(inst.source, inst.srcUse, inst.srcDef, use.get(inst), def.get(inst));
            extractUseDef(inst.destination, inst.destUse, inst.destDef, use.get(inst), def.get(inst));
        }

        boolean changed = true;

        while(changed) {
            changed = false;
            iterNum++;

            // ✅ PARCOURS BACKWARD (IMPORTANT)
            for(int i = nasm.sectionText.size() - 1; i >= 0; i--) {
                NasmInst inst = nasm.sectionText.get(i);

                Node n = fg.inst2Node.get(inst);

                IntSet oldIn = in.get(inst).copy();
                IntSet oldOut = out.get(inst).copy();

                // --- calcul OUT ---
                IntSet newOut = new IntSet(size);

                for(NodeList nList = n.succ(); nList != null; nList = nList.tail) {
                    Node succNode = nList.head;
                    NasmInst succInst = fg.node2Inst.get(succNode);

                    if (succInst != null) {
                        newOut.union(in.get(succInst));
                    }
                }

                out.put(inst, newOut);

                // --- calcul IN ---
                IntSet newIn = use.get(inst).copy();

                IntSet temp = out.get(inst).copy();
                temp.minus(def.get(inst));

                newIn.union(temp);

                in.put(inst, newIn);

                // --- test convergence ---
                if(!oldIn.equal(in.get(inst)) || !oldOut.equal(out.get(inst))) {
                    changed = true;
                }
            }
        }
    }

    private void extractUseDef(Object oper, boolean isUse, boolean isDef, IntSet useSet, IntSet defSet) {
        if (oper == null) return;

        if (oper instanceof NasmRegister) {
            NasmRegister r = (NasmRegister) oper;
            if (r.isGeneralRegister()) {
                if (isUse) useSet.add(r.val);
                if (isDef) defSet.add(r.val);
            }
        }
        else if (oper instanceof NasmAddress) {
            NasmAddress addr = (NasmAddress) oper;
            extractUseDef(addr.val, true, false, useSet, defSet);
        }
        else if (oper instanceof NasmExpPlus) {
            NasmExpPlus exp = (NasmExpPlus) oper;
            extractUseDef(exp.op1, true, false, useSet, defSet);
            extractUseDef(exp.op2, true, false, useSet, defSet);
        }
        else if (oper instanceof NasmExpMinus) {
            NasmExpMinus exp = (NasmExpMinus) oper;
            extractUseDef(exp.op1, true, false, useSet, defSet);
            extractUseDef(exp.op2, true, false, useSet, defSet);
        }
        else if (oper instanceof NasmExpTimes) {
            NasmExpTimes exp = (NasmExpTimes) oper;
            extractUseDef(exp.op1, true, false, useSet, defSet);
            extractUseDef(exp.op2, true, false, useSet, defSet);
        }
    }

    public void affiche(String baseFileName){
        String fileName;
        PrintStream ps = System.out;

        if (baseFileName != null){
            try {
                fileName = baseFileName + ".fgs";
                ps = new PrintStream(fileName);
            }
            catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        ps.println("iter num = " + iterNum);
        for(NasmInst inst : this.nasm.sectionText){
            ps.println(
                "use = " + use.get(inst) +
                " def = " + def.get(inst) +
                "\tin = " + in.get(inst) +
                "\tout = " + out.get(inst) +
                "\t\t" + inst
            );
        }
    }
}
