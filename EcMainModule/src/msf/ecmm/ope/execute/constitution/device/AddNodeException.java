/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

/**
 * Device Extention Failure Exception Class.
 */
public class AddNodeException extends Exception {

  /**
   * Device Extention Failure Exception.
   */
  public AddNodeException() {
    super();
  }

  /**
   * Device Extention Failure Exception.
   *
   * @param originException
   *          Originator Exception
   */
  public AddNodeException(Throwable originException) {
    super(originException);
  }

}
