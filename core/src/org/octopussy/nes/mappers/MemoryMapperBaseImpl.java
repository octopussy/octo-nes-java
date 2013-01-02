package org.octopussy.nes.mappers;

import org.octopussy.nes.OctoAppException;
import org.octopussy.nes.RomHeader;

import java.util.List;

/**
 * @author octopussy
 */
public abstract class MemoryMapperBaseImpl implements MemoryMapper {
	protected static final int RAM_SIZE = 0xffff;

	protected final byte[] mRam;
	protected final RomHeader header;
	protected final List<MemoryBank> prgBanks;
	protected final List<MemoryBank> chrBanks;

	protected MemoryMapperBaseImpl(RomHeader header, List<MemoryBank> prgBanks, List<MemoryBank> chrBanks) {
		this.header = header;
		this.prgBanks = prgBanks;
		this.chrBanks = chrBanks;
		mRam = new byte[RAM_SIZE];
	}

	@Override
	public byte getByte(int ptr) {
		ensureMemRange(ptr, 1);
		return mRam[ptr];
	}

	@Override
	public short getWord(int ptr) {
		ensureMemRange(ptr, 2);
		int result = ((mRam[ptr + 1] << 8) | mRam[ptr]) ;
		return (short)(result & 0xffff);
	}

	@Override
	public void writeByte(int address, byte value){
		ensureMemRange(address, 1);
		mRam[address] = value;
	}

	protected void swapBankIntoMemory(MemoryBank sourceBank, int memOffset) {
		ensureMemRange(memOffset, sourceBank.getSize());
		System.arraycopy(sourceBank.getData(), 0, mRam, memOffset, sourceBank.getSize());
	}

	private void ensureMemRange(int ptr, int size) {
		if (ptr < 0 || ptr + size > RAM_SIZE)
			throw new ArrayIndexOutOfBoundsException();
	}
}
