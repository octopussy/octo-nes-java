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

import junit.framework.Assert;
import org.junit.Test;
import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class InstructionsTest extends InstructionsTestBase{

	@Test
	public void testSetZeroBit(){
		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);
		mContext.setX((byte) 0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.ZERO_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);
		mContext.setY((byte) 0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.ZERO_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);
		mContext.setAcc((byte) 0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.ZERO_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);

		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);
		mMapper.setByte(0, (byte)255, false);
		mContext.incMemoryByte(0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.ZERO_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.ZERO_FLAG, false);
		mMapper.setByte(0, (byte)1, false);
		mContext.decMemoryByte(0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.ZERO_FLAG));
	}

	@Test
	public void testNegativeBit(){
		mContext.setStatusRegisterBit(ProgramContext.SIGN_FLAG, false);
		mContext.setX((byte) 0);
		Assert.assertFalse(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
		mContext.setX((byte) 127);
		Assert.assertFalse(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
		mContext.setX((byte) 128);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
		mContext.setX((byte) 255);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));

		mContext.setY((byte) 0);
		Assert.assertFalse(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
		mContext.setY((byte) 128);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));

		mContext.setAcc((byte) 0);
		Assert.assertFalse(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
		mContext.setAcc((byte) 128);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.SIGN_FLAG, false);
		mMapper.setByte(0, (byte)127, false);
		mContext.incMemoryByte(0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));

		mContext.setStatusRegisterBit(ProgramContext.SIGN_FLAG, false);
		mMapper.setByte(0, (byte)0, false);
		mContext.decMemoryByte(0);
		Assert.assertTrue(mContext.getStatusBit(ProgramContext.SIGN_FLAG));
	}
}
