package org.octopussy.nes;

import java.io.*;

/**
 * @author octopussy
 */
public class Main {
  public RomInfo open(String path) throws OctoAppException {
    final File file = new File(path);
    try {
      InputStream fis = null;
      try {
        fis = new BufferedInputStream(new FileInputStream(file));

        final String sign = readSign(fis);
        final byte format = readFormat(fis);

        return new RomInfo(sign, format);
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

  private String readSign(InputStream fis) throws IOException{
    byte[] buff = new byte[3];

    int read = fis.read(buff, 0, 3);
    if (read != -1)
      return new String(buff);
    return null;
  }

  private byte readFormat(InputStream fis) throws IOException {
    byte[] buff = new byte[1];
    int read = fis.read(buff, 0, 1);
    if (read != -1)
      return buff[0];
    return 0;
  }
}
