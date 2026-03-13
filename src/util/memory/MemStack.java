package util.memory;

/* Big Endian */

public class MemStack extends MemSeg{
    private int top;


    public MemStack(int size){
	super(size);
	top = size;
    }

    public int getTop(){
	return top;
    }
    
    public void setTop(int value){
	top = value;
    }
    
    public void print(){
	for(int adr = size - 4; adr >= top; adr = adr - 4){
	    //	    	    System.out.println("adr =" + adr);
	    System.out.print("[" + adr + "] " + readInt(adr) + "\t");
	}
	System.out.println();
    }
    
    public void shrink(int bytesNb){
	top += bytesNb;
    }
    
    public void extend(int bytesNb){
	top -= bytesNb;
    }
    
    public void pushInt(int value){
	if(top - 3 < 0)
            throw new RuntimeException("stack overflow");

	byte[] fourBytes = intToBytes(value);
	top--;
	mem[top] = fourBytes[3];
	top--;
	mem[top] = fourBytes[2];
	top--;
	mem[top] = fourBytes[1];
	top--;
	mem[top] = fourBytes[0];
	/*	mem[top--] = fourBytes[3];
	mem[top--] = fourBytes[2];
	mem[top--] = fourBytes[1];
	mem[top--] = fourBytes[0];
*/

    }

    public int popInt(){
	if(top + 3 >= size)
            throw new RuntimeException("stack underflow");
	byte byte0 = mem[top];
	top++;
	byte byte1 = mem[top];
	top++;
	byte byte2 = mem[top];
	top++;
	byte byte3 = mem[top];
	top++;
	
	/*	byte byte0 = mem[++top];
	byte byte1 = mem[++top];
	byte byte2 = mem[++top];
	byte byte3 = mem[++top];*/
	
	return bytesToInt(byte0, byte1, byte2, byte3);
    }

}
