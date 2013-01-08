package org.octopussy.nes.mappers;

import org.octopussy.nes.OctoAppException;
import org.octopussy.nes.RomHeader;

import java.util.List;

/**
 * @author octopussy
 */
public class NoBankSwitchMapper extends MemoryMapperBaseImpl {

	public NoBankSwitchMapper(RomHeader header, List<MemoryBank> prgBanks, List<MemoryBank> chrBanks) {
		super(header, prgBanks, chrBanks);

		int bankCopyCounter = 0;
		int memStart = 0x8000;
		while (bankCopyCounter < 2){
			if (bankCopyCounter < prgBanks.size())
				swapBankIntoMemory(prgBanks.get(bankCopyCounter), memStart);
			else
				swapBankIntoMemory(prgBanks.get(0), memStart);

			memStart += 0x4000;
			++bankCopyCounter;
		}
	}

	@Override
	public int getEntryPoint() {
		return 0x8000;
	}
}
