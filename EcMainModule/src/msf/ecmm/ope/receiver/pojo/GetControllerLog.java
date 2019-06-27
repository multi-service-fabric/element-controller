/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.EcEmLog;

/**
 * Controller Log Acquisition
 */
public class GetControllerLog extends AbstractResponseMessage {

  /** Log Information. */
  private EcEmLog ecemLog;

  /**
   * Getting log acquisition information.
   * 
   * @return log acquisition information
   */
  public EcEmLog getEcemLog() {
    return ecemLog;
  }

  /**
   * Setting log acquisition information
   * 
   * @param ecemLog
   *          log acquisition information
   */
  public void setEcem_log(EcEmLog ecemLog) {
    this.ecemLog = ecemLog;
  }

  @Override
  public String toString() {
    return "GetControllerLog [ecemLog=" + ecemLog + "]";
  }
}
