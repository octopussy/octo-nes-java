package org.octopussy.nes.mappers;

import org.octopussy.nes.OctoAppException;

/**
 * @author octopussy
 */
public interface MemoryMapper {
	void setByte(int address, byte value);

	int getEntryPoint();

	byte getByte(int ptr);

	short getWord(int ptr);

	void addMemoryRegisterHandler(int offset, int memWindowSize, MemoryRegisterHandler handler);
}
