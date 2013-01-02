package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class TransferPerformer implements InstructionPerformer{
	private static final int TAX = 0xAA; // A -> X
	private static final int TAY = 0xA8; // A -> Y
	private static final int TSX = 0xBA; // S -> X
	private static final int TXA = 0x8A; // X -> A
	private static final int TXS = 0x9A; // X -> S
	private static final int TYA = 0x98; // Y -> A

	@Override
	public int[] getOpCodeList() {
		return new int[] {TXS};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case TXS:
				byte value = context.getXRegisterValue();
				context.setStackPointerValue(value);
				break;
		}
	}
}
