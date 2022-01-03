package pack;

import java.util.LinkedList;

public class StringParser {
	
	LinkedList<Instruction> instructions;
	
	//takes instructions in the form of (S.D F1,100) or (ADD.D F0,F0,F1)
	//the index of the first register in all instructions is the destination, followed by 1st source, and then
	//2nd source if the second source is available
	//loads and stores have the effective address directly 
	//The operands registers shall have no spaces between them but rather commas
	//The instruction and the operands are separated with a white space

	public static LinkedList<Instruction> parse(String MIPScode) {
		LinkedList<Instruction> instructions = new LinkedList<Instruction>();
		String[] lines = MIPScode.split("\n");
		for(String line : lines) {
			OpCode opCode;
			String[] instructionSplit = line.split(" ");
			String[] instructionRegs = instructionSplit[1].split(",");
			int source1 = -1, source2 = -1, destination = -1;
			switch(instructionSplit[0]) {
				case "L.D": opCode = OpCode.LOAD; break;
				case "S.D": opCode = OpCode.STORE; break;
				case "ADD.D": opCode = OpCode.ADD; break;
				case "SUB.D": opCode = OpCode.SUB; break;
				case "DIV.D": opCode = OpCode.DIV; break;
				case "MUL.D": opCode = OpCode.MUL; break;
				default: opCode = null;
			}
			destination = Integer.parseInt(instructionRegs[0].substring(1));
			if(opCode == OpCode.LOAD || opCode == OpCode.STORE) {
				source1 = Integer.parseInt(instructionRegs[1]);
			}else {
				source1 = Integer.parseInt(instructionRegs[1].substring(1));
				source2 = Integer.parseInt(instructionRegs[2].substring(1));
			}
			instructions.addLast(new Instruction(opCode, source1, source2, destination));
		}
		return instructions;
	}
	
	public static void main(String[] args) {
		LinkedList<Instruction> instructions= StringParser.parse("L.D F1,100\nS.D F2,200\nADD.D F11,F12,F13");
		for(Instruction i : instructions) {
			System.out.println(i);
		}
	}
}
