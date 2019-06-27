/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clag Bridge IF IP address configuration class.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "l3-vni")
public class L3Vni {
  /** VNI ID for L3VN1. */
  @XmlElement(name = "vni-id")
  private Long vniId = null;

  /** Internal VLAN ID for L3VNI. */
  @XmlElement(name = "vlan-id")
  private Long vlanId = null;

  /**
   * Getting VNI ID for L3VNI.
   *
   * @return IP address 
   */
  public Long getVniId() {
    return vniId;
  }

  /**
   * Setting Internal VNI ID for L3VNI.
   *
   * @param vniId
   *          Internal VNI ID for L3VNI
   */
  public void setVniId(Long vniId) {
    this.vniId = vniId;
  }

  /**
   *  Getting Internal VNI ID for L3VNI.
   *
   * @return  Internal VNI ID for L3VNI
   */
  public Long getVlanId() {
    return vlanId;
  }

  /**
   *  Setting Internal VNI ID for L3VNI.
   *
   * @param vlanId
   *           Getting Internal VNI ID for L3VNI
   */
  public void setVlanId(Long vlanId) {
    this.vlanId = vlanId;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3Vni [vniId=" + vniId + ", vlanId=" + vlanId + "]";
  }
}
