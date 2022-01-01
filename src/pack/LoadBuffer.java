package pack;

public class LoadBuffer {
	boolean busy;
	int address;
	int instructionIndex; // to keep track of the instruction this is being executed in the load buffer

	public LoadBuffer(){
		this.busy = false;
		this.address = -1;
		this.instructionIndex=-1;
	}
	
	public void issueInstruction(int address, int instructionIndex) {
		this.address = address;
		this.busy = true;
		this.instructionIndex = instructionIndex;
	}
	
	public double execute() {
		return 0;
	}
}
