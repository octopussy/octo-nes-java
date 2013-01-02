package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class StoreRegisterPerformer implements InstructionPerformer {
	private static final int A_ZERO_PAGE = 0x85;
	private static final int A_ZERO_PAGE_X = 0x95;
	private static final int A_ABS = 0x80;
	private static final int A_ABS_X = 0x90;
	private static final int A_ABS_Y = 0x99;
	private static final int A_IND_X = 0x81;
	private static final int A_IND_Y = 0x91;

	private static final int X_ZERO_PAGE = 0x86;
	private static final int X_ZERO_PAGE_Y = 0x96;
	private static final int X_ABS = 0x8E;

	private static final int Y_ZERO_PAGE = 0x84;
	private static final int Y_ZERO_PAGE_X = 0x94;
	private static final int Y_ABS = 0x8C;

	@Override
	public int[] getOpCodeList() {
		return new int[] {X_ABS};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case X_ABS:
				byte value = context.getXRegisterValue();
				int address = context.consumeAbsAddress();
				context.storeByteInMemory(address, value);
				break;
		}
	}
}
