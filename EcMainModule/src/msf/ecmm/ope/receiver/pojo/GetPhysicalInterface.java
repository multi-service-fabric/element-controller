/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;

/**
 * Physical IF Information Acquisition
 */
public class GetPhysicalInterface extends AbstractResponseMessage {

  /** Physical IF Information */
  private PhysicalIf physicalIf;

  /**
   * Getting physical IF information.
   *
   * @return physical IF information
   */
  public PhysicalIf getPhysicalIf() {
    return physicalIf;
  }

  /**
   * Setting physical IF information.
   *
   * @param physicalIf
   *          physical IF information
   */
  public void setPhysicalIf(PhysicalIf physicalIf) {
    this.physicalIf = physicalIf;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetPhysicalInterface [physicalIf=" + physicalIf + "]";
  }

}
