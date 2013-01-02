package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class StatusRegisterPerformer implements InstructionPerformer {

	private static final int SEI = 0x78;  // Set interrupt disable bit
	private static final int CLD = 0xD8;  // Clear decimal mode
	private static final int CLC = 0x18;  // Clear carry flag
	private static final int CLI = 0x58;  // Clear interrupt disable bit
	private static final int CLV = 0xB8;  // Clear interrupt disable bit

	@Override
	public int[] getOpCodeList() {
		return new int[]{SEI, CLD, CLC};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case SEI:
				context.setStatusRegisterBit(ProgramContext.INTERRUPT_DISABLED_FLAG, true);
				break;
			case CLD:
				context.setStatusRegisterBit(ProgramContext.DECIMAL_MODE, false);
				break;
			case CLC:
				context.setStatusRegisterBit(ProgramContext.CARRY_FLAG, false);
				break;
			case CLI:
				context.setStatusRegisterBit(ProgramContext.INTERRUPT_DISABLED_FLAG, false);
				break;
			case CLV:
				context.setStatusRegisterBit(ProgramContext.OVERFLOW_FLAG, false);
				break;
		}

	}
}
