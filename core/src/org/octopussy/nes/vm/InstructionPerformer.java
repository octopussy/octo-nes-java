package org.octopussy.nes.vm;

import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public interface InstructionPerformer {
	int[] getOpCodeList();

	void perform(int opCode, ProgramContext context);
}
