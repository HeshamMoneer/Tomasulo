package pack;

public class StoreBuffer {

	boolean busy;
	int address;
	double val;
	SourceQ qI;
	int instructionIndex;
	int remainingCycles;

	public StoreBuffer(){

		this.busy = false;
		this.address = -1;
		this.val = -1;
		this.qI = SourceQ.ZERO;
		this.instructionIndex=-1;
		this.remainingCycles = -1;
	}

	public void issueInstruction(int address, Register f1, int latency, int instructionIndex) {
		this.address = address;
		this.val = (f1.qI == SourceQ.ZERO)? f1.val : this.val;
		this.qI = f1.qI;
		this.remainingCycles = latency;
		this.instructionIndex = instructionIndex;
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

}
