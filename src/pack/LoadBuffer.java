package pack;

public class LoadBuffer {
	SourceQ name;
	boolean busy;
	int address;
	int instructionIndex; // to keep track of the instruction this is being executed in the load buffer
	int remainingCycles;
	boolean isRunning;
	public LoadBuffer(SourceQ name){
		this.name = name;
		this.busy = false;
		this.address = -1;
		this.instructionIndex=-1;
		this.remainingCycles = -1;
		this.isRunning = false;
	}
	
	public void issueInstruction(int address, int instructionIndex, int latency) {
		this.address = address;
		this.busy = true;
		this.instructionIndex = instructionIndex;
		this.remainingCycles = latency;
		this.isRunning = false;
	}
	
	public double execute() {
		return 0;
	}

	public boolean clashes(int index){
		return this.busy && this.address == index;
	}

	public boolean finished(){
		return this.busy && this.remainingCycles==0;
	}

	public void decrementRemainingCycles(){
		this.remainingCycles = Math.max(0, this.remainingCycles-1);
	}

	public String toString(){
		return "("+this.name + ": "+this.address+ ", "+this.busy+ ", "+this.remainingCycles +")";
	}
}
