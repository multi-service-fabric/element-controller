/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifying controller status(Log).
 */
public class ControllerLogToFc {

  /** Controller type. */
  private String controllerType;

  /** Log level. */
  private String logLevel;

  /** Notified log contents. */
  private List<String> log = new ArrayList<String>();

  /**
   * Getting controller type.
   *
   * @return controller type
   */
  public String getControllerType() {
    return controllerType;
  }

  /**
   * Setting controller type.
   *
   * @param controllerType
   *          controller type
   */
  public void setControllerType(String controllerType) {
    this.controllerType = controllerType;
  }

  /**
   * Getting log level.
   *
   * @return log level
   */
  public String getLogLevel() {
    return logLevel;
  }

  /**
   * Setting log level.
   *
   * @param logLevel
   *          log level
   */
  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Getting log level.
   *
   * @return notified log contents
   */
  public List<String> getLog() {
    return log;
  }

  /**
   * Getting log level.
   *
   * @param log
   *          notified log contents
   */
  public void setLog(List<String> log) {
    this.log = log;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "ControllerLogToFc [controllerType=" + controllerType + ", logLevel=" + logLevel + ", log=" + log + "]";
  }
}
