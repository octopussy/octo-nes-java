/*
 * Copyright (c) 2013 by octopussy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.octopussy.nes.vm;

import org.octopussy.nes.Logger;
import org.octopussy.nes.OctoMath;
import org.octopussy.nes.mappers.MemoryMapper;
import org.octopussy.nes.mappers.MemoryRegisterHandler;

/**
 * @author octopussy
 */
public class PPU implements MemoryRegisterHandler{
	private static final int PPU_IO_OFFSET = 0x2000;

	private static final int PPU_CONTROL_REGISTER1 = PPU_IO_OFFSET;
	private static final int PPU_CONTROL_REGISTER2 = PPU_IO_OFFSET + 0x1;
	private static final int PPU_STATUS_REGISTER = PPU_IO_OFFSET + 0x2;

	private static final short VBLANK_BIT = 1 << 7;

	private final MemoryMapper mCPUMem;

	public PPU(MemoryMapper cpuMemoryMapper) {
		mCPUMem = cpuMemoryMapper;
		mCPUMem.addMemoryRegisterHandler(PPU_IO_OFFSET, 0x7, this);
		mCPUMem.setByte(PPU_STATUS_REGISTER, (byte) (1 << 7), false);
	}

	@Override
	public void setByte(int address, byte value) {
		Logger.debug("PPU register write: 0x" + Integer.toHexString(address) + " = " + Integer.toBinaryString(value & 0xff));
	}

	@Override
	public byte getByte(int address) {
		if (address == PPU_STATUS_REGISTER)
			resetVBlank();

		return mCPUMem.getByte(address, false);
	}

	public boolean tick() {
		return false;
	}

	private void resetVBlank() {
		setStatusRegister(OctoMath.resetBit(getStatusRegister(), (byte)VBLANK_BIT));
	}

	private byte getStatusRegister() {
		return mCPUMem.getByte(PPU_STATUS_REGISTER, false);
	}

	private void setStatusRegister(byte value) {
		mCPUMem.setByte(PPU_STATUS_REGISTER, value, false);
	}
}
