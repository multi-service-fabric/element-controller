/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Virtual Link.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "virtual-link")
public class OspfVirtualLink {

  /** Attribute Data of OspfVirtualLink. */
  @XmlAttribute
  private String operation = null;

  /**
   * Generating new instance.
   */
  public OspfVirtualLink() {
    super();
  }

  /** Loopback Address of Opposing B-Leaf. */
  @XmlElement(name = "router-id")
  private String routerId = null;

  /**
   * Getting attribute data of OspfVirtualLink.
   *
   * @return attribute data of OspfVirtualLink.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of OspfVirtualLink.
   *
   * @param operation
   *          attribute data of OspfVirtualLink.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting loopback address of opposing B-Leaf.
   *
   * @return loopback address of opposing B-Leaf.
   */
  public String getRouterId() {
    return routerId;
  }

  /**
   * Setting loopback address of opposing B-Leaf.
   *
   * @param routerId
   *          loopback address of opposing B-Leaf.
   */
  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  @Override
  public String toString() {
    return "VirtuakLink [routerId=" + routerId + "]";
  }
}
