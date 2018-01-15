/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spine Extention-Spine Device-LAG IF Information for Internal Link Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interna-link")
public class InternalLag {

  /** IF Name of LAG */
  String name = null;

  /** The Number of LAG Members */
  @XmlElement(name = "minimum-links")
  private Long minimumLinks = null;

  /** Line Speed */
  @XmlElement(name = "link-speed")
  private String linkSpeed = null;

  /** IPv4 Address of IF with Net Mask for Internal Link */
  private String address = null;

  /** IPv4 Address of IF with Net Mask for Internal Link */
  private Integer prefix = null;

  /** Physical IF Information List of LAG Member for Internal Link */
  @XmlElement(name = "internal-interface")
  private List<InternalInterface> internalInterface = null;

  /**
   * Generating new instance.
   */
  public InternalLag() {
    super();
  }

  /**
   * Getting IF name of LAG.
   *
   * @return IF name of LAG
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF name of LAG.
   *
   * @param name
   *          IF name of LAG
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting the number of LAG members.
   *
   * @return the number of LAG members
   */
  public Long getMinimumLinks() {
    return minimumLinks;
  }

  /**
   * Setting the number of LAG members.
   *
   * @param minimumLinks
   *          the number of LAG members
   */
  public void setMinimumLinks(Long minimumLinks) {
    this.minimumLinks = minimumLinks;
  }

  /**
   * Getting line speed.
   *
   * @return line speed
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting line speed.
   *
   * @param linkSpeed
   *          line speed
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting IPv4 address of IF with net mask for internal link.
   *
   * @return IPv4 address of IF with net mask for internal link
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IPv4 address of IF with net mask for internal link.
   *
   * @param address
   *          IPv4 address of IF with net mask for internal link
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 address of IF with net mask for internal link.
   *
   * @return IPv4 address of IF with net mask for internal link
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 address of IF with net mask for internal link.
   *
   * @param prefix
   *          IPv4 address of IF with net mask for internal link
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting physical IF information list of LAG member for internal link.
   *
   * @return physical IF information list of LAG member for internal link
   */
  public List<InternalInterface> getInternalInterface() {
    return internalInterface;
  }

  /**
   * Setting physical IF information list of LAG member for internal link.
   *
   * @param internalInterface
   *          physical IF information list of LAG member for internal link
   */
  public void setInternalInterface(List<InternalInterface> internalInterface) {
    this.internalInterface = internalInterface;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InternalLag [name=" + name + ", minimumLinks=" + minimumLinks + ", linkSpeed=" + linkSpeed + ", address="
        + address + ", prefix=" + prefix + ", internalInterface=" + internalInterface + "]";
  }
}
