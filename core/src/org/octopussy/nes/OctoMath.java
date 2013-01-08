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

	public static byte decByte(byte src) {
		int value = src & 0xff;
		--value;
		return (byte)value;
	}
}
