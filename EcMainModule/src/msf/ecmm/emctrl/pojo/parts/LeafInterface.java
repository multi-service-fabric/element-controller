/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Physical IF Information for CE Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "leaf-interface")
public class LeafInterface {

  /** Attribute Data of leaf-interface */
  @XmlAttribute
  private String operation = null;

  /** Physical IF Name of Device */
  private String name = null;

  /**
   * Generating new instance.
   */
  public LeafInterface() {
    super();
  }

  /**
   * Getting attribute data of leaf-interface.
   *
   * @return attribute data of leaf-interface
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of leaf-interface.
   *
   * @param operation
   *          attribute data of leaf-interface
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
    return "LeafInterface [operation=" + operation + ", name=" + name + "]";
  }
}
