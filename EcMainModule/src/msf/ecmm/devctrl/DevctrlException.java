/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

/**
 * Device Operation Related Error Occurs
 */
public class DevctrlException extends Exception {

  /**
   * Constructor
   *
   * @param string
   *          error contents
   */
  public DevctrlException(String string) {
    super(string);
  }

}
