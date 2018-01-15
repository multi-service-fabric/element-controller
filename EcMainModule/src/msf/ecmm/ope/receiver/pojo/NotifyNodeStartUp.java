/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;

/**
 * Device Start-up Notification
 */
public class NotifyNodeStartUp extends AbstractRestMessage {

  /** Management IF Address of Started Device */
  private String managementIfAddress;

  /**
   * Getting management IF address of started device.
   *
   * @return management IF address of started device
   */
  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  /**
   * Setting management IF address of started device.
   *
   * @param managementIfAddress
   *          management IF address of started device
   */
  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "NotifyNodeStartUp [managementIfAddress=" + managementIfAddress + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (managementIfAddress == null) {
      throw new CheckDataException();
    }
  }

}
