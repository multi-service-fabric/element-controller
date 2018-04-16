/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * VRF Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Vrf {

  /** VRF name */
  @XmlElement(name = "vrf-name")
  private String vrfName = null;

  /** Route Target */
  private String rt = null;

  /** Route Distinguisher */
  private String rd = null;

  /** Route ID */
  @XmlElement(name = "router-id")
  private String routerId = null;

  /**
   * Generating new instance.
   */
  public Vrf() {
    super();
  }

  /**
   * Getting VRF name.
   *
   * @return VRF name
   */
  public String getVrfName() {
    return vrfName;
  }

  /**
   * Setting VRF name.
   *
   * @param vrfName
   *          VRF name
   */
  public void setVrfName(String vrfName) {
    this.vrfName = vrfName;
  }

  /**
   * Getting Route Target.
   *
   * @return Route Target
   */
  public String getRt() {
    return rt;
  }

  /**
   * Setting Route Target.
   *
   * @param rt
   *          Route Target
   */
  public void setRt(String rt) {
    this.rt = rt;
  }

  /**
   * Getting Route Distinguisher.
   *
   * @return Route Distinguisher
   */
  public String getRd() {
    return rd;
  }

  /**
   * Setting Route Distinguisher.
   *
   * @param rd
   *          Route Distinguisher
   */
  public void setRd(String rd) {
    this.rd = rd;
  }

  /**
   * Getting Route ID.
   *
   * @return Route ID
   */
  public String getRouterId() {
    return routerId;
  }

  /**
   * Setting Route ID.
   *
   * @param routerId
   *          Route ID
   */
  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Vrf [vrfName=" + vrfName + ", rt=" + rt + ", rd=" + rd + ", routerId=" + routerId + "]";
  }
}
