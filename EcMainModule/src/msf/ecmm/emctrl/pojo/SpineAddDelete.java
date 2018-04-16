/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

/**
 * Spin Extention/Removal POJO Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "spine")
public class SpineAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/spine";

  /** Service Name */
  private String name = null;

  /** Spine Device Information */
  private Device device = null;

  /**
   * Generating new instance.
   */
  public SpineAddDelete() {
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
   * Getting Spine device information.
   *
   * @return Spine device information
   */
  public Device getDevice() {
    return device;
  }

  /**
   * Setting Spine device information.
   *
   * @param device
   *          Spine device information
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
    return "SpineAddDelete [name=" + name + ", device=" + device + "]";
  }
}
