/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * VANIF Information POJO Class.
 */
public class VlanIfs implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** VLAN IF ID. */
  private String vlan_if_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** LagIF ID. */
  private String lag_if_id = null;
  /** breakoutIF ID. */
  private String breakout_if_id = null;
  /** VLAN ID. */
  private String vlan_id = null;
  /** IF Name. */
  private String if_name = null;
  /** IF Status. */
  private int if_status = 0;
  /** Accommodated Device IF Address (IPv4). */
  private String ipv4_address = null;
  /** Accommodated Device IF Prefix (IPv4). */
  private Integer ipv4_prefix = null;
  /** Addommodate Device IF Address (IPv6). */
  private String ipv6_address = null;
  /** Accommodated Device IF Prefix (IPv6). */
  private Integer ipv6_prefix = null;
  /** MTU Value. */
  private Integer mtu = null;
  /** Port Mode. */
  private Integer port_mode = null;
  /** BGPID. */
  private String bgp_id = null;
  /** VRRPID. */
  private String vrrp_id = null;
  /** In flow ShapingRate. */
  private Float inflow_shaping_rate;
  /** Out flow ShapingRate. */
  private Float outflow_shaping_rate;
  /** Remark menu. */
  private String remark_menu;
  /** Egress queue menu. */
  private String egress_queue_menu;
  /** IRB instance ID. */
  private String irb_instance_id;
  /** Q-in-Q information. */
  private Boolean q_in_q;
  /** BGP Option List. */
  private Set<BGPOptions> bgpOptionsList;
  /** Static Route Information List. */
  private Set<StaticRouteOptions> staticRouteOptionsList;
  /** VRRP Option List. */
  private Set<VRRPOptions> vrrpOptionsList;

  /**
   * Generating new instance.
   */
  public VlanIfs() {
    super();
  }

  /**
   * Getting model ID.
   *
   * @return model ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Setting model ID.
   *
   * @param node_id
   *          model ID
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
   * Getting breakoutIF ID.
   *
   * @return breakoutIF ID
   */
  public String getBreakout_if_id() {
    return breakout_if_id;
  }

  /**
   * Setting breakoutIF ID.
   *
   * @param breakout_if_id
   *          breakoutIF ID
   */
  public void setBreakout_if_id(String breakout_if_id) {
    this.breakout_if_id = breakout_if_id;
  }

  /**
   * Getting VLAN ID.
   *
   * @return VLAN ID
   */
  public String getVlan_id() {
    return vlan_id;
  }

  /**
   * Setting VLAN ID.
   *
   * @param vlan_id
   *          VLAN ID
   */
  public void setVlan_id(String vlan_id) {
    this.vlan_id = vlan_id;
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
   * Getting accommodated device IF address (IPv4).
   *
   * @return accommodated device IF address (IPv4)
   */
  public Integer getIpv4_prefix() {
    return ipv4_prefix;
  }

  /**
   * Setting accommodated device IF address (IPv4).
   *
   * @param ipv4_prefix
   *          accommodated device IF address (IPv4)
   */
  public void setIpv4_prefix(Integer ipv4_prefix) {
    this.ipv4_prefix = ipv4_prefix;
  }

  /**
   * Getting accommodated device IF address (IPv6).
   *
   * @return accommodated device IF address (IPv6)
   */
  public String getIpv6_address() {
    return ipv6_address;
  }

  /**
   * Setting accommodated device IF address (IPv6).
   *
   * @param ipv6_address
   *          accommodated device IF address (IPv6)
   */
  public void setIpv6_address(String ipv6_address) {
    this.ipv6_address = ipv6_address;
  }

  /**
   * Getting accommodated device IF prefix (IPv6).
   *
   * @return accommodated device IF prefix (IPv6)
   */
  public Integer getIpv6_prefix() {
    return ipv6_prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv6).
   *
   * @param ipv6_prefix
   *          accommodated device IF prefix (IPv6)
   */
  public void setIpv6_prefix(Integer ipv6_prefix) {
    this.ipv6_prefix = ipv6_prefix;
  }

  /**
   * Getting MTU value.
   *
   * @return MTU value
   */
  public Integer getMtu() {
    return mtu;
  }

  /**
   * Setting MTU value.
   *
   * @param mtu
   *          MTU value
   */
  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  /**
   * Getting port mode.
   *
   * @return port mode
   */
  public Integer getPort_mode() {
    return port_mode;
  }

  /**
   * Setting port mode.
   *
   * @param port_mode
   *          port mode
   */
  public void setPort_mode(Integer port_mode) {
    this.port_mode = port_mode;
  }

  /**
   * Getting BGPID.
   *
   * @return BGPID
   */
  public String getBgp_id() {
    return bgp_id;
  }

  /**
   * Setting BGPID.
   *
   * @param bgp_id
   *          BGPID
   */
  public void setBgp_id(String bgp_id) {
    this.bgp_id = bgp_id;
  }

  /**
   * Getting VRRPID.
   *
   * @return VRRPID
   */
  public String getVrrp_id() {
    return vrrp_id;
  }

  /**
   * Setting VRRPID.
   *
   * @param vrrp_id
   *          VRRPID
   */
  public void setVrrp_id(String vrrp_id) {
    this.vrrp_id = vrrp_id;
  }

  /**
   * Getting In flow ShapingRate.
   *
   * @return In flow ShapingRate
   */
  public Float getInflow_shaping_rate() {
    return inflow_shaping_rate;
  }

  /**
   * Setting In flow ShapingRate.
   *
   * @param inflow_shaping_rate
   *          In flow ShapingRate
   */
  public void setInflow_shaping_rate(Float inflow_shaping_rate) {
    this.inflow_shaping_rate = inflow_shaping_rate;
  }

  /**
   * Getting Out flow ShapingRate.
   *
   * @return Out flow ShapingRate
   */
  public Float getOutflow_shaping_rate() {
    return outflow_shaping_rate;
  }

  /**
   * Setting Out flow ShapingRate.
   *
   * @param outflow_shaping_rate
   *          Out flow ShapingRate
   */
  public void setOutflow_shaping_rate(Float outflow_shaping_rate) {
    this.outflow_shaping_rate = outflow_shaping_rate;
  }

  /**
   * Getting Remark menu.
   *
   * @return remark menu
   */
  public String getRemark_menu() {
    return remark_menu;
  }

  /**
   * Setting Remark menu.
   *
   * @param remark_menu
   *          Set remark_menu
   */
  public void setRemark_menu(String remark_menu) {
    this.remark_menu = remark_menu;
  }

  /**
   * Getting Egress queue menu.
   *
   * @return Egress queue menu
   */
  public String getEgress_queue_menu() {
    return egress_queue_menu;
  }

  /**
   * Setting Egress queue menu.
   *
   * @param egress_queue_menu
   *          Set egress_queue_menu
   */
  public void setEgress_queue_menu(String egress_queue_menu) {
    this.egress_queue_menu = egress_queue_menu;
  }

  /**
   * Acquire IRB instance ID.
   *
   * @return irb_instance_id
   */
  public String getIrb_instance_id() {
    return irb_instance_id;
  }

  /**
   * Set IRB instance ID.
   *
   * @param irb_instance_id
   *          Set irb_instance_id
   */
  public void setIrb_instance_id(String irb_instance_id) {
    this.irb_instance_id = irb_instance_id;
  }

  /**
   * Getting Q-in-Q information.
   *
   * @return Q-in-Q information
   */
  public Boolean getQ_in_q() {
    return q_in_q;
  }

  /**
   * Setting Q-in-Q information.
   *
   * @param q_in_q
   *          Q-in-Q information
   */
  public void setQ_in_q(Boolean q_in_q) {
    this.q_in_q = q_in_q;
  }

  /**
   * Acquire BGP Option list.
   *
   * @return BGP option list
   */
  public Set<BGPOptions> getBgpOptionsList() {
    return bgpOptionsList;
  }

  /**
   * Set BGP option list.
   *
   * @param bgpOptionsList
   *          BGP option list
   */
  public void setBgpOptionsList(Set<BGPOptions> bgpOptionsList) {
    this.bgpOptionsList = bgpOptionsList;
  }

  /**
   * Getting static route information list.
   *
   * @return static route information list
   */
  public Set<StaticRouteOptions> getStaticRouteOptionsList() {
    return staticRouteOptionsList;
  }

  /**
   * Setting static route information list.
   *
   * @param staticRouteOptionsList
   *          static route information list
   */
  public void setStaticRouteOptionsList(Set<StaticRouteOptions> staticRouteOptionsList) {
    this.staticRouteOptionsList = staticRouteOptionsList;
  }

  /**
   * Getting VRRP option list.
   *
   * @return VRRP option list
   */
  public Set<VRRPOptions> getVrrpOptionsList() {
    return vrrpOptionsList;
  }

  /**
   * Setting VRRP option list.
   *
   * @param vrrpOptionsList
   *          VRRP option list
   */
  public void setVrrpOptionsList(Set<VRRPOptions> vrrpOptionsList) {
    this.vrrpOptionsList = vrrpOptionsList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    if (vlan_if_id != null) {
      hashCode ^= vlan_if_id.hashCode();
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

    VlanIfs target = (VlanIfs) obj;
    if (this.node_id.equals(target.getNode_id()) && this.vlan_if_id.equals(target.getVlan_if_id())) {

      return true;
    }
    return false;
  }

  /*
   * Stringize instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VlanIfs [node_id=" + node_id + ", vlan_if_id=" + vlan_if_id + ", physical_if_id=" + physical_if_id
        + ", lag_if_id=" + lag_if_id + ", breakout_if_id=" + breakout_if_id + ", vlan_id=" + vlan_id + ", if_name="
        + if_name + ", if_status=" + if_status + ", ipv4_address=" + ipv4_address + ", ipv4_prefix=" + ipv4_prefix
        + ", ipv6_address=" + ipv6_address + ", ipv6_prefix=" + ipv6_prefix + ", mtu=" + mtu + ", port_mode="
        + port_mode + ", bgp_id=" + bgp_id + ", vrrp_id=" + vrrp_id + ", inflow_shaping_rate=" + inflow_shaping_rate
        + ", outflow_shaping_rate=" + outflow_shaping_rate + ", remark_menu=" + remark_menu + ", egress_queue_menu="
        + egress_queue_menu + ", irb_instance_id=" + irb_instance_id + ", q_in_q=" + q_in_q + ", bgpOptionsList="
        + bgpOptionsList + ", staticRouteOptionsList=" + staticRouteOptionsList + ", vrrpOptionsList=" + vrrpOptionsList
        + "]";
  }

}
