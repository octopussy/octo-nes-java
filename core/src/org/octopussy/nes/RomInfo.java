package org.octopussy.nes;

/**
 * @author octopussy
 */
public final class RomInfo {
  public static final RomInfo INVALID_ONE = new RomInfo("CRAP", (byte)0);

  public static final String PROPER_SIGN = "NES";
  public static final byte PROPER_FORMAT = 0x1a;
  private final String mSign;
  private final byte mFormat;

  RomInfo(String sign, byte format){
    mSign = sign;
    mFormat = format;
  }

  public boolean isValid() {
    return mSign.equals(PROPER_SIGN) && mFormat == PROPER_FORMAT;
  }

  public String getSign() {
    return mSign;
  }

  public byte getFormat() {
    return mFormat;
  }
}
