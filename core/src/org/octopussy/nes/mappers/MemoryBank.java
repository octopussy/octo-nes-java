package org.octopussy.nes.mappers;

/**
 * @author octopussy
 */
public class MemoryBank {
	private final byte[] mData;

	public MemoryBank(byte[] data) {
		mData = data;
	}

	public int getSize() {
		return mData.length;
	}

	public byte[] getData() {
		return mData;
	}
}
