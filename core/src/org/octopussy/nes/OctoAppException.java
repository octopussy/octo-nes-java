package org.octopussy.nes;

/**
 * @author octopussy
 */
public class OctoAppException extends Exception {
  public OctoAppException(String message) {
    super(message);
  }

	public OctoAppException(String message, Throwable cause) {
		super(message, cause);
	}
}
