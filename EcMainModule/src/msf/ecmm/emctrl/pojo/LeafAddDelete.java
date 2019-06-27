/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

/**
 * Leaf Extention/Removal POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "leaf")
public class LeafAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/leaf";

  /** Service Name. */
  private String name = null;

  /** Leaf Device Information. */
  private Device device = null;

  /**
   * Generating new instance.
   */
  public LeafAddDelete() {
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
   * Getting Leaf device information.
   *
   * @return Leaf device information
   */
  public Device getDevice() {
    return device;
  }

  /**
   * Setting Leaf device information.
   *
   * @param device
   *          Leaf device information
   */
  public void setDevice(Device device) {
    this.device = device;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "LeafAddDelete [name=" + name + ", device=" + device + "]";
  }
}
