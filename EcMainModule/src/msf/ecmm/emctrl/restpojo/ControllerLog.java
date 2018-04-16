/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo;

import msf.ecmm.emctrl.restpojo.parts.LogConditions;
import msf.ecmm.emctrl.restpojo.parts.LogInformation;

/**
 * Controller Log Acquisition
 */
public class ControllerLog extends AbstractResponse {

  /**
   * Log Acquisition Condition Information
   */
  private LogConditions conditions;

  /**
   * Log Acquisition Information
   * (Tt is assumed to be acquired from EM, but used in both EC and Em)
   */
  private LogInformation em_log;

  /**
   * Getting log acquisition condition information.
   * @return log acquisition condition information
   */
  public LogConditions getConditions() {
    return conditions;
  }

  /**
   * Setting log acquisition condition information.
   * @param conditions log acquisition condition information
   */
  public void setConditions(LogConditions conditions) {
    this.conditions = conditions;
  }

  /**
   * Getting log acquisition information.
   * @return log acquisition information
   */
  public LogInformation getEm_log() {
    return em_log;
  }

  /**
   * Setting log acquisition information.
   * @param logInfo log acquisition information
   */
  public void setEm_log(LogInformation logInfo) {
    this.em_log = logInfo;
  }

  /* (Non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ControllerLog [conditions=" + conditions + ", logInfo=" + em_log + "]";
  }
}
