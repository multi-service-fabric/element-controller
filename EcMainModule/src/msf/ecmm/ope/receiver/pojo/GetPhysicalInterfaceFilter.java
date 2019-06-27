/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfFilter;

/**
 * Physical IF Filter Information Acquisition.
 */
public class GetPhysicalInterfaceFilter extends AbstractResponseMessage {
  private PhysicalIfFilter physicalIfFilter;

  /**
   * Getting the Phsical IF Filter Information.
   *
   * @return Physical IF Filter Information
   */
  public PhysicalIfFilter getPhysicalIfFilter() {
    return physicalIfFilter;
  }

  /**
   * Setting the Physical IF Filter Information.
   *
   * @param physicalIfFilter
   *          Physical IF Filter Information
   */
  public void setPhysicalIfFilter(PhysicalIfFilter physicalIfFilter) {
    this.physicalIfFilter = physicalIfFilter;
  }

  @Override
  public String toString() {
    return "GetPhysicalInterfaceFilter[physicalIfFilter" + physicalIfFilter + "]";
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
    if (physicalIfFilter == null) {
      throw new CheckDataException();
    }
  }
}
