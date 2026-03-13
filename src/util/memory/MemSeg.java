package util.memory;

/* Big Endian */

public class MemSeg{
    public int size;
    public byte[] mem;

    public MemSeg(int size){
	this.size = size;
	this.mem = new byte[size];
    }

    public int readInt(int address){
	//System.out.println("read memory at address " + address + " size = " + size);
	if((address < 0) || (address + 3 >= size))
            throw new RuntimeException("segmentation fault");
	return bytesToInt(mem[address], mem[address + 1], mem[address + 2], mem[address + 3]);
    }

    public byte readByte(int address){
	//	System.out.println("read memory at address " + address);
	if((address < 0) || (address >= size))
            throw new RuntimeException("segmentation fault");
	return mem[address];
    }

    public void writeInt(int address, int value){
	if((address < 0) || (address + 3 >= size))
            throw new RuntimeException("segmentation fault");

	byte[] fourBytes = intToBytes(value);
	mem[address] = fourBytes[0];
	mem[address + 1] = fourBytes[1];
	mem[address + 2] = fourBytes[2];
	mem[address + 3] = fourBytes[3];
    }

    public int bytesToInt(byte byte0, byte byte1, byte byte2, byte byte3){
        return  ((byte0 & 0xFF) << 24) |
                ((byte1 & 0xFF) << 16) |
                ((byte2 & 0xFF) << 8) |
                ((byte3 & 0xFF) << 0);
    }
    
    public byte[] intToBytes2( int data ) {    
    byte[] result = new byte[4];
    result[0] = (byte) ((data & 0xFF000000) >> 24);
    result[1] = (byte) ((data & 0x00FF0000) >> 16);
    result[2] = (byte) ((data & 0x0000FF00) >> 8);
    result[3] = (byte) ((data & 0x000000FF) >> 0);
    return result;        
}
    public byte[] intToBytes(int i)
    {
	byte[] result = new byte[4];
	
	result[0] = (byte) (i >> 24);
	result[1] = (byte) (i >> 16);
	result[2] = (byte) (i >> 8);
	result[3] = (byte) (i /*>> 0*/);
	
	return result;
    }

    /*    public static void main(String[] args){
	Memory mem = new Memory(100, 100);
	mem.pushInt(3467);
	int v = mem.popInt();
	System.out.println("val =" + v);
	v = mem.popInt();

	mem.writeInt(96, 234);
	int val = mem.readInt(96);
	System.out.println("val =" + val);
	
	}*/
    
}
