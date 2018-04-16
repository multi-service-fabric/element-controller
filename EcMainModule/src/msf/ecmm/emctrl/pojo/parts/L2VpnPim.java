/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spin Extention-Multicast Configuration for VXLAN-PIM Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pim")
public class L2VpnPim {

  /** RP Address (in case the other router is RP): only for Spine device extention */
  @XmlElement(name = "other-rp-address")
  private String otherRpAddress = null;

  /** RP Address (in case itself is RP): only for Spine device extention */
  @XmlElement(name = "self-rp-address")
  private String selfRpAddress = null;

  /** RP Address: only for Leaf device extention */
  @XmlElement(name = "rp-address")
  private String rpAddress = null;

  /**
   * Generating new instance.
   */
  public L2VpnPim() {
    super();
  }

  /**
   * Getting RP address (in case the other router is RP) (only for Spine device extention).
   *
   * @return RP address (in case the other router is RP)
   */
  public String getOtherRpAddress() {
    return otherRpAddress;
  }

  /**
   * Setting RP address (in case the other router is RP) (only for Spine device extention).
   *
   * @param otherRpAddress
   *          RP address (in case the other router is RP)
   */
  public void setOtherRpAddress(String otherRpAddress) {
    this.otherRpAddress = otherRpAddress;
  }

  /**
   * Getting RP address (in case itself is RP) (only for Spine device extention).
   *
   * @return RP address (in case itself is RP)
   */
  public String getSelfRpAddress() {
    return selfRpAddress;
  }

  /**
   * Setting RP address (in case itself is RP) (only for Spine device extention).
   *
   * @param selfRpAddress
   *          RP address (in case itself is RP)
   */
  public void setSelfRpAddress(String selfRpAddress) {
    this.selfRpAddress = selfRpAddress;
  }

  /**
   * Getting RP Address (only for Leaf device extention).
   *
   * @return RP Address
   */
  public String getRpAddress() {
    return rpAddress;
  }

  /**
   * Setting RP Address (only for Leaf device extention)
   *
   * @param rpAddress
   *          RP Address
   */
  public void setRpAddress(String rpAddress) {
    this.rpAddress = rpAddress;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L2VpnPim [otherRpAddress=" + otherRpAddress + ", selfRpAddress=" + selfRpAddress + ", rpAddress="
        + rpAddress + "]";
  }
}
