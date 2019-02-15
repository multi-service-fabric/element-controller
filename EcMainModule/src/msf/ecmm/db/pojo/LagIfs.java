/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * LAG Information POJO Class.
 */
public class LagIfs implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** LagIF ID. */
  private String lag_if_id = null;
  /** FC LagIF ID. */
  private String fc_lag_if_id = null;
  /** IF Name. */
  private String if_name = null;
  /** The Min. Number of Links. */
  private int minimum_link_num = 0;
  /** IF Rate. */
  private String if_speed = null;
  /** IF Status. */
  private int if_status = 0;
  /** Accommodated Device IF Address (IPv4). */
  private String ipv4_address = null;
  /** Accommodated Device IF Prefix (IPv4). */
  private Integer ipv4_prefix = null;
  /** Lag Member List. */
  private Set<LagMembers> lagMembersList;

  /**
   * Generating new instance.
   */
  public LagIfs() {
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
   * Getting LagIF ID.
   *
   * @return LagIF ID
   */
  public String getLag_if_id() {
    return lag_if_id;
  }

  /**
   * Setting LagIF ID.
   *
   * @param lag_if_id
   *          LagIF ID
   */
  public void setLag_if_id(String lag_if_id) {
    this.lag_if_id = lag_if_id;
  }

  /**
   * Getting FC LagIF ID.
   *
   * @return FC LagIF ID
   */
  public String getFc_lag_if_id() {
    return fc_lag_if_id;
  }

  /**
   * Setting LagIF ID.
   *
   * @param fc_lag_if_id
   *          fc_lag_if_id to be set
   */
  public void setFc_lag_if_id(String fc_lag_if_id) {
    this.fc_lag_if_id = fc_lag_if_id;
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
   * Getting the min. number of links.
   *
   * @return the min. number of links
   */
  public int getMinimum_link_num() {
    return minimum_link_num;
  }

  /**
   * Setting the min. number of links.
   *
   * @param minimum_link_num the min. number of link
   *
   * @param minimum_link_num
   *          Min. number of links
   */
  public void setMinimum_link_num(int minimum_link_num) {
    this.minimum_link_num = minimum_link_num;
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
   * Getting IF Status.
   *
   * @return IF Status
   */
  public int getIf_status() {
    return if_status;
  }

  /**
   * Setting IF Status.
   *
   * @param if_status
   *          IF Status
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
   * Getting accommodated device IF prefix (IPv4).
   *
   * @return accommodated device IF prefix (IPv4)
   */
  public Integer getIpv4_prefix() {
    return ipv4_prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv4).
   *
   * @param ipv4_prefix
   *          accommodated device IF prefix (IPv4)
   */
  public void setIpv4_prefix(Integer ipv4_prefix) {
    this.ipv4_prefix = ipv4_prefix;
  }

  /**
   * Getting LAG member list.
   *
   * @return LAG member list
   */
  public Set<LagMembers> getLagMembersList() {
    return lagMembersList;
  }

  /**
   * Setting LAG member list.
   *
   * @param lagMembersList
   *          LAG member list
   */
  public void setLagMembersList(Set<LagMembers> lagMembersList) {
    this.lagMembersList = lagMembersList;
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
    if (lag_if_id != null) {
      hashCode ^= lag_if_id.hashCode();
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

    LagIfs target = (LagIfs) obj;
    if (this.node_id.equals(target.getNode_id()) && this.lag_if_id.equals(target.getLag_if_id())) {
      return true;
    }
    return false;
  }

  /*
   * Stringizing instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "LagIfs [node_id=" + node_id + ", lag_if_id=" + lag_if_id + ", fc_lag_if_id=" + fc_lag_if_id + ", if_name="
        + if_name + ", minimum_link_num=" + minimum_link_num + ", if_speed=" + if_speed + ", if_status=" + if_status
        + ", ipv4_address=" + ipv4_address + ", ipv4_prefix=" + ipv4_prefix + ", lagMembersList=" + lagMembersList
        + "]";
  }

}
