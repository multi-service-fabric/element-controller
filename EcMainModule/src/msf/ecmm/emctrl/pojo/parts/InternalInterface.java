/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spine Extention-Spine Device-LAG IF Information for Internal Link Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "internal-interface")
public class InternalInterface {

  /** Attribute Data of Internal Link. */
  @XmlAttribute
  private String operation = null;

  /** IF Name of Internal Link. */
  String name = null;

  /** IF Type of Internal Link. */
  String type = null;

  /** Opposing Device Name. */
  @XmlElement(name = "opposite-node-name")
  String oppositeNodeName = null;

  /** The Number of LAG Members. */
  @XmlElement(name = "minimum-links")
  private Long minimumLinks = null;

  /** Line Speed. */
  @XmlElement(name = "link-speed")
  private String linkSpeed = null;

  /** IPv4 Address of IF with Net Mask for Internal Link. */
  private String address = null;

  /** IPv4 Address of IF with Net Mask for Internal Link. */
  private Integer prefix = null;

  /** Physical IF Information List of LAG Member for Internal Link. */
  @XmlElement(name = "internal-interface")
  private List<InternalInterfaceMember> internalInterfaceMember = null;

  /**
   * Generating new instance.
   */
  public InternalInterface() {
    super();
  }

  /**
   * Getting attribute data of internal link.
   *
   * @return attribute data of internal link.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of internal link.
   *
   * @param operation
   *          attribute data of internal link.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting IF name of internal link.
   *
   * @return IF name of internal link.
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF name of internal link.
   *
   * @param name
   *          IF name of internal link.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting IF type of internal link.
   *
   * @return IF type of internal link.
   */
  public String getType() {
    return type;
  }

  /**
   * Setting IF type of internal link.
   *
   * @param type
   *          IF type of internal link.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Getting opposing device name.
   *
   * @return opposing device name.
   */
  public String getOppositeNodeName() {
    return oppositeNodeName;
  }

  /**
   * Setting opposing device name.
   *
   * @param oppositeNodeName
   *          opposing device name.
   */
  public void setOppositeNodeName(String oppositeNodeName) {
    this.oppositeNodeName = oppositeNodeName;
  }

  /**
   * Getting the number of LAG members.
   *
   * @return the number of LAG members.
   */
  public Long getMinimumLinks() {
    return minimumLinks;
  }

  /**
   * Setting the number of LAG members.
   *
   * @param minimumLinks
   *          the number of LAG members.
   */
  public void setMinimumLinks(Long minimumLinks) {
    this.minimumLinks = minimumLinks;
  }

  /**
   * Getting line speed.
   *
   * @return line speed.
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting line speed.
   *
   * @param linkSpeed
   *          line speed.
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting IPv4 address of IF with net mask for internal link.
   *
   * @return IPv4 address of IF with net mask for internal link.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setiing IPv4 address of IF with net mask for internal link.
   *
   * @param address
   *          IPv4 address of IF with net mask for internal link.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 address of IF with net mask for internal link.
   *
   * @return IPv4 address of IF with net mask for internal link.
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 address of IF with net mask for internal link.
   *
   * @param prefix
   *          IPv4 address of IF with net mask for internal link.
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting physical IF information list of LAG member for internal link.
   *
   * @return physical IF information list of LAG member for internal link.
   */
  public List<InternalInterfaceMember> getInternalInterfaceMember() {
    return internalInterfaceMember;
  }

  /**
   * Setting physical IF information list of LAG member for internal link.
   *
   * @param internalInterface
   *          physical IF information list of LAG member for internal link.
   */
  public void setInternalInterfaceMember(List<InternalInterfaceMember> internalInterface) {
    this.internalInterfaceMember = internalInterface;
  }

  @Override
  public String toString() {
    return "InternalInterface [operation=" + operation + ", name=" + name + ", type=" + type + ", oppositeNodeName="
        + oppositeNodeName + ", minimumLinks=" + minimumLinks + ", linkSpeed=" + linkSpeed + ", address=" + address
        + ", prefix=" + prefix + ", internalInterfaceMember=" + internalInterfaceMember + "]";
  }
}
