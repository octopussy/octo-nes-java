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

import org.octopussy.nes.mappers.MemoryMapper;
import org.octopussy.nes.mappers.MemoryRegisterHandler;

/**
 * @author octopussy
 */
public class MemoryMapperStub implements MemoryMapper {
	private final byte[] mMem;

	public MemoryMapperStub(int size) {
		mMem = new byte[size];
	}

	@Override
	public void setByte(int address, byte value, boolean notifyMemoryHandlers) {
		mMem[address] = value;
	}

	@Override
	public int getEntryPoint() {
		return 0;
	}

	@Override
	public byte getByte(int ptr, boolean notifyMemoryHandlers) {
		return mMem[ptr];
	}

	@Override
	public short getWord(int ptr) {
		return 0;
	}

	@Override
	public void addMemoryRegisterHandler(int offset, int memWindowSize, MemoryRegisterHandler handler) {
	}
}

