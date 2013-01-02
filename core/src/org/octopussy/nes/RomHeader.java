package org.octopussy.nes;

/**
 * @author octopussy
 */
public final class RomHeader {
  public static final RomHeader INVALID_ONE = new RomHeader();

  public static final String PROPER_SIGN = "NES";
  public static final byte PROPER_FORMAT = 0x1a;

  public static final int MIRRORING_HORIZONTAL = 0;
  public static final int MIRRORING_VERTICAL = 1;
  public static final int MIRRORING_FOUR_SCREEN = 2;

  // header of ROM
  private final String mSign;
  private final byte mFormat;
  private final byte mPrgRomBanksNumber;
  private final byte mChrRomVRomBanksNumber;
  private byte mKbRamBanksNumber;
  private final int mMirroringType;
  private final boolean mSaveRamEnabled;
  private final boolean mTrainerPresent;
	private final int mMapperType;


	RomHeader() {
    this("CRAP", (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte)0, 0);
  }

  RomHeader(String sign, byte format, byte prgRomBanksNumber, byte chrRomVRomBanksNumber,
            byte romControlByte1, byte romControlByte2, byte kbRamBanksNumber, int mapperType){
    mSign = sign;
    mFormat = format;
    mPrgRomBanksNumber = prgRomBanksNumber;
    mChrRomVRomBanksNumber = chrRomVRomBanksNumber;
    mKbRamBanksNumber = kbRamBanksNumber;
		mMapperType = mapperType;

    mMirroringType = checkBit(romControlByte1, 3) ? MIRRORING_FOUR_SCREEN :
            (checkBit(romControlByte1, 0) ? MIRRORING_HORIZONTAL : MIRRORING_VERTICAL);
    mSaveRamEnabled = checkBit(romControlByte1, 1);
    mTrainerPresent = checkBit(romControlByte1, 2);
  }

  private boolean checkBit(byte b, int position) {
    return (b & (1 << position)) != 0;
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

  public byte getProgramCodeBanksNumber() {
    return mPrgRomBanksNumber;
  }

  public byte getCharRomBanksNumber() {
    return mChrRomVRomBanksNumber;
  }

  public int getMirroringType() {
    return mMirroringType;
  }

  public byte getKbRamBanksNumber() {
    return mKbRamBanksNumber;
  }

  public boolean isSaveRamEnabled() {
    return mSaveRamEnabled;
  }

  public boolean isTrainerPresent() {
    return mTrainerPresent;
  }

	public int getMapperType() {
		return mMapperType;
	}
}
