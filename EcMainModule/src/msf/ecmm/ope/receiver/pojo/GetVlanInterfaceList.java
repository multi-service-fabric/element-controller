/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.VlanIf;

/**
 * VLAN IF List Information Acquisition.
 */
public class GetVlanInterfaceList extends AbstractResponseMessage {

  /** VLAN IF Information. */
  private ArrayList<VlanIf> vlanIfs = new ArrayList<VlanIf>();

  /**
   * Getting VLAN IF list information.
   *
   * @return VLAN IF list information
   */
  public ArrayList<VlanIf> getVlanIfs() {
    return vlanIfs;
  }

  /**
   * Setting VLAN IF list information.
   *
   * @param vlanIf
   *          VLAN IF list information
   */
  public void setVlanIf(ArrayList<VlanIf> vlanIf) {
    this.vlanIfs = vlanIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetVlanInterfaceList [vlanIfs=" + vlanIfs + "]";
  }
}
