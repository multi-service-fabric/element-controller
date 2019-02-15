/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * VRRP Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "vrrp")
public class Vrrp {

  /** VRRP Group ID */
  @XmlElement(name = "group-id")
  private Long groupId = null;

  /** VRRP Virtual IPv4 Address */
  @XmlElement(name = "virtual-address")
  private String virtualAddress = null;

  /** VRRP Virtual IPv6 Address */
  @XmlElement(name = "virtual-address6")
  private String virtualAddress6 = null;

  /** VRRP Priority */
  private Integer priority = null;

  /** Tracking IF Configuration Information */
  private Track track = null;

  /**
   * Generating new instance.
   */
  public Vrrp() {
    super();
  }

  /**
   * Getting VRRP group ID.
   *
   * @return VRRP group ID
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * Setting VRRP group ID.
   *
   * @param groupId
   *          VRRP group ID
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * Getting VRRP virtual IPv4 address.
   *
   * @return VRRP virtual IPv4 address
   */
  public String getVirtualAddress() {
    return virtualAddress;
  }

  /**
   * Setting VRRP virtual IPv4 address.
   *
   * @param virtualAddress
   *          VRRP virtual IPv4 address
   */
  public void setVirtualAddress(String virtualAddress) {
    this.virtualAddress = virtualAddress;
  }

  /**
   * Getting VRRP virtual IPv6 address.
   *
   * @return VRRP virtual IPv6 address
   */
  public String getVirtualAddress6() {
    return virtualAddress6;
  }

  /**
   * Setting VRRP virtual IPv6 address.
   *
   * @param virtualAddress6
   *          VRRP virtual IPv6 address
   */
  public void setVirtualAddress6(String virtualAddress6) {
    this.virtualAddress6 = virtualAddress6;
  }

  /**
   * Getting VRRP priority.
   *
   * @return VRRP priority
   */
  public Integer getPriority() {
    return priority;
  }

  /**
   * Setting VRRP priority.
   *
   * @param priority
   *          VRRP priorit
   */
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * Getting tracking IF configuraiton informaitn.
   *
   * @return Tracking IF configuraiton information
   */
  public Track getTrack() {
    return track;
  }

  /**
   * Setting tracking IF configuration information.
   *
   * @param track
   *          Tracking IF configuraiton information
   */
  public void setTrack(Track track) {
    this.track = track;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Vrrp [groupId=" + groupId + ", virtualAddress=" + virtualAddress + ", virtualAddress6=" + virtualAddress6
        + ", priority=" + priority + ", track=" + track + "]";
  }

}
