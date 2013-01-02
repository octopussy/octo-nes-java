package org.octopussy.nes;

import org.apache.log4j.Logger;
import org.octopussy.nes.mappers.MemoryMapper;
import org.octopussy.nes.vm.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author octopussy
 */
public final class VM {
	private ProgramContext mContext;
	private final MemoryMapper mMemoryMapper;
	private Map<Integer, InstructionPerformer> mInstructions;

	public VM(MemoryMapper memoryMapper) throws OctoAppException {
		mMemoryMapper = memoryMapper;

		initOpCodes();
	}

	public void start(){
		mContext = new ProgramContext(mMemoryMapper);
		while(performNextInstruction()){
		}
	}

	private boolean performNextInstruction() {
		int opCode = mContext.consumeOpCode();
		if (!mInstructions.containsKey(opCode)){
			Logger.getRootLogger().debug("Unknown oppcode '0x" + Integer.toHexString(opCode) +"'");
			return false;
		}

		final InstructionPerformer instructionPerformer = mInstructions.get(opCode);
		instructionPerformer.perform(opCode, mContext);

		return true;
	}

	private void initOpCodes() throws OctoAppException {
		mInstructions = new HashMap<Integer, InstructionPerformer>();
		addInstruction(new ADC());
		addInstruction(new StatusRegisterPerformer());
		addInstruction(new LoadRegisterPerformer());
		addInstruction(new StoreRegisterPerformer());
		addInstruction(new TransferPerformer());
		addInstruction(new IncDecPerformer());
	}

	private void addInstruction(InstructionPerformer instructionPerformer) throws OctoAppException {
		for (int opCode: instructionPerformer.getOpCodeList()){
			if (mInstructions.containsKey(opCode)){
				throw new OctoAppException("InstructionPerformer already added: " + instructionPerformer.getClass().getSimpleName());
			}

			mInstructions.put(opCode, instructionPerformer);
		}
	}
}
