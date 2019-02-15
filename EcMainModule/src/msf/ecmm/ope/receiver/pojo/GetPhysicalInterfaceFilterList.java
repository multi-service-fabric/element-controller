/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfFilter;

/**
 * Getting list of Physical IF Filter Information.
 */
public class GetPhysicalInterfaceFilterList extends AbstractResponseMessage {
  private ArrayList<PhysicalIfFilter> physicalIfFilters = new ArrayList<>();

  /**
   * Getting list of Physical IF Filter Information.
   *
   * @return  List of Physical IF Filter Information
   */
  public ArrayList<PhysicalIfFilter> getPhysicalIfFilterList() {
    return physicalIfFilters;
  }

  /**
   * Setting list of Physical IF Filter Information.
   *
   * @param physicalIfFilterList
   *          List of Physical IF Filter Information
   */
  public void setPhysicalIfFilterList(ArrayList<PhysicalIfFilter> physicalIfFilterList) {
    this.physicalIfFilters = physicalIfFilterList;
  }

  @Override
  public String toString() {
    return "GetPhysicalInterfaceFilterList[physicalIfFilterList" + physicalIfFilters + "]";
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
    if (physicalIfFilters == null) {
      throw new CheckDataException();
    }
  }
}
