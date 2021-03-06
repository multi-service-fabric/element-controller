/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Leaf Extention-L3VPN Accommodation Leaf Basic Configuration Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "l3-vpn")
public class L3Vpn {

  /** BGP Configuration Information */
  @XmlElement(name = "bgp")
  private L2L3VpnBgp bgp = null;

  /** AS Configuration Information */
  @XmlElement(name = "as")
  private L2L3VpnAs as = null;

  /**
   * Generating new instance.
   */
  public L3Vpn() {
    super();
  }

  /**
   * Getting BGP configuration information.
   *
   * @return BGP configuration information
   */
  public L2L3VpnBgp getBgp() {
    return bgp;
  }

  /**
   * Setting BGP configuration information.
   *
   * @param bgp
   *          BGP configuration information
   */
  public void setBgp(L2L3VpnBgp bgp) {
    this.bgp = bgp;
  }

  /**
   * Getting AS configuration information.
   *
   * @return AS configuration information
   */
  public L2L3VpnAs getAs() {
    return as;
  }

  /**
   * Setting AS configuration information.
   *
   * @param as
   *          AS configuration information
   */
  public void setAs(L2L3VpnAs as) {
    this.as = as;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3Vpn [l3VpnBgp=" + bgp + ", l3VpnAs=" + as + "]";
  }

}
