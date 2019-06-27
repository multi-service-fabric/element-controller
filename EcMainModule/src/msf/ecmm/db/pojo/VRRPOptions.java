/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * VRRP Option Information POJO Class.
 */
public class VRRPOptions implements Serializable {

  /** VRRP Option Information. */
  private String vrrp_id = null;
  /** Group ID. */
  private int vrrp_group_id = 0;
  /** VRRP Role. */
  private int vrrp_role = 0;
  /** Virtual IPv4 Address. */
  private String vrrp_virtual_ipv4_address = null;
  /** Virtual IPv6 Address. */
  private String vrrp_virtual_ipv6_address = null;
  /** Device ID. */
  private String node_id = null;
  /** VLAN IF ID. */
  private String vlan_if_id = null;

  /**
   * Generating new instance.
   */
  public VRRPOptions() {
    super();

  }

  /**
   * Getting VRRP option information.
   *
   * @return VRRP option information
   */
  public String getVrrp_id() {
    return vrrp_id;
  }

  /**
   * Setting VRRP option information.
   *
   * @param vrrp_id
   *          VRRP option information
   */
  public void setVrrp_id(String vrrp_id) {
    this.vrrp_id = vrrp_id;
  }

  /**
   * Getting group ID.
   *
   * @return group ID
   */
  public int getVrrp_group_id() {
    return vrrp_group_id;
  }

  /**
   * Setting group ID.
   *
   * @param vrrp_group_id
   *          group ID
   */
  public void setVrrp_group_id(int vrrp_group_id) {
    this.vrrp_group_id = vrrp_group_id;
  }

  /**
   * Getting role.
   *
   * @return role
   */
  public int getVrrp_role() {
    return vrrp_role;
  }

  /**
   * Setting role.
   *
   * @param vrrp_role
   *          role
   */
  public void setVrrp_role(int vrrp_role) {
    this.vrrp_role = vrrp_role;
  }

  /**
   * Getting virtual IPv4 address.
   *
   * @return virtual IPv4 address
   */
  public String getVrrp_virtual_ipv4_address() {
    return vrrp_virtual_ipv4_address;
  }

  /**
   * Setting virtual IPv4 address.
   *
   * @param vrrp_virtual_ipv4_address
   *          virtual IPv4 address
   */
  public void setVrrp_virtual_ipv4_address(String vrrp_virtual_ipv4_address) {
    this.vrrp_virtual_ipv4_address = vrrp_virtual_ipv4_address;
  }

  /**
   * Getting virtual IPv6 address.
   *
   * @return virtual IPv6 address
   */
  public String getVrrp_virtual_ipv6_address() {
    return vrrp_virtual_ipv6_address;
  }

  /**
   * Setting virtual IPv6 address.
   *
   * @param vrrp_virtual_ipv6_address
   *          virtual IPv6 address
   */
  public void setVrrp_virtual_ipv6_address(String vrrp_virtual_ipv6_address) {
    this.vrrp_virtual_ipv6_address = vrrp_virtual_ipv6_address;
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
   * Getting VLAN IF ID.
   *
   * @return VLAN IF ID
   */
  public String getVlan_if_id() {
    return vlan_if_id;
  }

  /**
   * Setting VLAN IF ID.
   *
   * @param vlan_if_id
   *          VLAN IF ID
   */
  public void setVlan_if_id(String vlan_if_id) {
    this.vlan_if_id = vlan_if_id;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (vrrp_id != null) {
      hashCode ^= vrrp_id.hashCode();
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

    VRRPOptions target = (VRRPOptions) obj;
    if (this.vrrp_id.equals(target.getVrrp_id())) {

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
    return "VRRPOptions [vrrp_id=" + vrrp_id + ", vrrp_group_id=" + vrrp_group_id + ", vrrp_role=" + vrrp_role
        + ", vrrp_virtual_ipv4_address=" + vrrp_virtual_ipv4_address + ", vrrp_virtual_ipv6_address="
        + vrrp_virtual_ipv6_address + ", node_id=" + node_id + ", vlan_if_id=" + vlan_if_id + "]";
  }

}
