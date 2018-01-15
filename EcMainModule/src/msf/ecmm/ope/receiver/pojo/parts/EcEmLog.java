/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * EC/EM Log Information
 */
public class EcEmLog {

  /**
   * Log Acquisition Condition Information
   */
  private LogConditions conditions;

  /**
   * EC Log Acquisition Information
   */
  private LogInformation ec_log;

  /**
   * EM Log Acquisition Information
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
   * Getting EC log acquisition information.
   * @return EC log acquisition information
   */
  public LogInformation getEc_log() {
    return ec_log;
  }

  /**
   * Setting EC log acquisition information.
   * @param ec_log EC log acquisition information
   */
  public void setEc_log(LogInformation ec_log) {
    this.ec_log = ec_log;
  }

  /**
   * Getting EM log acquisition information.
   * @return EM log acquisition information
   */
  public LogInformation getEm_log() {
    return em_log;
  }

  /**
   * Setting EM log acquisition information.
   * @param em_log EM log acquisition information
   */
  public void setEm_log(LogInformation em_log) {
    this.em_log = em_log;
  }

}
