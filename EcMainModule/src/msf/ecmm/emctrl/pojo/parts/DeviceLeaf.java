/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Leaf Device Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device-leaf")
public class DeviceLeaf {

  /** Leaf Device Name. */
  private String name = null;

  /** VRF Configuration Information (only for L3VLAN IF Batch Generation). */
  private Vrf vrf = null;

  /** CP Configuration Information List. */
  @XmlElement(name = "cp")
  private List<Cp> cpList = null;

  /** CP Configuration Information List (only for L2VLAN IF Batch Generation/Change). */
  @XmlElement(name = "cp")
  private List<Cp> updCpList = null;

  /**
   * Generating new instance.
   */
  public DeviceLeaf() {
    super();
  }

  /**
   * Getting Leaf device name.
   *
   * @return Leaf device name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting Leaf device name.
   *
   * @param name
   *          Leaf device name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting VRF configuration information (only for L3VLAN IF batch generation).
   *
   * @return VRF configuration information
   */
  public Vrf getVrf() {
    return vrf;
  }

  /**
   * Setting VRF configuration information (only for L3VLAN IF batch generation).
   *
   * @param vrf
   *          VRF configuration information
   */
  public void setVrf(Vrf vrf) {
    this.vrf = vrf;
  }

  /**
   * Getting CP configuration information list.
   *
   * @return CP configuration information list
   */
  public List<Cp> getCpList() {
    return cpList;
  }

  /**
   * Setting CP configuration information list.
   *
   * @param cpList
   *          CP configuration information list
   */
  public void setCpList(List<Cp> cpList) {
    this.cpList = cpList;
  }

  /**
   * Getting CP configuration information list.
   *
   * @return updCpList
   */
  public List<Cp> getUpdCpList() {
    return updCpList;
  }

  /**
   * Setting CP configuration information list.
   *
   * @param updCpList
   *          set updCpList
   */
  public void setUpdCpList(List<Cp> updCpList) {
    this.updCpList = updCpList;
  }

  /**
   * Stringazing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DeviceLeaf [name=" + name + ", vrf=" + vrf + ", cpList=" + cpList + ", updCpList=" + updCpList + "]";
  }
}
