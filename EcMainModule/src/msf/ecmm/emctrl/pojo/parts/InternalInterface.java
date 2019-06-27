/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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

  XmlIntegerElement cost = null;

  /** Opposing Device Name. */
  @XmlElement(name = "opposite-node-name")
  String oppositeNodeName = null;

  /** LagIfId. */
  @XmlElement(name = "lag-id")
  Integer ifId = null;

  /** VLANID for internal link. */
  @XmlElement(name = "vlan-id")
  Integer vlanId = null;

  /** No. of members of LAG. */
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
   * Getting Lag IF ID.
   *
   * @return LagIfId
   */
  public Integer getIfId() {
    return ifId;
  }

  /**
   * Settin Lag IF ID.
   *
   * @param ifId
   *          LagIfId
   */
  public void setIfId(Integer ifId) {
    this.ifId = ifId;
  }

  /**
   * Acquiring VLANID for internal link.
   *
   * @return vlanId
   */
  public Integer getVlanId() {
    return vlanId;
  }

  /**
   * Setting VLANID for internal link.
   *
   * @param vlanId  set vlanId
   */
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Acquiring IF type of internal link.
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
   * Acqurining cost value of internal link IF.
   *
   * @return Cost value of internal link IF.
   */
  public XmlIntegerElement getCost() {
    return cost;
  }

  /**
   * Setting cost value of internal link IF.
   *
   * @param cost
   *          Cost value of internal link IF.
   */
  public void setCost(XmlIntegerElement cost) {
    this.cost = cost;
  }

  /**
   * Acquiring name of opposint device.
   *
   * @return Name of opposing device.
   */
  public String getOppositeNodeName() {
    return oppositeNodeName;
  }

  /**
   * Setting name of opposing device.
   *
   * @param oppositeNodeName
   *          Name of opposing device.
   */
  public void setOppositeNodeName(String oppositeNodeName) {
    this.oppositeNodeName = oppositeNodeName;
  }

  /**
   * Acquiring no. of member of LAG.
   *
   * @return No. of member of LAG.
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
    return "InternalInterface [operation=" + operation + ", name=" + name + ", type=" + type + ", cost=" + cost
        + ", oppositeNodeName=" + oppositeNodeName + ", ifId=" + ifId + ", vlanId=" + vlanId + ", minimumLinks="
        + minimumLinks + ", linkSpeed=" + linkSpeed + ", address=" + address + ", prefix=" + prefix
        + ", internalInterfaceMember=" + internalInterfaceMember + "]";
  }
}
