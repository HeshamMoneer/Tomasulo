package pack;

public class ReservationStation {
	SourceQ name;
	boolean busy;
	OpCode opCode;
	double vJ, vK;
	SourceQ qJ, qK;
	int remainingCycles;
	int instructionIndex; // to keep track of the instruction this is being executed in the load buffer

	public ReservationStation(SourceQ name){
		this.name = name;
		this.busy = false;
		this.opCode = OpCode.ADD;
		this.vJ = -1;
		this.vK = -1;
		this.qJ = SourceQ.ZERO;
		this.qK = SourceQ.ZERO;
		this.remainingCycles = -1;
		this.instructionIndex = -1;
	}
	
	public double execute()
	{
		switch (this.opCode){
			case ADD:
				return vJ + vK;
			case SUB:
				return vJ - vK;
			case MUL:
				return vJ * vK;
			case DIV:
				return vJ / vK;
			default:
				return -1;
		}

	}

	public void decrementRemainingCycles(){
		this.remainingCycles = Math.max(0, this.remainingCycles-1);
	}

	public boolean finished(){
		return this.busy && this.remainingCycles==0;
	}
	
	public void issueInstruction(OpCode opCode, Register f1, Register f2, int latency, int instructionIndex) {
		this.busy = true;
		this.opCode = opCode;
		this.vJ = (f1.qI == SourceQ.ZERO)? f1.val : this.vJ;
		this.vK = (f2.qI == SourceQ.ZERO)? f2.val : this.vK;
		this.remainingCycles = latency;
		this.instructionIndex = instructionIndex;
	}
}
