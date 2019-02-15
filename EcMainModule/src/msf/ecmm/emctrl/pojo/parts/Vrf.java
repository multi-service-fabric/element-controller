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

  /** VRF ID (used for L2VLAN generation). */
  @XmlElement(name = "vrf-id")
  private String vrfId = null;

  /** Route Target. */
  private String rt = null;

  /** Route Distinguisher. */
  private String rd = null;

  /** Route ID. */
  @XmlElement(name = "router-id")
  private String routerId = null;

  /** LoopBack.Using for L2VLAN generation */
  @XmlElement(name = "loopback")
  private Loopback loopback = null;

  /** l3-vni. Using for L2VLAN generation */
  @XmlElement(name = "l3-vni")
  private L3Vni l3Vni = null;

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

  /**
   * Getting VRF-ID.
   *
   * @return VRF-ID
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting VRF-ID.
   *
   * @param vrfId
   *          VRF-ID
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting Loopback.
   *
   * @return Loopback
   */
  public Loopback getLoopback() {
    return loopback;
  }

  /**
   * Setting LoopBack.
   *
   * @param loopback
   *          Loopback
   */
  public void setLoopback(Loopback loopback) {
    this.loopback = loopback;
  }

  /**
   * Getting L3-VNI.
   *
   * @return L3Vni
   */
  public L3Vni getL3Vni() {
    return l3Vni;
  }

  /**
   * Setting L3-VNI.
   *
   * @param l3Vni
   *          L3Vni
   */
  public void setL3Vni(L3Vni l3Vni) {
    this.l3Vni = l3Vni;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Vrf [vrfName=" + vrfName + ", vrfId=" + vrfId + ", rt=" + rt + ", rd=" + rd + ", routerId=" + routerId
        + ", loopback=" + loopback + ", l3Vni=" + l3Vni + "]";
  }
}
