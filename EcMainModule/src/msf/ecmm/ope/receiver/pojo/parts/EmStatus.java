/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * EM Information
 */
public class EmStatus {

  /** Service Start-up Status */
  private String status;

  /**
   * Getting service start-up status.
   *
   * @return service start-up status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Setting service start-up status.
   *
   * @param status
   *           service start-up status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "EmStatus [status=" + status + "]";
  }

}
