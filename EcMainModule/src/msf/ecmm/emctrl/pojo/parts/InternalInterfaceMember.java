/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spine Extention-Spine Device-LAG Member Information for Internal Link Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "internal-interface")
public class InternalInterfaceMember {

  /** Attribute Data of internal-interface. */
  @XmlAttribute
  private String operation = null;

  /** Physical IF Name of Device. */
  private String name = null;

  /**
   * Generating new instance.
   */
  public InternalInterfaceMember() {
    super();
  }

  /**
   * Getting attribute data of cp.
   *
   * @return attribute data of device
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of cp.
   *
   * @param operation
   *          attribute data of device
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting physical IF name of device.
   *
   * @return physical IF name of device
   */
  public String getName() {
    return name;
  }

  /**
   * Setting physical IF name of device.
   *
   * @param name
   *          physical IF name of device
   */
  public void setName(String name) {
    this.name = name;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InternalInterface [operation=" + operation + ", name=" + name + "]";
  }
}
