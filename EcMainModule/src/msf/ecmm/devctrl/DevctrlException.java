/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

/**
 * Abnormality occurs at device operation relations
 */
public class DevctrlException extends Exception {

  /**
   * Cobstructor
   *
   * @param string
   *          Error contents
   */
  public DevctrlException(String string) {
    super(string);
  }

}
