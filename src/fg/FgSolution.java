package fg;
import util.graph.*;
import nasm.*;
import util.intset.*;
import java.io.*;
import java.util.*;

public class FgSolution {
    int iterNum = 0;
    public Nasm nasm;
    Fg fg;
    public Map<NasmInst, IntSet> use;
    public Map<NasmInst, IntSet> def;
    public Map<NasmInst, IntSet> in;
    public Map<NasmInst, IntSet> out;

    public FgSolution(Nasm nasm, Fg fg) {
        this.nasm = nasm;
        this.fg = fg;
        this.use = new HashMap<NasmInst, IntSet>();
        this.def = new HashMap<NasmInst, IntSet>();
        this.in = new HashMap<NasmInst, IntSet>();
        this.out = new HashMap<NasmInst, IntSet>();

        // On utilise le compteur de temporaires pour dimensionner les IntSet
        int maxSize = nasm.getTempCounter();

        // 1. Initialisation
        for (NasmInst inst : nasm.sectionText) {
            this.use.put(inst, new IntSet(maxSize));
            this.def.put(inst, new IntSet(maxSize));
            this.in.put(inst, new IntSet(maxSize));
            this.out.put(inst, new IntSet(maxSize));
            fillUseDef(inst);
        }

        // 2. Résolution
        solve(maxSize);
    }

    private void fillUseDef(NasmInst inst) {
        IntSet u = this.use.get(inst);
        IntSet d = this.def.get(inst);

        // Source
        if (inst.source != null) {
            if (inst.srcUse) collectRegisters(inst.source, u);
            if (inst.srcDef) collectRegisters(inst.source, d);
        }

        // Destination
        if (inst.destination != null) {
            // Si c'est une adresse [exp], on l'utilise pour le calcul (USE)
            if (inst.destination instanceof NasmAddress) {
                collectRegisters(inst.destination, u);
            } else {
                if (inst.destUse) collectRegisters(inst.destination, u);
                if (inst.destDef) collectRegisters(inst.destination, d);
            }
        }
        
        // Adresse (appels/sauts)
        if (inst.address != null) {
            collectRegisters(inst.address, u);
        }
    }

    private void collectRegisters(NasmOperand op, IntSet set) {
        if (op == null) return;

        // Si l'opérande est directement un registre
        if (op instanceof NasmRegister) {
            NasmRegister reg = (NasmRegister) op;
            if (reg.isGeneralRegister()) {
                set.add(reg.val);
            }
        } 
        // Si c'est une adresse [val], on regarde ce qu'il y a dans 'val'
        else if (op instanceof NasmAddress) {
            NasmAddress addr = (NasmAddress) op;
            // 'val' est un NasmExp. On vérifie s'il contient un registre.
            if (addr.val instanceof NasmRegister) {
                NasmRegister reg = (NasmRegister) addr.val;
                if (reg.isGeneralRegister()) set.add(reg.val);
            } 
            // Note : Si ton NasmExp peut être une addition (ex: r1 + r2), 
            // il faudrait un visiteur d'expression. Mais avec les classes fournies,
            // on se limite au cas où l'expression est un registre ou une constante.
        }
    }

    private void solve(int maxSize) {
        boolean stable = false;
        this.iterNum = 0;

        while (!stable) {
            stable = true;
            this.iterNum++;

            // Parcours inverse pour accélérer la propagation du "out" vers le "in"
            for (int i = nasm.sectionText.size() - 1; i >= 0; i--) {
                NasmInst inst = nasm.sectionText.get(i);
                
                IntSet oldIn = this.in.get(inst).copy();
                IntSet oldOut = this.out.get(inst).copy();

                // 1. OUT[s] = Union des IN[succ]
                IntSet newOut = new IntSet(maxSize);
                Node node = fg.inst2Node.get(inst);
                if (node != null) {
                    NodeList succs = node.succ();
                    while (succs != null) {
                        NasmInst nextInst = fg.node2Inst.get(succs.head);
                        if (nextInst != null) {
                            newOut.union(this.in.get(nextInst));
                        }
                        succs = succs.tail;
                    }
                }
                this.out.put(inst, newOut);

                // 2. IN[s] = USE[s] U (OUT[s] - DEF[s])
                IntSet newIn = newOut.copy();
                newIn.minus(this.def.get(inst));
                newIn.union(this.use.get(inst));
                this.in.put(inst, newIn);

                // 3. Test de stabilité
                if (!newIn.equal(oldIn) || !newOut.equal(oldOut)) {
                    stable = false;
                }
            }
        }
    }

    public void affiche(String baseFileName) {
        PrintStream outStream = System.out;
        if (baseFileName != null) {
            try {
                outStream = new PrintStream(new FileOutputStream(baseFileName + ".fgs"));
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        outStream.println("iter num = " + iterNum);
        for (NasmInst inst : this.nasm.sectionText) {
            outStream.println("use = " + this.use.get(inst) + 
                              " def = " + this.def.get(inst) + 
                              "\tin = " + this.in.get(inst) + 
                              "\tout = " + this.out.get(inst) + 
                              "\t\t" + inst);
        }
    }
}