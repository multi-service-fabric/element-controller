/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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

  /** VRF configuration information. */
  private Vrf vrf = null;

  /** CP Configuration Information List. */
  @XmlElement(name = "cp")
  private List<Cp> cpList = null;

  /** CP Configuration Information List (only for L2VLAN IF Batch Generation/Change). */
  @XmlElement(name = "cp")
  private List<Cp> updCpList = null;

  /** Dummy VLAN configuration information list. */
  @XmlElement(name = "dummy_vlan")
  private List<DummyVlan> dummyVlanList = null;

  /** multi-homing. */
  @XmlElement(name = "multi-homing")
  private MultiHoming multiHoming = null;

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
   * @return updCpList
   */
  public MultiHoming getMultiHoming() {
    return multiHoming;
  }

  /**
   * Setting multihoming configuration.
   *
   * @param multiHoming
   *          set multiHoming
   */
  public void setMultiHoming(MultiHoming multiHoming) {
    this.multiHoming = multiHoming;
  }

  /**
   * Acquiring dummy VLAN configuration information list.
   *
   * @return updCpList
   */
  public List<DummyVlan> getDummyVlanList() {
    return dummyVlanList;
  }

  /**
   * Acquring dummy VLAN configuration information list.
   *
   * @param dummyVlanList
   *          set dummyVlanList
   */
  public void setDummyVlanList(List<DummyVlan> dummyVlanList) {
    this.dummyVlanList = dummyVlanList;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DeviceLeaf [name=" + name + ", vrf=" + vrf + ", cpList=" + cpList + ", updCpList=" + updCpList
        + ", dummyVlanList=" + dummyVlanList + ", multiHoming=" + multiHoming + "]";
  }
}
