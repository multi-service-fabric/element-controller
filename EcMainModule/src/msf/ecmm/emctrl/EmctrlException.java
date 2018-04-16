/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

/**
 * Exception occurs in EM access. <br>
 * Without releasing exception in the normal error response, notify with response message
 */
public class EmctrlException extends Exception {

  /**
   * Constructor
   *
   * @param string
   *          error factor string
   */
  public EmctrlException(String string) {
    super(string);
  }

  /** Serial Version UID */
  private static final long serialVersionUID = 1L;
}
