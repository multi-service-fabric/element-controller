/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * L3 Slice Static Connection Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

  /** Attribute Data of cp */
  @XmlAttribute
  private String operation = null;

  /** IP Address */
  private String address = null;

  /** Net Mask */
  private Integer prefix = null;

  /** Destination IP Address */
  @XmlElement(name = "next-hop")
  private String nextHop = null;

  /**
   * Generating new instance.
   */
  public Route() {
    super();
  }

  /**
   * Getting attribute data of cp.
   *
   * @return attribute data of cp
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of cp.
   *
   * @param operation
   *          attribute data of cp
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting IP address.
   *
   * @return IP address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IP address.
   *
   * @param address
   *          IP address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting net mask.
   *
   * @return net mask
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting net mask.
   *
   * @param prefix
   *          net mask
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting destination IP address.
   *
   * @return destination IP address
   */
  public String getNextHop() {
    return nextHop;
  }

  /**
   * Setting destination IP address.
   *
   * @param nextHop
   *          destination IP address
   */
  public void setNextHop(String nextHop) {
    this.nextHop = nextHop;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Route [operation=" + operation + ", address=" + address + ", prefix=" + prefix + ", nextHop=" + nextHop
        + "]";
  }
}
