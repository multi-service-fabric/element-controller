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
 * Service reconfiguration class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recover-service")
public class RecoverUpdateService extends AbstractMessage {

/** Attribute Data of Service Basic Setting. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/recover-service";

  /** service name. */
  private String name = null;

    /** Leaf Device Infomation. */
  private Device device = null;

  /**
   * Generating new instance.
   */
  public RecoverUpdateService() {
    super();
  }

  /**
   * Getting service name.
   *
   * @return service name.
   */
  public String getName() {
    return name;
  }

  /**
   * Setting service name.
   *
   * @param name
   *          service name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting Leaf device information.
   *
   * @return Leaf device information.
   */
  public Device getDevice() {
    return device;
  }

   /**
   * Setting Leaf device information.
   *
   * @param device
   *       Leaf device information.
   */
  public void setDevice(Device device) {
    this.device = device;
  }

  /**
   * Stringizing Instance.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "LeafAddDelete [name=" + name + ", device=" + device + "]";
  }
}
