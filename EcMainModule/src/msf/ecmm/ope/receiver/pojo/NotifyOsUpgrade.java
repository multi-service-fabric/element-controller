/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;

/**
 * Class for notifiying the completion of OS upgrade process
 */
public class NotifyOsUpgrade extends AbstractRestMessage {

  /*x The address of the management-IF in the OS-upgraded node. */
  private String managementIfAddress;

  /**
   * The address of the management-IF in the OS-upgraded node is acquired.
   *
   * @return The address of the management-IF in the OS-upgraded node.
   */
  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  /**
   * The address of the management-IF in the OS-upgraded node is set.
   *
   * @param managementIfAddress
   *          The address of the management-IF in the OS-upgraded node.
   */
  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NotifyOsUpgrade [managementIfAddress=" + managementIfAddress + "]";
  }

  /**
   * The input paramter is checked.
   *
   * @param ope
   *          The operation type
   * @throws CheckDataException
   *          The input paramter error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (managementIfAddress == null) {
      throw new CheckDataException();
    }
  }
}
