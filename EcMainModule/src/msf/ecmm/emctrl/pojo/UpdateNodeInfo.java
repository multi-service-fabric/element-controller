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
 * POJO class for updating the device information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device-info")
public class UpdateNodeInfo extends AbstractMessage {

  /** The attribute data for the basic setting of the service. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/device-info";

  /** The service name. */
  private String name = null;

  /** The Spine/Leaf device information. */
  private Device device = null;

  /**
   * A new instance is generated.
   */
  public UpdateNodeInfo() {
    super();
  }

  /**
   * The service namae is acquired.
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
  public Device getDevice() {
    return device;
  }

  /**
   * The Spine/Leaf device information is set.
   *
   * @param device
   *          The Spine/Leaf device information.
   */
  public void setDevice(Device device) {
    this.device = device;
  }


  /* (non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "UpdateNodeInfo [xmlns=" + xmlns + ", name=" + name + ", device=" + device + "]";
  }

}
