/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

/**
 * B-Leaf Extention/Removal POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "b-leaf")
public class BLeafAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/b-leaf";

  /** Service Name. */
  private String name = null;

  /** Opposing B-Leaf Device Information. */
  @XmlElement(name = "device")
  private Device pairDevice = null;

  /** B-Leaf Device Information. */
  private Device device = null;

  /**
   * Getting attribute data of service basic configuration.
   *
   * @return attribute data of service basic configuration
   */
  public String getXmlns() {
    return xmlns;
  }

  /**
   * Setting attribute data of service basic configuration.
   *
   * @param xmlns
   *          attribute data of service basic configuration
   */
  public void setXmlns(String xmlns) {
    this.xmlns = xmlns;
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
   * Getting opposing B-Leaf device information.
   *
   * @return opposing B-Leaf device information
   */
  public Device getPairDevice() {
    return pairDevice;
  }

  /**
   * Setting opposing B-Leaf device information.
   *
   * @param pairDevice
   *          opposing B-Leaf device information
   */
  public void setPairDevice(Device pairDevice) {
    this.pairDevice = pairDevice;
  }

  /**
   * Getting B-Leaf device information.
   *
   * @return B-Leaf device information
   */
  public Device getDevice() {
    return device;
  }

  /**
   * Setting B-Leaf device information.
   *
   * @param device
   *          B-Leaf device information
   */
  public void setDevice(Device device) {
    this.device = device;
  }

  @Override
  public String toString() {
    return "BLeafAddDelete [xmlns=" + xmlns + ", name=" + name + ", pareDevice=" + pairDevice + ", device=" + device
        + "]";
  }
}
