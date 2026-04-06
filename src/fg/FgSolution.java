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
	public Map< NasmInst, IntSet> use;
	public Map< NasmInst, IntSet> def;
	public Map< NasmInst, IntSet> in;
	public Map< NasmInst, IntSet> out;

	public FgSolution(Nasm nasm, Fg fg){
		this.nasm = nasm;
		this.fg = fg;
		this.use = new HashMap< NasmInst, IntSet>();
		this.def = new HashMap< NasmInst, IntSet>();
		this.in =  new HashMap< NasmInst, IntSet>();
		this.out = new HashMap< NasmInst, IntSet>();

		int size = nasm.getTempCounter();

		for(NasmInst inst : nasm.sectionText){
			use.put(inst, new IntSet(size));
			def.put(inst, new IntSet(size));
			in.put(inst, new IntSet(size));
			out.put(inst, new IntSet(size));

			extractUseDef(inst.source, inst.srcDef, inst.srcUse, use.get(inst), def.get(inst));
			extractUseDef(inst.destination, inst.destDef, inst.destUse, use.get(inst), def.get(inst));
		}

		boolean changed = true;
		while(changed) {
			changed = false;
			iterNum++;

			for(NasmInst inst : nasm.sectionText) {
				Node n = fg.inst2Node.get(inst);

				IntSet oldIn = in.get(inst).copy();
				IntSet oldOut = out.get(inst).copy();

				IntSet newOut = new IntSet(size);

				for(NodeList nList = n.succ(); nList != null; nList = nList.tail) {
					Node succNode = nList.head;
					NasmInst succInst = fg.node2Inst.get(succNode);
					newOut.union(in.get(succInst));
				}
				out.put(inst, newOut);
				

				IntSet newIn = use.get(inst).copy();
				IntSet tempOut = out.get(inst).copy().minus(def.get(inst));
				newIn.union(tempOut);
				in.put(inst, newIn);

				if(!oldIn.equal(in.get(inst)) || !oldOut.equal(out.get(inst))) {
					changed = true;
				}
			}
		}
	} 

	private void extractUseDef(Object oper, boolean isUse, boolean isDef, IntSet useSet, IntSet defSet) {
		if (oper == null) return;

		if (oper instanceof NasmRegister) {
			NasmRegister r1 = (NasmRegister) oper;
			if (r1.isGeneralRegister()) {
				if (isUse) useSet.add(r1.val);
				if (isDef) defSet.add(r1.val);
			}
		}
		else if (oper instanceof NasmAddress) {
			NasmAddress address = (NasmAddress) oper;
			// Dans une adresse (ex: [ebp + r1]), les composants sont TOUJOURS utilises (lus),
			// meme si l'adresse globale est la destination d'une ecriture.
			extractUseDef(address.val, true, false, useSet, defSet);
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
		PrintStream out = System.out;

		if (baseFileName != null){
			try {
				fileName = baseFileName + ".fgs";
				out = new PrintStream(fileName);
			}
			catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}

		out.println("iter num = " + iterNum);
		for(NasmInst nasmInst : this.nasm.sectionText){
			out.println("use = "+ this.use.get(nasmInst) + " def = "+ this.def.get(nasmInst) + "\tin = " + this.in.get(nasmInst) + "\t \tout = " + this.out.get(nasmInst) + "\t \t" + nasmInst);
		}
	}
}