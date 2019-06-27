/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Setting information when IF is open/closed
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interface")
public class InterfaceStatusUpdate {

  /** Attribute data interface. */
  @XmlAttribute
  private String operation = null;

  /** IF name. */
  private String name = null;

  /** IF type. */
  private String type = null;

  /** port status (open : enable, closed : disable). */
  private XmlStringElement condition = null;

  /**
   * Creating a new instance.
   */
  public InterfaceStatusUpdate() {
    super();
  }

  /**
   * Getting attribute data for interface.
   *
   * @return attribute data for interface
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data for interface.
   *
   * @param operation
   *          attribute data for interface
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF name.
   *
   * @param name
   *          IF name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting IF type.
   *
   * @return IF type
   */
  public String getType() {
    return type;
  }

  /**
   * Setting IF type.
   *
   * @param type
   *          IF type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Getting port status.
   *
   * @return port status
   */
  public XmlStringElement getCondition() {
    return condition;
  }

  /**
   * Setting port status..
   *
   * @param condition
   *          port status
   */
  public void setCondition(XmlStringElement condition) {
    this.condition = condition;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InterfaceStatusUpdate [name=" + name + ", type=" + type + ", condition=" + condition + "]";
  }

}
