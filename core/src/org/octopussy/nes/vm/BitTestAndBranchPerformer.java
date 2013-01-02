package org.octopussy.nes.vm;

import org.octopussy.nes.OctoMath;
import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class BitTestAndBranchPerformer extends InstructionPerformerBaseImpl {
	private static final int BIT_ABS = 0x2C; // A /\ M == 0 ? Z = 1: Z = 0, M7 -> N, M6 -> V
	@Override
	public int[] getOpCodeList() {
		return new int[0];
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case BIT_ABS:
				byte acValue = context.getAccumulatorRegister();
				int address = context.consumeAbsAddress();
				byte memValue = context.getByteInMemory(address);
				setZeroFlag(context, OctoMath.unsignedAndBytes(acValue, memValue));
				break;
		}
	}
}
