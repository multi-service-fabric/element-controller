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
 * POJO class for increasing and decreasig the speed of the internal-link Lag in the EM data mapping.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "internal-link")
public class InternalLinkLagIfsChange extends AbstractMessage {

  /** The attribute data for the basic setting  of the service. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/internal-link";

  /** The service name. */
  private String name = null;

  /** The Spine/Leaf device information. */
  private List<Device> device = null;

  /**
   * A new instance is generated.
   */
  public InternalLinkLagIfsChange() {
    super();
  }

  /**
   * The service name is acquired.
   *
   * @return The service name.
   */
  public String getName() {
    return name;
  }

  /**
   * The service name is set. 
   *
   * @param name
   *          The service name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Spine/Leaf device information ise acquired.
   *
   * @return The Spine/Leaf device information.
   */
  public List<Device> getDevice() {
    return device;
  }

  /**
   * The Spine/Leaf device information ise set.
   *
   * @param device
   *          The Spine/Leaf device information.
   */
  public void setDevice(List<Device> device) {
    this.device = device;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InternalLinkLagIfsChange [xmlns=" + xmlns + ", name=" + name + ", device=" + device + "]";
  }
}
