package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author octopussy
 */
public class ADC implements InstructionPerformer {
	private static final int IMMEDIATE = 0x69;
	private static final int ZERO_PAGE = 0x65;
	private static final int ZERO_PAGE_X = 0x75;
	private static final int ABS = 0x60;
	private static final int ABS_X = 0x70;
	private static final int ABS_Y = 0x79;
	private static final int INDIRECT_X = 0x61;
	private static final int INDIRECT_Y = 0x71;

	@Override
	public int[] getOpCodeList() {
		return new int[]{ IMMEDIATE, ZERO_PAGE, ZERO_PAGE_X, ABS, ABS_X, ABS_Y, INDIRECT_X, INDIRECT_Y};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		throw new NotImplementedException();
	}
}
