/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Notifying controller satsus(server).
 */
public class ControllerServer {

  /** Controller type. */
  private String controllerType;

  /** System type. */
  private String systemType;

  /** Failure information. */
  private FailureInfo failureInfo;

  /**
   * Getting controller type.
   *
   * @return controller type
   */
  public String getControllerType() {
    return controllerType;
  }

  /**
   * Setting controller type.v
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
   * Setting system type
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
  public FailureInfo getFailureInfo() {
    return failureInfo;
  }

  /**
   * Setting failure information..
   *
   * @param failureInfo 
   *          failure information
   */
  public void setFailureInfo(FailureInfo failureInfo) {
    this.failureInfo = failureInfo;
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

    if (controllerType == null || controllerType.length() == 0
        || (!controllerType.equals(CommonDefinitions.EM) && !controllerType.equals(CommonDefinitions.EM_SBY))) {
      throw new CheckDataException();
    } else if (systemType == null || systemType.length() == 0 || (!systemType.equals(CommonDefinitions.SYSTEM_TYPE_OS)
        && !systemType.equals(CommonDefinitions.SYSTEM_TYPE_CTL_PROCESS))) {
      throw new CheckDataException();
    }

    if (failureInfo != null) {
      failureInfo.check(ope);
    }
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "ControllerServer [controllerType=" + controllerType + ", systemType=" + systemType + ", failureInfo="
        + failureInfo + "]";
  }
}
