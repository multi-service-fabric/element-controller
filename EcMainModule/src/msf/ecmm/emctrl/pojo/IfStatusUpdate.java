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
 * POJO class for opening or closing theIF.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interface-condition")
public class IfStatusUpdate extends AbstractMessage {

  /** The attribute data for the basic setting in the service. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/interface-condition";

  /** The service name. */
  private String name = null;

  /** The Leafdevice information. */
  private Device device = null;

  /**
   *  A new instance is generated.
   */
  public IfStatusUpdate() {
    super();
  }

  /**
   * The service name is aquired.
   *
   * @return The service name
   */
  public String getName() {
    return name;
  }

  /**
   * The service name is acquired..
   *
   * @param name
   *          The service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Spine/Leaf device information is acquired.
   *
   * @return The Spine/Leaf device information
   */
  public Device getDevice() {
    return device;
  }

  /**
   * The Spine/Leaf device information is set,
   *
   * @param device
   *          The Spine/Leaf device information
   */
  public void setDevice(Device device) {
    this.device = device;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "IfStatusUpdate [name=" + name + ", device=" + device + "]";
  }

}
