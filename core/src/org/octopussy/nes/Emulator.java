package org.octopussy.nes;

import org.octopussy.nes.mappers.MemoryBank;
import org.octopussy.nes.mappers.MemoryMapper;
import org.octopussy.nes.mappers.NoBankSwitch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author octopussy
 */
public class Emulator {
	private static final long TRAINER_SIZE = 512;
	private static final int PRG_BANK_SIZE = 0x3fff;
	private static final int CHR_BANK_SIZE = 0x1fff;

	public Emulator() {
	}

	public VM open(String path) throws OctoAppException {
		final File file = new File(path);
		try {
			InputStream fis = null;
			try {
				fis = new BufferedInputStream(new FileInputStream(file));

				final String sign = readString(fis, 3);
				final byte format = readByte(fis);
				final byte prgRomBanksNumber = readByte(fis);
				final byte chrRomVRomBanksNumber = readByte(fis);
				final byte romControlByte1 = readByte(fis);
				final byte romControlByte2 = readByte(fis);
				final byte kbRamBanksNumber = readByte(fis);
				final String reservedBytes = readString(fis, 7);
				int mapperType = ((romControlByte1 >> 4) & 0xf) | (romControlByte2 & 0xf0);

				final RomHeader header = new RomHeader(sign, format, prgRomBanksNumber, chrRomVRomBanksNumber, romControlByte1, romControlByte2,
								kbRamBanksNumber, mapperType);

				if (header.isTrainerPresent()){
					fis.skip(TRAINER_SIZE);
				}

				MemoryMapper mm = createMapper(header, fis);
				if (mm == null){
					throw new OctoAppException("Unknown mapper '" + mapperType + "'");
				}

				return new VM(mm);
			} finally {

				if (fis != null) {
					fis.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw new OctoAppException("ROM file not found", e);
		} catch (IOException e) {
			throw new OctoAppException("ROM file read error", e);
		}
	}

	private MemoryMapper createMapper(RomHeader header, InputStream fis) throws IOException {
		List<MemoryBank> prgBanks = readMemoryBanks(fis, header.getProgramCodeBanksNumber(), PRG_BANK_SIZE);
		List<MemoryBank> chrBanks = readMemoryBanks(fis, header.getCharRomBanksNumber(), CHR_BANK_SIZE);
		switch (header.getMapperType()){
			case 0: return new NoBankSwitch(header, prgBanks, chrBanks);
		}
		return null;
	}

	private static List<MemoryBank> readMemoryBanks( InputStream fis, final int amount, final int bankSize) throws IOException {
		List<MemoryBank> banks = new ArrayList<MemoryBank>();
		int counter = amount;
		while (counter > 0){
			byte[] data = new byte[bankSize];
			fis.read(data, 0, bankSize);
			banks.add(new MemoryBank(data));
			--counter;
		}
		
		return banks;
	}

	private byte readByte(InputStream fis) throws IOException {
		byte[] buff = new byte[1];
		int read = fis.read(buff, 0, 1);
		if (read != -1)
			return buff[0];
		return 0;
	}

	private String readString(InputStream is, int length) throws IOException {
		byte[] buff = new byte[length];

		int read = is.read(buff, 0, length);
		if (read != -1)
			return new String(buff);
		return null;
	}

}
