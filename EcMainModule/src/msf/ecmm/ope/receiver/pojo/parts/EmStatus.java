/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
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
   * @return service start-up status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Setting service start-up status.
   * @param status service start-up status
   */
  public void setStatus(String status) {
    this.status = status;
  }

}
