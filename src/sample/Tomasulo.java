package sample;


import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Tomasulo {
	double[] RAM;
	Register[] registerFile;
	StoreBuffer[] storeStation;
	LoadBuffer[] loadStation;
	ReservationStation[] addStation;
	ReservationStation[] mulStation;
	int currentIndex, currentCycle, addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency;
	Instruction[] instrucionQueue;
    @FXML
	GridPane instructionQ;

	public Tomasulo(String program){
		this(3, 3, 4, 8, 5, 3, program);
	}

	public Tomasulo(int addLatency, int subLatency, int mulLatency, int divLatency, int loadLatency, int storeLatency, String program){
		this.RAM = new double[2048];
		for(int i=0;i<RAM.length;i++){
			this.RAM[i] = i;
		}
		this.registerFile = new Register[32];
		for(int i=0;i<registerFile.length;i++)
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
		if(currentIndex>= instrucionQueue.length)
			return;
		Instruction next = instrucionQueue[currentIndex];
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
						next.issueCycle = currentCycle;
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
						next.issueCycle = currentCycle;
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
				rs.issueInstruction(i.opCode, registerFile[i.source1], registerFile[i.source2], currentIndex, latency);
				registerFile[i.destination].waitForValue(rs.name);
				currentIndex++;
				i.issueCycle = currentCycle;
				return;
			}
	}

	public void executeReservation(ReservationStation rs){
		if(!rs.isRunning && rs.remainingCycles !=0)
		{
			instrucionQueue[rs.instructionIndex].startCycle = currentCycle;
			rs.isRunning = true;
		}

		rs.decrementRemainingCycles();
		if(rs.isRunning && rs.remainingCycles == 0)
		{
			instrucionQueue[rs.instructionIndex].endCycle = currentCycle;
			rs.isRunning = false;
		}
	}

	public void execute(){
		for(ReservationStation rs : addStation)
			if(rs.busy && rs.qJ == SourceQ.ZERO && rs.qK == SourceQ.ZERO && instrucionQueue[rs.instructionIndex].issueCycle != currentCycle)
			{
				executeReservation(rs);
			}
		for(ReservationStation rs : mulStation)
			if(rs.busy && rs.qJ == SourceQ.ZERO && rs.qK == SourceQ.ZERO && instrucionQueue[rs.instructionIndex].issueCycle != currentCycle)
				executeReservation(rs);
		for(StoreBuffer sb : storeStation)
			if(sb.busy && sb.qI == SourceQ.ZERO && instrucionQueue[sb.instructionIndex].issueCycle != currentCycle)
			{
				if(!sb.isRunning && sb.remainingCycles !=0)
				{
					instrucionQueue[sb.instructionIndex].startCycle = currentCycle;
					sb.isRunning = true;
				}
				sb.decrementRemainingCycles();
				if(sb.isRunning && sb.remainingCycles==0)
				{
					instrucionQueue[sb.instructionIndex].endCycle = currentCycle;
					sb.isRunning = false;
					RAM[sb.address] = sb.val;
					sb.busy = false;
				}
			}
		for(LoadBuffer lb : loadStation)
			if(lb.busy && instrucionQueue[lb.instructionIndex].issueCycle != currentCycle)
			{
				if(!lb.isRunning && lb.remainingCycles !=0)
				{
					instrucionQueue[lb.instructionIndex].startCycle = currentCycle;
					lb.isRunning = true;
				}
				lb.decrementRemainingCycles();
				if(lb.isRunning && lb.remainingCycles==0)
				{
					instrucionQueue[lb.instructionIndex].endCycle = currentCycle;
					lb.isRunning = false;
				}
			}
	}

	public void writeReservation(ReservationStation rs){
		double val =rs.execute();
		if(registerFile[instrucionQueue[rs.instructionIndex].destination].qI == rs.name)
			registerFile[instrucionQueue[rs.instructionIndex].destination].writeValue(val);
		instrucionQueue[rs.instructionIndex].writeCycle = currentCycle;
		writeReservations(rs.name, val);
		rs.busy = false;
	}

	public void writeResult()
	{
		// starting with the add station
		for(ReservationStation rs : addStation)
			if(rs.finished() && instrucionQueue[rs.instructionIndex].endCycle != currentCycle)
			{
				writeReservation(rs);
				return;
			}
		for(ReservationStation rs : mulStation)
			if(rs.finished() && instrucionQueue[rs.instructionIndex].endCycle != currentCycle)
			{
				writeReservation(rs);
				return;
			}

		for(LoadBuffer lb : loadStation)
			if(lb.finished() && instrucionQueue[lb.instructionIndex].endCycle != currentCycle)
			{
				double val = RAM[lb.address];

				if(registerFile[instrucionQueue[lb.instructionIndex].destination].qI == lb.name)
					registerFile[instrucionQueue[lb.instructionIndex].destination].writeValue(val);
				instrucionQueue[lb.instructionIndex].writeCycle = currentCycle;
				writeReservations(lb.name, val);
				lb.busy = false;
				return;
			}
	}

	public void writeReservations(SourceQ reservationName, double val){
		for(ReservationStation rs : addStation)
			if(rs.busy)
				writeReservation(reservationName, val, rs);
		for(ReservationStation rs : mulStation)
			if(rs.busy)
				writeReservation(reservationName, val, rs);

		for(StoreBuffer sb : storeStation)
			if(sb.busy && sb.qI==reservationName)
			{
				sb.qI = SourceQ.ZERO;
				sb.val = val;
			}
	}
	public void writeReservation(SourceQ reservationName, double val, ReservationStation rs){
		if(rs.qJ == reservationName)
		{
			rs.qJ = SourceQ.ZERO;
			rs.vJ = val;
		}

		if(rs.qK == reservationName)
		{
			rs.qK = SourceQ.ZERO;
			rs.vK = val;
		}
	}

	public void performCycle(){
		currentCycle++;
		issue();
		execute();
		writeResult();
	}

	public boolean isDone(){
		if(currentIndex<instrucionQueue.length)return false;
		for(ReservationStation rs : addStation)
			if(rs.busy)return false;
		for(ReservationStation rs : mulStation)
			if(rs.busy) return false;
		for(LoadBuffer lb : loadStation)
			if(lb.busy)return false;
		for(StoreBuffer sb : storeStation)
			if(sb.busy)return false;
		return true;
	}

	public static void main(String[] args) throws IOException {
		String program = "L.D F0,1\n" +
				"S.D F1,1";
//				"ADD.D F2,F0,F1\n" +
//				"ADD.D F2,F2,F2\n"+
//				"ADD.D F2,F2,F2\n"+
//				"ADD.D F2,F2,F2\n"+
//				"S.D F2,0\n";

		Tomasulo architecture = new Tomasulo(program);
		System.out.println(Arrays.toString(architecture.instrucionQueue));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(!architecture.isDone())
		{
//			String inp = br.readLine();
			architecture.performCycle();
			System.out.println(architecture.currentCycle);
			System.out.println("instruction queue");
			for(int i=0;i<architecture.instrucionQueue.length;i++) {
				System.out.println();
			}
			System.out.println("RAM");
			System.out.println(Arrays.toString(architecture.RAM));


			System.out.println("register file");
			System.out.println(Arrays.toString(architecture.registerFile));

			System.out.println("Add stations");
			System.out.println(Arrays.toString(architecture.addStation));

			System.out.println("Mul stations");
			System.out.println(Arrays.toString(architecture.mulStation));

			System.out.println("load buffers");
			System.out.println(Arrays.toString(architecture.loadStation));

			System.out.println("store buffers");
			System.out.println(Arrays.toString(architecture.storeStation));

		}

	}
}
