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
 * Internal Link IF Add/Delete POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "internal-link")
public class InternalLinkAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/internal-link";

  /** Service Name. */
  private String name = null;

  /** Spine/Leaf Device Information. */
  private List<Device> device = null;

  /**
   * Generating new instance.
   */
  public InternalLinkAddDelete() {
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
   * Getting Spine/Leaf device information.
   *
   * @return Spine/Leaf device information
   */
  public List<Device> getDevice() {
    return device;
  }

  /**
   * Setting Spine/Leaf device information.
   *
   * @param device
   *          Spine/Leaf device information
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
    return "InternalLinkAddDelete [xmlns=" + xmlns + ", name=" + name + ", device=" + device + "]";
  }
}
