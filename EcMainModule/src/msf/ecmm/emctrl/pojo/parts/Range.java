/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OSPF Route Aggregation Configuration.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "range")
public class Range {

  /** Attribute Data of Range. */
  @XmlAttribute
  private String operation = null;

  /** OSPF Route Aggregation Configuration Address. */
  private String address = null;

  /** OSPF Route Aggregation Address Prefix. */
  private Integer prefix = null;

  /**
   * Generating new instance.
   */
  public Range() {
    super();
  }

  /**
   * Getting attribute data of Range.
   *
   * @return attribute data of Range.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of Range.
   *
   * @param operation
   *          attribute data of Range.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting OSPF route aggregation configuration address.
   *
   * @return OSPF route aggregation configuration address.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting OSPF route aggregation configuration address.
   *
   * @param address
   *          OSPF route aggregation configuration address.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting OSPF route aggregation configuration address prefix.
   *
   * @return OSPF route aggregation configuration address prefix.
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting OSPF route aggregation configuration address prefix.
   *
   * @param prefix
   *          OSPF route aggregation configuration address prefix.
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  @Override
  public String toString() {
    return "Range [operation=" + operation + ", address=" + address + ", prefix=" + prefix + "]";
  }
}
