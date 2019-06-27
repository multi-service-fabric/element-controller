/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Notifying controller status (log).
 */
public class ControllerLog {

  /** Log level. */
  private String controllerType;

  /** Log level. */
  private String logLevel;

  /** Notified log contents. */
  private List<String> message = new ArrayList<String>();

  /**
   * Getting controller type.
   *
   * @param controllerType
   */
  public String getControllerType() {
    return controllerType;
  }

  /**
   * Setting controller type.
   *
   * @param controllerType
   *          controllerType
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
   * Getting log contents.
   *
   * @return notified log contents
   */
  public List<String> getMessage() {
    return message;
  }

  /**
   * Setting log contents.
   *
   * @param message
   *          notified log contents
   */
  public void setMessage(List<String> message) {
    this.message = message;
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (controllerType == null || !controllerType.equals(CommonDefinitions.EM)) {
      throw new CheckDataException();
    }
    if (logLevel == null
        || (!logLevel.equals(CommonDefinitions.LOG_LV_ERROR)
            && !logLevel.equals(CommonDefinitions.LOG_LV_WARNING)
            && !logLevel.equals(CommonDefinitions.LOG_LV_INFO))) {
      throw new CheckDataException();
    }
    if (message == null) {
      throw new CheckDataException();
    }

  }

  @Override
  public String toString() {
    return "ControllerLog [controllerType=" + controllerType + ", logLevel=" + logLevel + ", message=" + message + "]";
  }
}
