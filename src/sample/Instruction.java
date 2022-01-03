package sample;

public class Instruction {
	OpCode opCode;
	int source1, source2, destination; //immediate?
	int issueCycle, startCycle, endCycle, writeCycle; // to keep track of the execution of every instruction
	
	public Instruction(OpCode opCode, int source1, int source2, int destination) {
		this.opCode = opCode;
		this.source1 = source1;
		this.source2 = source2;
		this.destination = destination;
		this.issueCycle = -1;
		this.startCycle = -1;
		this.endCycle = -1;
		this.writeCycle = -1;
	}
	
	public String toString() {
		return opCode+ " "+issueCycle+" "+startCycle+ " "+endCycle+" "+writeCycle;
	}
}
