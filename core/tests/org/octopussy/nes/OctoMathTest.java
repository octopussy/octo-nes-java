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

package org.octopussy.nes;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author octopussy
 */
public class OctoMathTest {
	@Test
	public void testIncByte() throws Exception {
		Assert.assertEquals(OctoMath.incByte((byte)0xff), 0);
		Assert.assertEquals(OctoMath.incByte((byte)0x0), 1);
		Assert.assertEquals(OctoMath.incByte((byte)127), (byte)128);
	}

	@Test
	public void testDecByte() throws Exception {
		Assert.assertEquals(OctoMath.decByte((byte)0x0), (byte)0xff);
		Assert.assertEquals(OctoMath.decByte((byte)0xff), (byte)0xfe);
		Assert.assertEquals(OctoMath.decByte((byte)128), (byte)127);
	}
}
