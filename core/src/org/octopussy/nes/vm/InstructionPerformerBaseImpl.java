package org.octopussy.nes.vm;

import org.octopussy.nes.OctoMath;
import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public abstract class InstructionPerformerBaseImpl implements InstructionPerformer {
	protected void setSignFlag(ProgramContext con, byte value) {
		boolean isNegative = ((value >> 7) & 0x1) != 0;
		con.setStatusRegisterBit(ProgramContext.SIGN_FLAG, isNegative);
	}

	protected void setZeroFlag(ProgramContext con, byte value) {
		con.setStatusRegisterBit(ProgramContext.ZERO_FLAG, value == 0);
	}

	protected byte incValue(ProgramContext con, byte value){
		byte newValue = OctoMath.incByte(value);
		setSignFlag(con, newValue);
		setZeroFlag(con, newValue);

		return newValue;
	}
}
