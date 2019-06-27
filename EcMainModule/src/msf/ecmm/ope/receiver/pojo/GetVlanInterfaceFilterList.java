/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfFilter;

/**
 * Getting list of VLAN IF Filter Information.
 */
public class GetVlanInterfaceFilterList extends AbstractResponseMessage {

  /** List of VLANIF Filter Information. */
  private ArrayList<VlanIfFilter> vlanIfFilters = new ArrayList<>();

  /**
   * Getting list of VLANIF Filter Information.
   *
   * @return List of VLANIF Filter Information
   */
  public ArrayList<VlanIfFilter> getVlanIfFilterList() {
    return vlanIfFilters;
  }

  /**
   * Setting list of VLANIF Filter Information.
   *
   * @param vlanIfFilterList
   *          List of VLANIF Filter Information
   */
  public void setVlanIfFilterList(ArrayList<VlanIfFilter> vlanIfFilterList) {
    this.vlanIfFilters = vlanIfFilterList;
  }

  @Override
  public String toString() {
    return "GetVlanInterfaceFilterList[vlanIfFilterList" + vlanIfFilters + "]";
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
    if (vlanIfFilters == null) {
      throw new CheckDataException();
    }
  }
}
