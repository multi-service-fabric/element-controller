/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Device Extention/Change VPN Class.
 */
public class Vpn {

  /** L2/L3VPN Type. */
  private String vpnType;

  /** VPN Configuration When the VPN Type is L3. */
  private L2L3vpn l3vpn;

  /** VPN Configuration When the VPN Type is L2. */
  private L2L3vpn l2vpn;

  /**
   * Getting L2/L3VPN type.
   *
   * @return L2/L3VPN type
   */
  public String getVpnType() {
    return vpnType;
  }

  /**
   * Setting L2/L3VPN type.
   *
   * @param vpnType
   *          L2/L3VPN type
   */
  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  /**
   * Getting VPN Configuration When the VPN Type is L3.
   *
   * @return VPN Configuration When the VPN Type is L3
   */
  public L2L3vpn getL3vpn() {
    return l3vpn;
  }

  /**
   * Setting VPN Configuration When the VPN Type is L3.
   *
   * @param l3vpn
   *          VPN Configuration When the VPN Type is L3
   */
  public void setL3vpn(L2L3vpn l3vpn) {
    this.l3vpn = l3vpn;
  }

  /**
   * Getting VPN Configuration When the VPN Type is L2.
   *
   * @return VPN Configuration When the VPN Type is L2
   */
  public L2L3vpn getL2vpn() {
    return l2vpn;
  }

  /**
   * Setting VPN Configuration When the VPN Type is L2.
   *
   * @param l2vpn
   *          VPN Configuration When the VPN Type is L2
   */
  public void setL2vpn(L2L3vpn l2vpn) {
    this.l2vpn = l2vpn;
  }

  @Override
  public String toString() {
    return "Vpn [vpnType=" + vpnType + ", l3vpn=" + l3vpn + ", l2vpn=" + l2vpn + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (l3vpn != null) {
      l3vpn.check(ope);
    }
    if (l2vpn != null) {
      l2vpn.check(ope);
    }
  }
}
