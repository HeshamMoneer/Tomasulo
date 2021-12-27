package pack;

import java.util.LinkedList;

public class Tomasulo {
	double[] RAM;
	Register[] registerFile;
	StoreBuffer[] storeStation;
	LoadBuffer[] loadStation;
	ReservationStation[] addStation;
	ReservationStation[] mulStation;
	int cycle, addLatency, subLatency, mulLatency, divLatency;
	LinkedList<Instruction> instrucionQueue;
}
