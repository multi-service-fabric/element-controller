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
 * LAG IF Configuration Informatoin for CE Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ce-lag-interface")
public class CeLagInterface {

  /** Internal link attribute data. */
  @XmlAttribute
  private String operation = null;

  /** LAG IF Configuration Name */
  private String name = null;

  /** LAG ID. */
  @XmlElement(name = "lag-id")
  private Integer lagId = null;

  /** The Number of LAG Members */
  @XmlElement(name = "minimum-links")
  private Long minimumLinks = null;

  /** Line Speed (only for LagIF Generation) */
  @XmlElement(name = "link-speed")
  private String linkSpeed = null;

  /** Physical IF Information List for CE */
  @XmlElement(name = "leaf-interface")
  private List<LeafInterface> leafInterfaceList = null;

  /**
   * Generating new instance.
   */
  public CeLagInterface() {
    super();
  }

  /**
   * Getting operation.
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting operation
   *
   * @param operation
   *          operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting LAG IF configuration name.
   *
   * @return LAG IF configuration name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting LAG IF configuration name.
   *
   * @param name
   *          LAG IF configuration name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting LAG ID.
   *
   * @return Lag ID
   */
  public int getLagId() {
    return lagId;
  }

  /**
   * Setting LAG ID.
   *
   * @param lagId
   *          LagID
   */
  public void setLagId(int lagId) {
    this.lagId = lagId;
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
   * Getting line speed (only for LagIF generation).
   *
   * @return line speed
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting line speed (only for LagIF generation)
   *
   * @param linkSpeed
   *          line speed
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting physical IF information list for CE.
   *
   * @return physical IF information list for CE
   */
  public List<LeafInterface> getLeafInterfaceList() {
    return leafInterfaceList;
  }

  /**
   * Setting physical IF information list for CE.
   *
   * @param leafInterfaceList
   *          physical IF information list for CE
   */
  public void setLeafInterfaceList(List<LeafInterface> leafInterfaceList) {
    this.leafInterfaceList = leafInterfaceList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CeLagInterface [name=" + name + ", minimumLinks=" + minimumLinks + ", linkSpeed=" + linkSpeed
        + ", leafInterfaceList=" + leafInterfaceList + "]";
  }
}
