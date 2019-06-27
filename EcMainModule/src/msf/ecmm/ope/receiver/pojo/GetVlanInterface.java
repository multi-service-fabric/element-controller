/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.VlanIf;

/**
 * VLAN IF Information Acquisition.
 */
public class GetVlanInterface extends AbstractResponseMessage {

  /** VLAN IF Information. */
  private VlanIf vlanIf;

  /**
   * Getting VLAN IF information.
   *
   * @return VLAN IF information
   */
  public VlanIf getVlanIf() {
    return vlanIf;
  }

  /**
   * Setting VLAN IF information.
   *
   * @param vlanIf
   *          VLAN IF information
   */
  public void setVlanIf(VlanIf vlanIf) {
    this.vlanIf = vlanIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetVlanInterface [vlanIf=" + vlanIf + "]";
  }
}
