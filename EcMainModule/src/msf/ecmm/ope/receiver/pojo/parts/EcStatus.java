/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * EC Information
 */
public class EcStatus {

  /** Service Start-up Status */
  private String status;

  /** Maintenance Blockage Status */
  private String busy;

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

  /**
   * Getting maintenance blockage status.
   *
   * @return maintenance blockage status
   */
  public String getBusy() {
    return busy;
  }

  /**
   * Setting maintenance blockage status.
   *
   * @param busy
   *           maintenance blockage status
   */
  public void setBusy(String busy) {
    this.busy = busy;
  }

  @Override
  public String toString() {
    return "EcStatus [status=" + status + ", busy=" + busy + "]";
  }

}
