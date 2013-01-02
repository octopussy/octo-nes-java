package org.octopussy.nes;

/**
 * @author octopussy
 */
public class OctoMath {
	public static byte incByte(byte src){
		int value = src & 0xff;
		++value;
		return (byte)value;
	}

	public static byte unsignedAndBytes(byte l, byte r) {
		int il = l & 0xff;
		int ir = r & 0xff;
		return (byte) (l & r);
	}
}
