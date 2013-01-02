package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class LoadRegisterPerformer implements InstructionPerformer {
	private static final int A_IMMEDIATE = 0xA9;
	private static final int A_ZERO_PAGE = 0xA5;
	private static final int A_ZERO_PAGE_X = 0xB5;
	private static final int A_ABS = 0xAD;
	private static final int A_ABS_X = 0xBD;
	private static final int A_ABS_Y = 0xB9;
	private static final int A_IND_X = 0xA1;
	private static final int A_IND_Y = 0xB1;

	private static final int X_IMMEDIATE = 0xA2;
	private static final int X_ZERO_PAGE = 0xA6;
	private static final int X_ZERO_PAGE_Y = 0xB6;
	private static final int X_ABS = 0xAE;
	private static final int X_ABS_Y = 0xBE;

	private static final int Y_IMMEDIATE = 0xA0;
	private static final int Y_ZERO_PAGE = 0xA4;
	private static final int Y_ZERO_PAGE_X = 0xB4;
	private static final int Y_ABS = 0xAC;
	private static final int Y_ABS_X = 0xBC;

	@Override
	public int[] getOpCodeList() {
		return new int[] {X_IMMEDIATE};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case X_IMMEDIATE:
				byte value = context.consumeByte();
				context.setXRegisterValue(value);
				break;
		}
	}
}
