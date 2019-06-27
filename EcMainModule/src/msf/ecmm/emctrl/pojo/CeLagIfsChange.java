/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

/**
 * POJO Class for modifying LAGIF for CE.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ce-lag")
public class CeLagIfsChange extends AbstractMessage {

  /** The attribute data for the basic setting in the service. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/ce-lag";

  /** The service name. */
  private String name = null;

  /** The Leafdevice information. */
  private List<Device> device = null;

  /**
   * A new instace is generated.
   */
  public CeLagIfsChange() {
    super();
  }

  /**
   * The service name is acquired.
   *
   * @return The service name
   */
  public String getName() {
    return name;
  }

  /**
   * The service name is aquired..
   *
   * @param name
   *          The service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Leaf device infoarmtion is acquired.
   *
   * @return The Leaf device inforamtion
   */
  public List<Device> getDevice() {
    return device;
  }

  /**
   * The Leaf device information is set.
   *
   * @param device
   *          The Leaf device information
   */
  public void setDevice(List<Device> device) {
    this.device = device;
  }

  /**
   * The instance to changed to the string.
   *
   * @return The string to which the instance is changed. 
   */
  @Override
  public String toString() {
    return "CeLagIfsChange [name=" + name + ", device=" + device + "]";
  }
}
