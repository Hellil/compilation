package ig;

import java.io.IOException;
import java.io.PrintStream;

import fg.FgSolution;
import nasm.Nasm;
import nasm.NasmAddress;
import nasm.NasmExp;
import nasm.NasmExpMinus;
import nasm.NasmExpPlus;
import nasm.NasmInst;
import nasm.NasmOperand;
import nasm.NasmRegister;
import util.graph.ColorGraph;
import util.graph.Graph;
import util.graph.Node;
import util.graph.NodeList;
import util.intset.IntSet;

public class Ig {
    public Graph graph;
    public FgSolution fgs;
    public int regNb;
    public Nasm nasm;
    public Node int2Node[];
    
    public Ig(FgSolution fgs){
		this.fgs = fgs;
		this.graph = new Graph();
		this.nasm = fgs.nasm;
		this.regNb = this.nasm.getTempCounter();
		this.int2Node = new Node[regNb];
		this.build();
    }
    
    public void build() {
        // Étape 1 : un nœud par registre virtuel
        for (int i = 0; i < regNb; i++) {
            int2Node[i] = graph.newNode();
        }
        for (NasmInst inst : nasm.sectionText) {
            addInterferenceEdges(fgs.in.get(inst));
            addInterferenceEdges(fgs.out.get(inst));
        }
    }

    private void addInterferenceEdges(IntSet set){
        if(set == null) return;
        for(int i = 0; i < regNb; i++){
            if(!set.isMember(i)) continue;
            for(int j = i + 1; j < regNb; j++){
                if(!set.isMember(j)) continue;
                graph.addNOEdge(int2Node[i], int2Node[j]);
            }
        }
    }

    public int[] getPrecoloredTemporaries(){
        int[] precolored = new int[regNb];
        for(int i = 0; i < regNb; i++)
            precolored[i] = -1;

        for(NasmInst inst : nasm.sectionText){
			if(inst.destination instanceof NasmRegister){
				NasmRegister reg = (NasmRegister) inst.destination;
				if(reg.isGeneralRegister() && reg.val >= 0 && reg.color != Nasm.REG_UNK)
					precolored[reg.val] = reg.color;
			}

			if(inst.source instanceof NasmRegister){
				NasmRegister reg = (NasmRegister) inst.source;
				if(reg.isGeneralRegister() && reg.val >= 0 && reg.color != Nasm.REG_UNK)
					precolored[reg.val] = reg.color;
			}
        }
        return precolored;
    }

    public void allocateRegisters(){
        int[] precolored = getPrecoloredTemporaries();
        ColorGraph cg = new ColorGraph(graph, 4, precolored);
        cg.color();
        for(NasmInst inst : nasm.sectionText){
			if(inst.destination instanceof NasmRegister){
				NasmRegister reg = (NasmRegister) inst.destination;
				if(reg.isGeneralRegister() && reg.val >= 0)
					reg.colorRegister(cg.color[reg.val]);
			} else if(inst.destination instanceof NasmAddress){
				colorExp(((NasmAddress) inst.destination).val, cg.color);
			}

			if(inst.source instanceof NasmRegister){
				NasmRegister reg = (NasmRegister) inst.source;
				if(reg.isGeneralRegister() && reg.val >= 0)
					reg.colorRegister(cg.color[reg.val]);
			} else if(inst.source instanceof NasmAddress){
				colorExp(((NasmAddress) inst.source).val, cg.color);
			}
        }
    }

    private void colorExp(NasmExp exp, int[] colors){
        if(exp instanceof NasmRegister){
            NasmRegister reg = (NasmRegister) exp;
            if(reg.isGeneralRegister() && reg.val >= 0)
                reg.colorRegister(colors[reg.val]);
        } else if(exp instanceof NasmExpPlus){
            colorExp(((NasmExpPlus) exp).op1, colors);
            colorExp(((NasmExpPlus) exp).op2, colors);
        } else if(exp instanceof NasmExpMinus){
            colorExp(((NasmExpMinus) exp).op1, colors);
            colorExp(((NasmExpMinus) exp).op2, colors);
        }
    }

    public void affiche(String baseFileName){
        String fileName;
        PrintStream out = System.out;
        if (baseFileName != null){
            try {
                fileName = baseFileName + ".ig";
                out = new PrintStream(fileName);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        for(int i = 0; i < regNb; i++){
            Node n = this.int2Node[i];
            out.print(n + " : ( ");
            for(NodeList q=n.succ(); q!=null; q=q.tail) {
                out.print(q.head.toString());
                out.print(" ");
            }
            out.println(")");
        }
    }
}
