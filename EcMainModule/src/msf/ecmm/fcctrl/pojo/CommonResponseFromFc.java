/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

/**
 * Common Response
 */
public class CommonResponseFromFc extends AbstractResponse {

  /** A value represents the detail of error occurred in EC */
  String errorCode = "";

  /**
   * Getting the value represents the detail of error occurred in EC.
   *
   * @return the value represents the detail of error occurred in EC
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * Setting the value represents the detail of error occurred in EC.
   *
   * @param errorCode
   *          the value represents the detail of error occurred in EC
   */
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
