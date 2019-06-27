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
@XmlRootElement(name = "l3-slice")
public class L3SliceAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/l3-slice";

  /** Service Name */
  private String name = null;

  /** Forceful Deletion Flag (only for L3VLAN IF Batch Delete) */
  private String force = null;

  /** Leaf Device Information List */
  @XmlElement(name = "device-leaf")
  List<DeviceLeaf> deviceLeafList = null;

  /**
   * Generating new instance.
   */
  public L3SliceAddDelete() {
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
   * Adding forceful deletin flag (only for L3VLAN IF batch delete).
   */
  public void addForce() {
    this.force = new String("");
  }

  /**
   * Deleting forceful deletion flag (only for L3VLAN IF batch delete).
   */
  public void delForce() {
    this.force = null;
  }

  /**
   * Getting Leaf device information.
   *
   * @return Leaf device information
   */
  public List<DeviceLeaf> getDeviceLeafList() {
    return deviceLeafList;
  }

  /**
   * Setting Leaf device information.
   *
   * @param deviceLeafList
   *          Leaf device information
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
    return "L3SliceAddDelete [name=" + name + ", force=" + force + ", deviceLeafList=" + deviceLeafList + "]";
  }

}
