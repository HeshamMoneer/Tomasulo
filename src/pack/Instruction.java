package pack;

public class Instruction {
	OpCode opCode;
	int source1, source2, destination; //immediate?
	
	public Instruction(OpCode opCode, int source1, int source2, int destination) {
		this.opCode = opCode;
		this.source1 = source1;
		this.source2 = source2;
		this.destination = destination;
	}
	
	public String toString() {
		return opCode+" "+destination+" "+ source1 + " "+source2;
	}
}
