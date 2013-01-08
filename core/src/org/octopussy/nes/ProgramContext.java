package org.octopussy.nes;

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
	private byte mStackPointer;
	private short mStatusRegister;
	private byte mXRegister;
	private byte mYRegister;
	private byte mARegister;

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

	public int consumeZeroPageAddress() {
		int address = mMemoryMapper.getByte(mProgramCounter);
		++mProgramCounter;
		return address & 0xff;
	}

	public int consumeAbsAddress() {
		int address = readAbsAddressInMem(mProgramCounter);
		mProgramCounter += 2;
		return address;
	}

	public int readAbsAddressInMem(int ptr) {
		short word = mMemoryMapper.getWord(ptr);
		return word & 0xffff;
	}

	public void setStatusRegisterBit(short fields, boolean value) {
		if (value)
			mStatusRegister |= fields;
		else
			mStatusRegister &= ~fields;
	}

	public boolean getStatusRegisterBit(short field) {
		return (mStatusRegister & (field)) != 0;
	}

	public void setX(byte value) {
		mXRegister = value;
		setZeroFlag(mXRegister);
		setSignFlag(mXRegister);
	}

	public void setY(byte val) {
		mYRegister = val;
		setZeroFlag(mYRegister);
		setSignFlag(mYRegister);
	}

	public void setAcc(byte val) {
		mARegister = val;
		setZeroFlag(mARegister);
		setSignFlag(mARegister);
	}

	public void incMemoryByte(int ptr) {
		int oldValue = mMemoryMapper.getByte(ptr) & 0xff;
		byte newValue = (byte)(oldValue + 1);
		mMemoryMapper.setByte(ptr, newValue);
		setSignFlag(newValue);
		setZeroFlag(newValue);
	}

	public void decMemoryByte(int ptr) {
		int oldValue = mMemoryMapper.getByte(ptr) & 0xff;
		byte newValue = (byte)(oldValue - 1);
		mMemoryMapper.setByte(ptr, newValue);
		setSignFlag(newValue);
		setZeroFlag(newValue);
	}

	public byte getX() {
		return mXRegister;
	}

	public byte getByteInMemory(int address) {
		return mMemoryMapper.getByte(address);
	}

	public void setByteInMemory(int address, byte value) {
		mMemoryMapper.setByte(address, value);
	}

	public short getWordInMemory(int ptr) {
		return mMemoryMapper.getWord(ptr);
	}

	public void storeByteInMemory(int address, byte value) {
		mMemoryMapper.setByte(address, value);
	}

	public byte getSP() {
		return mStackPointer;
	}

	public void setSP(byte value) {
		mStackPointer = value;
	}

	private void setSignFlag(byte value) {
		boolean isNegative = ((value >> 7) & 0x1) != 0;
		setStatusRegisterBit(ProgramContext.SIGN_FLAG, isNegative);
	}

	private void setZeroFlag(byte value) {
		setStatusRegisterBit(ProgramContext.ZERO_FLAG, value == 0);
	}

	public byte getY() {
		return mYRegister;
	}

	public byte getAcc() {
		return mARegister;
	}
}
