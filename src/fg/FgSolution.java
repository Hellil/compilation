package fg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import nasm.Nasm;
import nasm.NasmAddress;
import nasm.NasmExp;
import nasm.NasmInst;
import nasm.NasmOperand;
import nasm.NasmRegister;
import util.graph.Node;
import util.graph.NodeList;
import util.intset.IntSet;

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

        // On initialise la taille des ensembles avec le nombre de registres temporaires
        int maxSize = nasm.getTempCounter();

        // 1. Initialisation des ensembles pour chaque instruction
        for (NasmInst inst : nasm.sectionText) {
            this.use.put(inst, new IntSet(maxSize));
            this.def.put(inst, new IntSet(maxSize));
            this.in.put(inst, new IntSet(maxSize));
            this.out.put(inst, new IntSet(maxSize));

            fillUseDef(inst);
        }

        // 2. Calcul itératif de in et out
        solve(maxSize);
    }

    private void fillUseDef(NasmInst inst) {
        IntSet u = this.use.get(inst);
        IntSet d = this.def.get(inst);

        // Gestion de la source
        if (inst.source != null) {
            if (inst.srcUse) collectRegisters(inst.source, u);
            if (inst.srcDef) collectRegisters(inst.source, d);
        }

        // Gestion de la destination
        if (inst.destination != null) {
            // Cas particulier : si la destination est une adresse [exp]
            // on l'UTILISE (pour calculer l'adresse) mais on ne définit pas les registres dedans
            if (inst.destination instanceof NasmAddress) {
                collectRegisters(inst.destination, u);
            } else {
                if (inst.destUse) collectRegisters(inst.destination, u);
                if (inst.destDef) collectRegisters(inst.destination, d);
            }
        }
        
        // Gestion du champ address (souvent utilisé pour les sauts ou appels)
        if (inst.address != null) {
            collectRegisters(inst.address, u);
        }
    }

    // Méthode récursive pour fouiller dans les opérandes et les expressions (NasmExp)
    private void collectRegisters(NasmOperand op, IntSet set) {
        if (op == null) return;

        if (op instanceof NasmRegister) {
            NasmRegister reg = (NasmRegister) op;
            if (reg.isGeneralRegister()) {
                set.add(reg.val);
            }
        } else if (op instanceof NasmAddress) {
            // Dans une adresse [val], on cherche les registres dans l'expression 'val'
            collectRegistersFromExp(((NasmAddress) op).val, set);
        }
    }

    private void collectRegistersFromExp(NasmExp exp, IntSet set) {
        if (exp == null) return;
        
        if (exp instanceof NasmRegister) {
            NasmRegister reg = (NasmRegister) exp;
            if (reg.isGeneralRegister()) {
                set.add(reg.val);
            }
        } else if (exp instanceof NasmBinExp) {
            // Si c'est une opération (ex: ebp + r1), on regarde à gauche et à droite
            NasmBinExp bin = (NasmBinExp) exp;
            collectRegistersFromExp(bin.left, set);
            collectRegistersFromExp(bin.right, set);
        }
        // Les constantes (NasmConstant) ne contiennent pas de registres, on les ignore.
    }

    private void solve(int maxSize) {
        boolean stable = false;
        this.iterNum = 0;

        while (!stable) {
            stable = true;
            this.iterNum++;

            // On parcourt les instructions en partant de la fin pour une convergence plus rapide
            for (int i = nasm.sectionText.size() - 1; i >= 0; i--) {
                NasmInst inst = nasm.sectionText.get(i);
                
                IntSet currentIn = this.in.get(inst);
                IntSet currentOut = this.out.get(inst);
                
                IntSet oldIn = currentIn.copy();
                IntSet oldOut = currentOut.copy();

                // out[s] = Union des in[succ]
                Node node = fg.inst2Node.get(inst);
                IntSet newOut = new IntSet(maxSize);
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

                // in[s] = use[s] U (out[s] - def[s])
                IntSet newIn = newOut.copy();
                newIn.minus(this.def.get(inst));
                newIn.union(this.use.get(inst));
                this.in.put(inst, newIn);

                // Vérification du point fixe
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
        for (NasmInst nasmInst : this.nasm.sectionText) {
            outStream.println("use = " + this.use.get(nasmInst) + 
                              " def = " + this.def.get(nasmInst) + 
                              "\tin = " + this.in.get(nasmInst) + 
                              "\tout = " + this.out.get(nasmInst) + 
                              "\t\t" + nasmInst);
        }
    }
}