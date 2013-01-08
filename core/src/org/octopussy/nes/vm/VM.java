package org.octopussy.nes.vm;

import org.octopussy.nes.OctoAppException;
import org.octopussy.nes.ProgramContext;
import org.octopussy.nes.mappers.MemoryMapper;

/**
 * @author octopussy
 */
public final class VM {
	private CPU mCPU;
	private PPU mPPU;
	private final MemoryMapper mMemoryMapper;

	public VM(MemoryMapper memoryMapper) throws OctoAppException {
		mMemoryMapper = memoryMapper;
	}

	public void start() {
		mPPU = new PPU(mMemoryMapper);
		mCPU = new CPU(mMemoryMapper);

		while (mCPU.performNextInstruction()) {
			mPPU.tick();
		}
	}
}
