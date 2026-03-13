package util.graph;

import util.graph.*;
import util.intset.*;
import java.util.*;
import java.io.*;

public class ColorGraph {
    public  Graph          graph;
    public  int            vertexNb;
    private Stack<Integer> stack;
    public  IntSet         removed;
    public  IntSet         spill;
    public  int[]          color;
    public  int            colorNb;
    public  Node[]         int2Node;
    static  int            NOCOLOR = -1;

    public ColorGraph(Graph graph, int colorNb, int[] preColoredVertices){
	this.graph   = graph;
	this.colorNb = colorNb;
	stack        = new Stack<Integer>(); 
	vertexNb     = graph.nodeCount();
	color        = new int[vertexNb];
	removed      = new IntSet(vertexNb);
	spill        = new IntSet(vertexNb);
	int2Node     = graph.nodeArray();
	for(int v=0; v < vertexNb; v++){
	    int preColor = preColoredVertices[v];
	    if(preColor >= 0 && preColor < colorNb)
		color[v] = preColoredVertices[v];
	    else
		color[v] = NOCOLOR;
	}
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* associe une couleur à tous les sommets se trouvant dans la pile */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public void select()
    {
	int t;
	while(!stack.empty()){
	    t = stack.pop();
	    removed.remove(t);
	    if(color[t] == NOCOLOR)
		color[t] = chooseAvailableColor(neighborsColor(t));
	    if(color[t] == NOCOLOR)
		System.out.println("cannot find a color for vertex "+ t);
	}
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* récupère les couleurs des voisins de t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public IntSet neighborsColor(int t)
    {
	IntSet colorSet = new IntSet(colorNb);

	for(NodeList p = int2Node[t].succ(); p!=null; p=p.tail)
	    if(color[p.head.label()] != NOCOLOR)
		colorSet.add(color[p.head.label()]);
	return colorSet;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* recherche une couleur absente de colorSet */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int chooseAvailableColor(IntSet colorSet)
    {
	for(int c=0; c < colorSet.getSize(); c++)
	    if(!colorSet.isMember(c))
		return c;
	return NOCOLOR;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* calcule le nombre de voisins du sommet t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int neighborsNb(int t)
    {
	int nb = 0;
	for(NodeList p = this.int2Node[t].succ(); p!=null; p=p.tail)
	    if(!removed.isMember(p.head.label()))
		nb++;
	return nb;
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* simplifie le graphe d'interférence g                                                                        */
    /* la simplification consiste à enlever du graphe les temporaires qui ont moins de k voisins                   */
    /* et à les mettre dans une pile                                                                               */
    /* à la fin du processus, le graphe peut ne pas être vide, il s'agit des temporaires qui ont au moins k voisin */
    /*-------------------------------------------------------------------------------------------------------------*/

    public int simplify()
    {
	boolean removedAtLeastOneTemp = true;
	while(removedAtLeastOneTemp && stack.size() != vertexNb){
	    removedAtLeastOneTemp = false;
	    for(int t = 0; t < vertexNb; t++){
		if(!removed.isMember(t)){
		    int n = neighborsNb(t);
		    //		    System.out.println("node " + t + " has " + n + " neighbours"); 
		    int precolored = (color[t] == NOCOLOR)? 0 : 1;
		    if(n < (colorNb - precolored)){
			stack.push(t);
			removed.add(t);
			//			System.out.println("remove vertex " + t);
			removedAtLeastOneTemp = true;
		    }
		}
	    }
	}
	return stack.size();
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public void spill()
    {
	int t;
	while(stack.size() != vertexNb){ /* some nodes have not been pushed */
	    for(t=0; t < vertexNb; t++){
		if(!removed.isMember(t)){ /* t i still in the graph */
		    System.out.println("vertex " + t + " is a potential spill");
		    spill.add(t);
		    removed.add(t);
		    stack.push(t);
		    simplify();
		}
	    }
	}
    }


    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/

    public void color()
    {
	this.simplify();
	this.spill();
	this.select();
    }

    public void affiche()
    {
	System.out.println("vertex\tcolor");
	for(int i = 0; i < vertexNb; i++){
	    System.out.println(i + "\t" + color[i]);
	}
    }
    
    

}
