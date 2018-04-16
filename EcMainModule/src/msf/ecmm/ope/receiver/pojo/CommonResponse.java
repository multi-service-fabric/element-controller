/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

/**
 * Common Response
 */
public class CommonResponse extends AbstractResponseMessage {

  /** A value which represents the detail of error occurred in EC */
  private String errorCode = "";

  /**
   * Getting the value which represents the detail of error occurred in EC.
   *
   * @return the value which represents the detail of error occurred in EC
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * Setting the value which represents the detail of error occurred in EC.
   *
   * @param errorCode
   *          the value which represents the detail of error occurred in EC
   */
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "CommonResponse [errorCode=" + errorCode + "]";
  }

}
