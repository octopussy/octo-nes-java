package org.octopussy.nes.vm;

import org.octopussy.nes.OctoAppException;
import org.octopussy.nes.ProgramContext;
import org.octopussy.nes.mappers.MemoryMapper;

/**
 * @author octopussy
 */
public final class VM {
	private ProgramContext mContext;
	private CPU mCpu;
	private final MemoryMapper mMemoryMapper;

	public VM(MemoryMapper memoryMapper) throws OctoAppException {
		mMemoryMapper = memoryMapper;
	}

	public void start() {
		mContext = new ProgramContext(mMemoryMapper);
		mCpu = new CPU(mContext);
		while (performNextInstruction()) {
		}
	}

	private boolean performNextInstruction() {
		int opCode = mContext.consumeOpCode();
		return mCpu.perform(opCode);
	}
}
