package org.octopussy.nes;

import org.apache.log4j.Logger;
import org.octopussy.nes.mappers.MemoryMapper;

/**
 * @author octopussy
 */
public final class ProgramContext {
	// status register bits
	public static final short CARRY_FLAG = 1;
	public static final short ZERO_FLAG = 1 << 1;
	public static final short INTERRUPT_DISABLED_FLAG = 1 << 2;
	public static final short DECIMAL_MODE = 1 << 3;
	public static final short OVERFLOW_FLAG = 1 << 6;
	public static final short SIGN_FLAG = 1 << 7;

	private final MemoryMapper mMemoryMapper;


	private int mProgramCounter;
	private int mStackPointer;
	private short mStatusRegister;
	private byte mXRegister;
	private byte mAccumulatorRegister;

	public ProgramContext(MemoryMapper memoryMapper) {
		mMemoryMapper = memoryMapper;
		mProgramCounter = memoryMapper.getEntryPoint();
	}

	public int getProgramCounter() {
		return mProgramCounter;
	}

	public int consumeOpCode() {
		return consumeByte() & 0xff;
	}

	public byte consumeByte() {
		byte result = mMemoryMapper.getByte(mProgramCounter);
		++mProgramCounter;
		return result;
	}

	public int consumeAbsAddress() {
		short word = mMemoryMapper.getWord(mProgramCounter);
		mProgramCounter += 2;
		return word & 0xffff;
	}

	public void setStatusRegisterBit(short fields, boolean value) {
		if (value)
			mStatusRegister |= fields;
		else
			mStatusRegister &= ~fields;
	}

	public boolean getStatusRegisterBit(byte field) {
		return (mStatusRegister & (field)) != 0;
	}

	public void setXRegisterValue(byte value) {
		mXRegister = value;
	}

	public byte getXRegisterValue() {
		return mXRegister;
	}

	public byte getByteInMemory(int address) {
		return mMemoryMapper.getByte(address);
	}

	public void storeByteInMemory(int address, byte value) {
		mMemoryMapper.writeByte(address, value);
	}

	public void setStackPointerValue(byte value) {
		mStackPointer = value;
	}

	public byte getAccumulatorRegister() {
		return mAccumulatorRegister;
	}
}
