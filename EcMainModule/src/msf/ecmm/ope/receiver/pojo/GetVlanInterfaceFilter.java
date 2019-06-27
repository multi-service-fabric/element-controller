/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfFilter;

/**
 * Getting VLAN IF Filter Information.
 */
public class GetVlanInterfaceFilter extends AbstractResponseMessage {

  /** VLANIF Filter Information. */
  private VlanIfFilter vlanIfFilter;

  /**
   * Getting VLANIF Filter Information.
   *
   * @return VLANIF Filter Information
   */
  public VlanIfFilter getVlanIfFilter() {
    return vlanIfFilter;
  }

  /**
   * Setting VLANIF Filter Information.
   *
   * @param vlanIfFilter
   *          VLANIF Filter Information
   */
  public void setVlanIfFilter(VlanIfFilter vlanIfFilter) {
    this.vlanIfFilter = vlanIfFilter;
  }

  @Override
  public String toString() {
    return "GetVlanInterfaceFilter[vlanIfFilter" + vlanIfFilter + "]";
  }

  /**
   * Checking input parameter.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vlanIfFilter == null) {
      throw new CheckDataException();
    }
  }
}
