/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * BGP option table POJO class.
 */
public class BGPOptions implements Serializable {

  /** BGP option information ID. */
  private String bgp_id = null;
  /** BGP role. */
  private int bgp_role = 0;
  /** BGP neighbor AS number. */
  private int bgp_neighbor_as = 0;
  /** BGP neighbor IPv4 address. */
  private String bgp_neighbor_ipv4_address = null;
  /** BGP neighbor IPv6 address. */
  private String bgp_neighbor_ipv6_address = null;
  /** Device ID. */
  private String node_id = null;
  /** VLANIF ID. */
  private String vlan_if_id = null;

  /**
   * Generate new instance.
   */
  public BGPOptions() {
    super();
  }

  /**
   * Getting BGP option information ID.
   *
   * @return BGP option information ID
   */
  public String getBgp_id() {
    return bgp_id;
  }

  /**
   * Setting BGP option information ID.
   *
   * @param bgp_id
   *          BGP option information ID
   */
  public void setBgp_id(String bgp_id) {
    this.bgp_id = bgp_id;
  }

  /**
   * Getting BGP role.
   *
   * @return BGP role
   */
  public int getBgp_role() {
    return bgp_role;
  }

  /**
   * Setting BGP role.
   *
   * @param bgp_role
   *          BGP role
   */
  public void setBgp_role(int bgp_role) {
    this.bgp_role = bgp_role;
  }

  /**
   * Getting BGP neighbor AS number.
   *
   * @return BGP neighbor AS number
   */
  public int getBgp_neighbor_as() {
    return bgp_neighbor_as;
  }

  /**
   * Setting BGP neighbor AS number.
   *
   * @param bgp_neighbor_as
   *          BGP neighbor AS number
   */
  public void setBgp_neighbor_as(int bgp_neighbor_as) {
    this.bgp_neighbor_as = bgp_neighbor_as;
  }

  /**
   * Getting BGP neighbor IPv4 address.
   *
   * @return BGP neighbor IPv4 address
   */
  public String getBgp_neighbor_ipv4_address() {
    return bgp_neighbor_ipv4_address;
  }

  /**
   * Setting BGP neighbor IPv4 address.
   *
   * @param bgp_neighbor_ipv4_address
   *          BGP neighbor IPv4 address
   */
  public void setBgp_neighbor_ipv4_address(String bgp_neighbor_ipv4_address) {
    this.bgp_neighbor_ipv4_address = bgp_neighbor_ipv4_address;
  }

  /**
   * Getting BGP neighbor IPv6 address.
   *
   * @return BGP neighbor IPv6 address
   */
  public String getBgp_neighbor_ipv6_address() {
    return bgp_neighbor_ipv6_address;
  }

  /**
   * Setting BGP neighbor IPv6 address.
   *
   * @param bgp_neighbor_ipv6_address
   *          BGP neighbor IPv6 address
   */
  public void setBgp_neighbor_ipv6_address(String bgp_neighbor_ipv6_address) {
    this.bgp_neighbor_ipv6_address = bgp_neighbor_ipv6_address;
  }

  /**
   * Getting Model ID.
   *
   * @return Model ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Setting Model ID.
   *
   * @param node_id
   *          Model ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting VLANIF ID.
   *
   * @return VLANIF ID
   */
  public String getVlan_if_id() {
    return vlan_if_id;
  }

  /**
   * Setting VLANIF ID.
   *
   * @param vlan_if_id
   *          VLANIF ID
   */
  public void setVlan_if_id(String vlan_if_id) {
    this.vlan_if_id = vlan_if_id;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 0;
    if (bgp_id != null) {
      hashCode ^= bgp_id.hashCode();
    }

    return hashCode;
  }

  /*
   * (Non Javadoc)
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

    BGPOptions target = (BGPOptions) obj;
    if (this.bgp_id.equals(target.getBgp_id())) {
      return true;
    }
    return false;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BGPOptions [bgp_id=" + bgp_id + ", bgp_role=" + bgp_role + ", bgp_neighbor_as=" + bgp_neighbor_as
        + ", bgp_neighbor_ipv4_address=" + bgp_neighbor_ipv4_address + ", bgp_neighbor_ipv6_address="
        + bgp_neighbor_ipv6_address + ", node_id=" + node_id + ", vlan_if_id=" + vlan_if_id + "]";
  }
}
