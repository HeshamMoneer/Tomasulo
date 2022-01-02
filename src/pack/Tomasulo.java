package pack;



public class Tomasulo {
	double[] RAM;
	Register[] registerFile;
	StoreBuffer[] storeStation;
	LoadBuffer[] loadStation;
	ReservationStation[] addStation;
	ReservationStation[] mulStation;
	int currentIndex, currentCycle, addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency;
	Instruction[] instrucionQueue;

	public Tomasulo(String program){
		this(3, 3, 4, 8, 1, 1, program);
	}

	public Tomasulo(int addLatency, int subLatency, int mulLatency, int divLatency, int loadLatency, int storeLatency, String program){
		this.RAM = new double[1<<11];
		for(int i=0;i<1<<11;i++){
			this.RAM[i] = i+1;
		}
		this.registerFile = new Register[32];
		for(int i=0;i<32;i++)
			this.registerFile[i] = new Register();
		this.storeStation = new StoreBuffer[3];
		this.loadStation = new LoadBuffer[3];
		this.addStation = new ReservationStation[3];

		// initializing add reservation stations
		this.addStation[0] = new ReservationStation(SourceQ.A1);
		this.addStation[1] = new ReservationStation(SourceQ.A2);
		this.addStation[2] = new ReservationStation(SourceQ.A3);

		// initializing load buffers
		this.loadStation[0] = new LoadBuffer(SourceQ.L1);
		this.loadStation[1] = new LoadBuffer(SourceQ.L2);
		this.loadStation[2] = new LoadBuffer(SourceQ.L3);

		//initializing store buffers
		for(int i=0;i<3;i++)
			this.storeStation[i] = new StoreBuffer();

		// initializing mul reservation stations
		this.mulStation = new ReservationStation[2];
		this.mulStation[0] = new ReservationStation(SourceQ.M1);
		this.mulStation[1] = new ReservationStation(SourceQ.M2);

		this.currentCycle = 0;
		this.currentIndex = 0;
		this.addLatency = addLatency;
		this.subLatency = subLatency;
		this.mulLatency = mulLatency;
		this.divLatency = divLatency;
		this.loadLatency = loadLatency;
		this.storeLatency = storeLatency;
		this.instrucionQueue = (StringParser.parse(program)).toArray(new Instruction[0]);


	}

	public void issue(){
		if(currentCycle>= instrucionQueue.length)
			return;
		Instruction next = instrucionQueue[currentCycle];
		switch (next.opCode){
			case SUB:
				findReservation(next, addStation, subLatency);
				break;
			case ADD:
				findReservation(next, addStation, addLatency);
				break;
			case MUL:
				findReservation(next, mulStation, mulLatency);
				break;
			case DIV:
				findReservation(next, mulStation, divLatency);
				break;
			case LOAD:
				for(StoreBuffer sb: storeStation)
					if(sb.clashes(next.source1))
						return; // there is a store instruction with the same active address so we can't issue the instruction RAW
				for(LoadBuffer lb : loadStation)
					if(!lb.busy)
					{
						lb.issueInstruction(next.source1, currentIndex, loadLatency);
						registerFile[next.destination].waitForValue(lb.name);
						currentIndex++;
						return;
					}
				break;
			case STORE:
				for(StoreBuffer sb: storeStation)
					if(sb.clashes(next.source1))
						return; // there is a store instruction with the same active address so we can't issue the instruction WAW

				for(LoadBuffer lb: loadStation)
					if(lb.clashes(next.source1))
						return; // there is a store instruction with the same active address so we can't issue the instruction WAR

				for(StoreBuffer lb : storeStation)
					if(!lb.busy)
					{
						lb.issueInstruction(next.source1, registerFile[next.destination], currentIndex, loadLatency);
						currentIndex++;
						return;
					}
				break;

		}
	}

	// handles the issuing of the ALU instructions
	public void findReservation(Instruction i, ReservationStation[] r, int latency)
	{
		for(ReservationStation rs : r)
			if(!rs.busy){
				rs.issueInstruction(i.opCode, registerFile[i.source1], registerFile[i.source2], latency, currentIndex);
				registerFile[i.destination].waitForValue(rs.name);
				currentIndex++;
				return;
			}
	}

	public static void main(String[] args) {

	}
}
