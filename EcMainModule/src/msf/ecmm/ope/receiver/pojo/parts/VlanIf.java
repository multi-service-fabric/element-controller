/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

/**
 * VlanIF Information.
 */
public class VlanIf {

  /** VLANIF ID. */
  private String vlanIfId;

  /** VLAN ID. */
  private String vlanId;

  /** IF Name. */
  private String ifName;

  /** IF Status. */
  private String ifState;

  /** MTU Value. */
  private Integer mtu;

  /** Accommodated Device IF Address (IPv4). */
  private String ipv4Address;

  /** Accommodate Device IF Address (IPv6). */
  private String ipv6Address;

  /** Accommodated Device IF Prefix (IPv4). */
  private String ipv4Prefix;

  /** Accommodated Device IF Prefix (IPv6). */
  private String ipv6Prefix;

  /** Port Mode. */
  private String portMode;

  /** Base IF Information. */
  private BaseIf baseIf;

  /** Information for BGP. */
  private Bgp bgp;

  /** Static Route Information List. */
  private ArrayList<StaticRoute> staticRoutes = new ArrayList<StaticRoute>();

  /** Information for VRRP. */
  private Vrrp vrrp;

  /** QoS capability. */
  private QosGetVlanIfs qos;

  /**
   * Getting VLANIF ID.
   *
   * @return VLANIF ID
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting VLANIF ID.
   *
   * @param vlanIfId
   *          VLANIF ID
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting VLAN ID.
   *
   * @return VLAN ID
   */
  public String getVlanId() {
    return vlanId;
  }

  /**
   * Setting VLAN ID.
   *
   * @param vlanId
   *          VLAN ID
   */
  public void setVlanId(String vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getIfName() {
    return ifName;
  }

  /**
   * Setting IF name.
   *
   * @param ifName
   *          IF name
   */
  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  /**
   * Getting IF status.
   *
   * @return IF status
   */
  public String getIfState() {
    return ifState;
  }

  /**
   * Setting IF status.
   *
   * @param ifState
   *          IF status
   */
  public void setIfState(String ifState) {
    this.ifState = ifState;
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
   * Getting MTU value.
   *
   * @param mtu
   *          MTU value
   */
  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  /**
   * Getting accommodated device IF address (IPv4).
   *
   * @return accommodated device IF address (IPv4)
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting accommodated device IF address (IPv4).
   *
   * @param ipv4Address
   *          accommodated device IF address (IPv4)
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting accommodated device IF address (IPv6).
   *
   * @return accommodated device IF address (IPv6)
   */
  public String getIpv6Address() {
    return ipv6Address;
  }

  /**
   * Setting accommodated device IF address (IPv6).
   *
   * @param ipv6Address
   *          accommodated device IF address (IPv6)
   */
  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  /**
   * Getting accommodated device IF prefix (IPv4).
   *
   * @return accommodated device IF prefix (IPv4)
   */
  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv4).
   *
   * @param ipv4Prefix
   *          accommodated device IF prefix (IPv4)
   */
  public void setIpv4Prefix(String ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting accommodated device IF prefix (IPv6).
   *
   * @return accommodated device IF prefix (IPv6)
   */
  public String getIpv6Prefix() {
    return ipv6Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv6).
   *
   * @param ipv6Prefix
   *          accommodated device IF prefix (IPv6)
   */
  public void setIpv6Prefix(String ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  /**
   * Getting port mode.
   *
   * @return port mode
   */
  public String getPortMode() {
    return portMode;
  }

  /**
   * Setting port mode.
   *
   * @param portMode
   *          port mode
   */
  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  /**
   * Getting base IF information.
   *
   * @return base IF
   */
  public BaseIf getBaseIf() {
    return baseIf;
  }

  /**
   * Setting base IF information.
   *
   * @param baseIf
   *          base IF
   */
  public void setBaseIf(BaseIf baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting information for BGP.
   *
   * @return information for BGP
   */
  public Bgp getBgp() {
    return bgp;
  }

  /**
   * Setting information for BGP.
   *
   * @param bgp
   *          information for BGP
   */
  public void setBgp(Bgp bgp) {
    this.bgp = bgp;
  }

  /**
   * Getting static route information list.
   *
   * @return static route information list
   */
  public ArrayList<StaticRoute> getStaticRoutes() {
    return staticRoutes;
  }

  /**
   * Setting static route information list.
   *
   * @param staticRoutes
   *          static route information list
   */
  public void setStaticRoutes(ArrayList<StaticRoute> staticRoutes) {
    this.staticRoutes = staticRoutes;
  }

  /**
   * Getting information for VRRP.
   *
   * @return information for VRRP
   */
  public Vrrp getVrrp() {
    return vrrp;
  }

  /**
   * Setting information for VRRP.
   *
   * @param vrrp
   *          information for VRRP
   */
  public void setVrrp(Vrrp vrrp) {
    this.vrrp = vrrp;
  }

  /**
   * Getting QoS capability.
   *
   * @return qos
   */
  public QosGetVlanIfs getQos() {
    return qos;
  }

  /**
   * Setting QoS capability.
   *
   * @param qos
   *          Setting qos
   */
  public void setQos(QosGetVlanIfs qos) {
    this.qos = qos;
  }

  /**
   * Stringizing Instance
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VlanIf [vlanIfId=" + vlanIfId + ", vlanId=" + vlanId + ", ifName=" + ifName + ", ifState=" + ifState
        + ", mtu=" + mtu + ", ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix="
        + ipv4Prefix + ", ipv6Prefix=" + ipv6Prefix + ", portMode=" + portMode + ", baseIf=" + baseIf + ", bgp=" + bgp
        + ", staticRoutes=" + staticRoutes + ", vrrp=" + vrrp + ", qos=" + qos + "]";
  }
}
