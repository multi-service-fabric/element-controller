/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.DeviceLeaf;

/**
 * L3VLAN IF Batch Generation/Deletion POJO Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "l2-slice")
public class L2SliceAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/l2-slice";

  /** Service Name */
  private String name = null;

  /** Forceful Deletion Flag (only for L2VLAN IF Batch Delete/Change) */
  private String force = null;

  /** Leaf Device Information List */
  @XmlElement(name = "device-leaf")
  private List<DeviceLeaf> deviceLeafList = null;

  /**
   * Generating new instance.
   */
  public L2SliceAddDelete() {
    super();
  }

  /**
   * Getting service name.
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting service name.
   *
   * @param name
   *          service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adding forceful deletion flad (only for L2VLAN IF batch delete/change).
   */
  public void addForce() {
    this.force = new String("");
  }

  /**
   * Deleting forceful deletion flad (only for L2VLAN IF batch delete/change).
   */
  public void delForce() {
    this.force = null;
  }

  /**
   * Getting Leaf device information list.
   *
   * @return Leaf device information list
   */
  public List<DeviceLeaf> getDeviceLeafList() {
    return deviceLeafList;
  }

  /**
   * Setting Leaf device information list.
   *
   * @param deviceLeafList
   *          Leaf device information list
   */
  public void setDeviceLeafList(List<DeviceLeaf> deviceLeafList) {
    this.deviceLeafList = deviceLeafList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L2SliceAddDelete [name=" + name + ", force=" + force + ", deviceLeafList=" + deviceLeafList + "]";
  }
}
