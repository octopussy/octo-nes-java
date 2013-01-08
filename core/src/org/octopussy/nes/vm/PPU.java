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

import org.apache.log4j.Logger;
import org.octopussy.nes.mappers.MemoryMapper;
import org.octopussy.nes.mappers.MemoryRegisterHandler;

/**
 * @author octopussy
 */
public class PPU implements MemoryRegisterHandler{
	public static final int PPU_CONTROL_REGISTER_OFFSET = 0x2000;

	private final MemoryMapper mMemoryMapper;

	public PPU(MemoryMapper memoryMapper) {
		mMemoryMapper = memoryMapper;
		memoryMapper.addMemoryRegisterHandler(PPU_CONTROL_REGISTER_OFFSET, 0x7, this);
		mMemoryMapper.setByte(PPU_CONTROL_REGISTER_OFFSET + 2, (byte)(1 << 7));
	}

	@Override
	public void setByte(int address, byte value) {
		Logger.getRootLogger().debug("PPU register write: " + address + " = " + Integer.toBinaryString(value & 0xff) );
	}

	public void tick() {

	}
}
