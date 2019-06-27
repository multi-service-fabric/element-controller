/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Log Data Information
 */
public class LogData {

  /**
   *  Log Message
   */
  private String message;

  /**
   * Getting log message.
   *
   * @return log message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Setting log message.
   *
   * @param message
   *           log message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "LogData [message=" + message + "]";
  }

}
