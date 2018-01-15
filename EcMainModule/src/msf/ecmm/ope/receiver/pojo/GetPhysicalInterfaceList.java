/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;

/**
 * Physical IF Information List Acquisition
 */
public class GetPhysicalInterfaceList extends AbstractResponseMessage {

  /** Physical IF Information List */
  private ArrayList<PhysicalIf> physicalIfs = new ArrayList<PhysicalIf>();

  /**
   * Getting physical IF information list.
   *
   * @return physical IF information list
   */
  public ArrayList<PhysicalIf> getPhysicalIfs() {
    return physicalIfs;
  }

  /**
   * Setting physical IF information list.
   *
   * @param physicalIfs
   *          physical IF information list
   */
  public void setPhysicalIfs(ArrayList<PhysicalIf> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetPhysicalInterfaceList [physicalIfs=" + physicalIfs + "]";
  }

}
