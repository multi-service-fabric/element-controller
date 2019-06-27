/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Notifying controller satsus(server).
 */
public class ControllerServerToFc {

  /** Controller type. */
  private String controllerType;

  /** System type. */
  private String systemType;

  /** Failure information. */
  private FailureInfoToFc failureInfo;

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
   * Getting system type.
   *
   * @return system type
   */
  public String getSystemType() {
    return systemType;
  }

  /**
   * Setting system type.
   *
   * @param systemType
   *          system type
   */
  public void setSystemType(String systemType) {
    this.systemType = systemType;
  }

  /**
   * Getting failure information.
   *
   * @return failure information
   */
  public FailureInfoToFc getFailureInfo() {
    return failureInfo;
  }

  /**
   * Setting failure information.
   *
   * @param failureInformation
   *          failure information
   */
  public void setFailureInfo(FailureInfoToFc failureInfo) {
    this.failureInfo = failureInfo;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "ControllerServerToFc [controllerType=" + controllerType + ", systemType=" + systemType + ", failureInfo="
        + failureInfo + "]";
  }
}
