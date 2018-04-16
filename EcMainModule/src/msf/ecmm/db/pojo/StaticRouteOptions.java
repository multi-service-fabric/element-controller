/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * static Route Option Information POJO Class.
 */
public class StaticRouteOptions implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** VLANIF ID. */
  private String vlan_if_id = null;
  /** Address Type. */
  private int static_route_address_type = 0;
  /** Destination Address. */
  private String static_route_destination_address = null;
  /** Destination Prefix. */
  private int static_route_prefix = 0;
  /** Next Hop Address. */
  private String static_route_nexthop_address = null;

  /**
   * Generating new instance.
   */
  public StaticRouteOptions() {
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

  /**
   * Getting address type.
   *
   * @return address type
   */
  public int getStatic_route_address_type() {
    return static_route_address_type;
  }

  /**
   * Setting address type.
   *
   * @param static_route_address_type
   *          address type
   */
  public void setStatic_route_address_type(int static_route_address_type) {
    this.static_route_address_type = static_route_address_type;
  }

  /**
   * Getting destination address.
   *
   * @return destination address
   */
  public String getStatic_route_destination_address() {
    return static_route_destination_address;
  }

  /**
   * Setting destination address.
   *
   * @param static_route_destination_address
   *          destination address
   */
  public void setStatic_route_destination_address(String static_route_destination_address) {
    this.static_route_destination_address = static_route_destination_address;
  }

  /**
   * Getting destination prefix.
   *
   * @return destination prefix
   */
  public int getStatic_route_prefix() {
    return static_route_prefix;
  }

  /**
   * Setting destination prefix.
   *
   * @param static_route_prefix
   *          destination prefix
   */
  public void setStatic_route_prefix(int static_route_prefix) {
    this.static_route_prefix = static_route_prefix;
  }

  /**
   * Getting next hop address.
   *
   * @return next hop address
   */
  public String getStatic_route_nexthop_address() {
    return static_route_nexthop_address;
  }

  /**
   * Setting next hop address.
   *
   * @param static_route_nexthop_address
   *          next hop address
   */
  public void setStatic_route_nexthop_address(String static_route_nexthop_address) {
    this.static_route_nexthop_address = static_route_nexthop_address;
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
    if (vlan_if_id != null) {
      hashCode ^= vlan_if_id.hashCode();
    }

    if (static_route_destination_address != null) {
      hashCode ^= static_route_destination_address.hashCode();
    }

    if (static_route_address_type != 0) {
      hashCode ^= static_route_address_type;
    }

    if (static_route_prefix != 0) {
      hashCode ^= static_route_prefix;
    }

    if (static_route_nexthop_address != null) {
      hashCode ^= static_route_nexthop_address.hashCode();
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

    StaticRouteOptions target = (StaticRouteOptions) obj;
    if (this.node_id.equals(target.getNode_id()) && this.vlan_if_id.equals(target.getVlan_if_id())
        && (this.static_route_address_type == target.getStatic_route_address_type())
        && this.static_route_destination_address.equals(target.getStatic_route_destination_address())
        && (this.static_route_prefix == target.getStatic_route_prefix())
        && (this.static_route_nexthop_address == target.getStatic_route_nexthop_address())) {
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
    return "StaticRouteOptions [node_id=" + node_id + ", vlan_if_id=" + vlan_if_id + ", static_route_address_type="
        + static_route_address_type + ", static_route_destination_address=" + static_route_destination_address
        + ", static_route_prefix=" + static_route_prefix + ", static_route_nexthop_address="
        + static_route_nexthop_address + "]";
  }

}
