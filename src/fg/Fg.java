package fg;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import nasm.Nasm;
import nasm.NasmAdd;
import nasm.NasmAddress;
import nasm.NasmAnd;
import nasm.NasmCall;
import nasm.NasmCmp;
import nasm.NasmConstant;
import nasm.NasmDiv;
import nasm.NasmEmpty;
import nasm.NasmExp;
import nasm.NasmExpMinus;
import nasm.NasmExpPlus;
import nasm.NasmExpTimes;
import nasm.NasmInst;
import nasm.NasmInt;
import nasm.NasmJe;
import nasm.NasmJg;
import nasm.NasmJge;
import nasm.NasmJl;
import nasm.NasmJle;
import nasm.NasmJmp;
import nasm.NasmJne;
import nasm.NasmLabel;
import nasm.NasmMov;
import nasm.NasmMul;
import nasm.NasmNot;
import nasm.NasmOperand;
import nasm.NasmOr;
import nasm.NasmPop;
import nasm.NasmPush;
import nasm.NasmRegister;
import nasm.NasmResb;
import nasm.NasmResd;
import nasm.NasmResq;
import nasm.NasmRest;
import nasm.NasmResw;
import nasm.NasmRet;
import nasm.NasmSub;
import nasm.NasmVisitor;
import nasm.NasmXor;
import util.graph.Graph;
import util.graph.Node;
import util.graph.NodeList;

public class Fg implements NasmVisitor<Void> {
    public Nasm nasm;
    public Graph graph;
    Map<NasmInst, Node> inst2Node;
    Map<Node, NasmInst> node2Inst;
    Map<String, NasmInst> label2Inst;

    public Fg(Nasm nasm){
        this.nasm = nasm;
        this.inst2Node = new HashMap<>();
        this.node2Inst = new HashMap<>();
        this.label2Inst = new HashMap<>();
        this.graph = new Graph();

        // Création des noeuds + mapping labels
        for (NasmInst inst : nasm.sectionText) {
            Node node = this.graph.newNode();
            inst2Node.put(inst, node);
            node2Inst.put(node, inst);

            if (inst.label != null) {
                // OK ici car NasmLabel.toString() = val
                label2Inst.put(inst.label.toString(), inst);
            }
        }

        // Création des arcs
        for (NasmInst inst : nasm.sectionText) {
            inst.accept(this);
        }
    }

    // --- utilitaires ---

    private void addSequentialEdge(NasmInst inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
    }

    private void addJumpEdge(NasmInst inst, NasmOperand address) {
        if (address == null) return;

        String labelName = null;

        // cas 1 : label direct (call f, jmp f, etc.)
        if (address instanceof NasmLabel) {
            labelName = ((NasmLabel) address).val;
        }
        // cas 2 : adresse contenant un label
        else if (address instanceof NasmAddress) {
            NasmAddress addr = (NasmAddress) address;

            if (addr.val instanceof NasmLabel) {
                labelName = ((NasmLabel) addr.val).val;
            }
        }

        if (labelName != null) {
            NasmInst targetInst = label2Inst.get(labelName);
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
    }

    public void affiche(String baseFileName){
        String fileName;
        PrintStream out = System.out;

        if (baseFileName != null){
            try {
                fileName = baseFileName + ".fg";
                out = new PrintStream(fileName);
            }
            catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        for(NasmInst nasmInst : nasm.sectionText){
            Node n = this.inst2Node.get(nasmInst);
            out.print(n + " : ( ");
            for(NodeList q=n.succ(); q!=null; q=q.tail) {
                out.print(q.head.toString());
                out.print(" ");
            }
            out.println(")\t" + nasmInst);
        }
    }

    // --- Instructions séquentielles ---
    public Void visit(NasmAdd inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmSub inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmMul inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmDiv inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmMov inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmPush inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmPop inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmOr inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmAnd inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmXor inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmNot inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmCmp inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmInst inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmInt inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmEmpty inst) { addSequentialEdge(inst); return null; }
    public Void visit(NasmCall inst) { addSequentialEdge(inst); return null; }

    // --- Sauts conditionnels ---
    public Void visit(NasmJe inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    public Void visit(NasmJne inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    public Void visit(NasmJl inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    public Void visit(NasmJle inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    public Void visit(NasmJg inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    public Void visit(NasmJge inst) {
        addSequentialEdge(inst);
        addJumpEdge(inst, inst.address);
        return null;
    }

    // --- Saut inconditionnel ---
    public Void visit(NasmJmp inst) {
        addJumpEdge(inst, inst.address);
        return null;
    }

    // --- Fin ---
    public Void visit(NasmRet inst) {
        return null;
    }

    // --- Opérandes ---
    public Void visit(NasmAddress operand) { return null; }
    public Void visit(NasmConstant operand) { return null; }
    public Void visit(NasmLabel operand) { return null; }
    public Void visit(NasmRegister operand) { return null; }

    public Void visit(NasmResb pseudoInst) { return null; }
    public Void visit(NasmResw pseudoInst) { return null; }
    public Void visit(NasmResd pseudoInst) { return null; }
    public Void visit(NasmResq pseudoInst) { return null; }
    public Void visit(NasmRest pseudoInst) { return null; }

    public Void visit(NasmExpPlus exp) { return null; }
    public Void visit(NasmExpMinus exp) { return null; }
    public Void visit(NasmExpTimes exp) { return null; }
    public Void visit(NasmExp exp) { return null; }
}
