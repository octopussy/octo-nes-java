package org.octopussy.nes.vm;

import org.octopussy.nes.OctoMath;
import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class IncDecPerformer extends InstructionPerformerBaseImpl{
	private static final int INX = 0xE8;

	@Override
	public int[] getOpCodeList() {
		return new int[] {INX};
	}

	@Override
	public void perform(int opCode, ProgramContext context) {
		switch (opCode){
			case INX:
				byte value = context.getXRegisterValue();
				context.setXRegisterValue(incValue(context, value));
				break;
		}
	}
}
