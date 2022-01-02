package pack;

public class StoreBuffer {

	boolean busy;
	int address;
	double val;
	SourceQ qI;
	int instructionIndex;
	int remainingCycles;
	boolean isRunning;

	public StoreBuffer(){

		this.busy = false;
		this.address = -1;
		this.val = -1;
		this.qI = SourceQ.ZERO;
		this.instructionIndex=-1;
		this.remainingCycles = -1;
		this.isRunning = false;
	}

	public void issueInstruction(int address, Register f1, int instructionIndex, int latency) {
		this.address = address;
		this.busy = true;
		this.val = (f1.qI == SourceQ.ZERO)? f1.val : this.val;
		this.qI = f1.qI;
		this.remainingCycles = latency;
		this.instructionIndex = instructionIndex;
		this.isRunning = false;
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
		return "("+this.val + ", "+this.qI + ", "+ this.address + ", " + this.remainingCycles + ", "+this.busy+")";
	}

}
