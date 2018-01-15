/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

/**
 * LAG Add/Delete for CE POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ce-lag")
public class CeLagAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/ce-lag";

  /** Service Name. */
  private String name = null;

  /** Leaf Device Information. */
  private List<Device> device = null;

  /**
   * Generating new instance.
   */
  public CeLagAddDelete() {
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
  public List<Device> getDevice() {
    return device;
  }

  /**
   * Setting Leaf device information.
   *
   * @param device
   *          Leaf device information
   */
  public void setDevice(List<Device> device) {
    this.device = device;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CeLagAddDelete [name=" + name + ", device=" + device + "]";
  }
}
