/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl;

/**
 * REST Client Exception
 */
public class RestClientException extends Exception {

  /** Error Code */
  private int code = COMMON_NG;

  /** nore configured */
  public static final int NOT_SET = -1;
  /** common NG */
  public static final int COMMON_NG = 0;
  /** connect NG */
  public static final int CONNECT_NG = 1;
  /** time out */
  public static final int TIMEOUT = 2;
  /** error response received (>3xx) */
  public static final int ERROR_RESPONSE = 3;
  /** JSON format NG */
  public static final int JSON_FORMAT_NG = 4;

  /**
   * REST Client Exception
   *
   * @param code
   *          error code
   */
  public RestClientException(int code) {
    this.code = code;
  }

  /**
   * Getting error code
   *
   * @return error code
   */
  public int getCode() {
    return code;
  }
}
