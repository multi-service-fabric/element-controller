/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * Physical IF Information POJO Class.
 */
public class PhysicalIfs implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** IF Name. */
  private String if_name = null;
  /** IF Speed. */
  private String if_speed = null;
  /** IF Status. */
  private int if_status = 0;
  /** Accommodated Device IF Address (IPv4). */
  private String ipv4_address = null;
  /** Accommodated Device IF Prefix (IPv4). */
  private Integer ipv4_prefix = null;
  /** BreakoutIf Information List. */
  private Set<BreakoutIfs> breakoutIfsList;

  /**
   * Generating new instance.
   */
  public PhysicalIfs() {
    super();
  }

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Setting device ID.
   *
   * @param node_id
   *          device ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysical_if_id() {
    return physical_if_id;
  }

  /**
   * Setting physical IF ID.
   *
   * @param physical_if_id
   *          physical IF ID
   */
  public void setPhysical_if_id(String physical_if_id) {
    this.physical_if_id = physical_if_id;
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getIf_name() {
    return if_name;
  }

  /**
   * Setting IF name.
   *
   * @param if_name
   *          IF name
   */
  public void setIf_name(String if_name) {
    this.if_name = if_name;
  }

  /**
   * Getting IF speed.
   *
   * @return IF speed
   */
  public String getIf_speed() {
    return if_speed;
  }

  /**
   * Setting IF speed.
   *
   * @param if_speed
   *          IF speed
   */
  public void setIf_speed(String if_speed) {
    this.if_speed = if_speed;
  }

  /**
   * Getting IF status.
   *
   * @return IF status
   */
  public int getIf_status() {
    return if_status;
  }

  /**
   * Setting IF status.
   *
   * @param if_status
   *          IF status
   */
  public void setIf_status(int if_status) {
    this.if_status = if_status;
  }

  /**
   * Getting accommodated device IF address (IPv4).
   *
   * @return accommodated device IF address (IPv4)
   */
  public String getIpv4_address() {
    return ipv4_address;
  }

  /**
   * Setting accommodated device IF address (IPv4).
   *
   * @param ipv4_address
   *          accommodated device IF address (IPv4)
   */
  public void setIpv4_address(String ipv4_address) {
    this.ipv4_address = ipv4_address;
  }

  /**
   * Getting accommodated device IF Prefix (IPv4).
   *
   * @return accommodated device IF Prefix (IPv4)
   */
  public Integer getIpv4_prefix() {
    return ipv4_prefix;
  }

  /**
   * Setting accommodated device IF Prefix (IPv4).
   *
   * @param ipv4_prefix
   *          accommodated device IF Prefix (IPv4)
   */
  public void setIpv4_prefix(Integer ipv4_prefix) {
    this.ipv4_prefix = ipv4_prefix;
  }

  /**
   * Getting BreakoutIf Information List.
   *
   * @return BreakoutIf Information List
   */
  public Set<BreakoutIfs> getBreakoutIfsList() {
    return breakoutIfsList;
  }

  /**
   * Setting BreakoutIf Information List.
   *
   * @param breakoutIfsList
   *          BreakoutIf Information List
   */
  public void setBreakoutIfsList(Set<BreakoutIfs> breakoutIfsList) {
    this.breakoutIfsList = breakoutIfsList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 0;
    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    if (physical_if_id != null) {
      hashCode ^= physical_if_id.hashCode();
    }
    return hashCode;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.hashCode() != obj.hashCode()) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    PhysicalIfs target = (PhysicalIfs) obj;
    if (this.node_id.equals(target.getNode_id()) && this.physical_if_id.equals(target.getPhysical_if_id())) {
      return true;
    }
    return false;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PhysicalIfs [node_id=" + node_id + ", physical_if_id=" + physical_if_id + ", if_name=" + if_name
        + ", if_speed=" + if_speed + ", if_status=" + if_status + ", ipv4_address=" + ipv4_address + ", ipv4_prefix="
        + ipv4_prefix + ", breakoutIfsList=" + breakoutIfsList + "]";
  }
}
