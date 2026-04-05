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

public class Fg implements NasmVisitor <Void> {
    public Nasm nasm;
    public Graph graph;
    Map< NasmInst, Node> inst2Node;
    Map< Node, NasmInst> node2Inst;
    Map< String, NasmInst> label2Inst;

    public Fg(Nasm nasm){
        this.nasm = nasm;
        this.inst2Node = new HashMap<NasmInst, Node>();
        this.node2Inst = new HashMap<Node, NasmInst>();
        this.label2Inst = new HashMap<String, NasmInst>();
        this.graph = new Graph();

        // créer noeuds et gestion labels
        for (NasmInst inst : nasm.sectionText) {
            Node node = this.graph.newNode();
            this.inst2Node.put(inst, node);
            this.node2Inst.put(node, inst);
            
            if (inst.label != null) {
                this.label2Inst.put(inst.label.toString(), inst);
            }
        }

        // créer arcs avec visiteur
        for (NasmInst inst : nasm.sectionText) {
            inst.accept(this);
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

    // --- Instructions purement séquentielles ---

    public Void visit(NasmAdd inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmSub inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmMul inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmDiv inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmMov inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmPush inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmPop inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmOr inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmAnd inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmXor inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmNot inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmCmp inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmInst inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmInt inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmEmpty inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    public Void visit(NasmCall inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        return null;
    }

    // --- Sauts Conditionnels (Séquentiel + Branchement) ---

    public Void visit(NasmJe inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    public Void visit(NasmJne inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    public Void visit(NasmJl inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    public Void visit(NasmJle inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    public Void visit(NasmJg inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    public Void visit(NasmJge inst) {
        int index = nasm.sectionText.indexOf(inst);
        if (index < nasm.sectionText.size() - 1) {
            NasmInst nextInst = nasm.sectionText.get(index + 1);
            graph.addEdge(inst2Node.get(inst), inst2Node.get(nextInst));
        }
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    // --- Saut Inconditionnel (Branchement uniquement) ---

    public Void visit(NasmJmp inst) {
        if (inst.address != null) {
            NasmInst targetInst = label2Inst.get(inst.address.toString());
            if (targetInst != null) {
                graph.addEdge(inst2Node.get(inst), inst2Node.get(targetInst));
            }
        }
        return null;
    }

    // --- Fin du flux (Aucun arc) ---

    public Void visit(NasmRet inst) {
        return null;
    }

    // --- Visiteurs des opérandes et pseudo-instructions ---
    
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