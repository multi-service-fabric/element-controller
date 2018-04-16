/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Physical IF Naming Convention Information
 */
public class IfNameRule {

  /** Port Speed */
  private String speed;

  /** Port Name Prefix */
  private String portPrefix;

  /**
   * Getting port speed.
   *
   * @return port speed
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting port speed.
   *
   * @param speed
   *          port speed
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Getting port name prefix.
   *
   * @return port name prefix
   */
  public String getPortPrefix() {
    return portPrefix;
  }

  /**
   * Setting port name prefix.
   *
   * @param portPrefix
   *          port name prefix
   */
  public void setPortPrefix(String portPrefix) {
    this.portPrefix = portPrefix;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "IfNameRule [speed=" + speed + ", portPrefix=" + portPrefix + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param operationType
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {
    if (speed == null) {
      throw new CheckDataException();
    }
    if (portPrefix == null) {
      throw new CheckDataException();
    }
  }
}
