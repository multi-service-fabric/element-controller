/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VPN Configuration When the VPN Type is L3.
 */
public class L3vpn {

  /** BGP Configuration. */
  private BgpAddNode bgp;

  /** AS Configuration. */
  private As as;

  /**
   * Getting BGP configuration.
   *
   * @return BGP configuration
   */
  public BgpAddNode getBgp() {
    return bgp;
  }

  /**
   * Setting BGP configuration.
   *
   * @param bgp
   *          BGP configuration
   */
  public void setBgp(BgpAddNode bgp) {
    this.bgp = bgp;
  }

  /**
   * Getting AS configuration.
   *
   * @return AS configuration
   */
  public As getAs() {
    return as;
  }

  /**
   * Setting AS configuration.
   *
   * @param as
   *          AS configuration
   */
  public void setAs(As as) {
    this.as = as;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "L3vpn [bgp=" + bgp + ", as=" + as + "]";
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
    if (bgp != null) {
      bgp.check(ope);
    }
    if (as == null) {
      throw new CheckDataException();
    } else {
      as.check(ope);
    }
  }
}
