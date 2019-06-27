/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

/**
 * VlanIF information.
 */
public class VlanIf {

  /** VLANIF ID. */
  private String vlanIfId;

  /** VLAN ID. */
  private String vlanId;

  /** IF name. */
  private String ifName;

  /** IF state. */
  private String ifState;

  /** MTU value. */
  private Integer mtu;

  /** Accommodated device IF address(IPv4). */
  private String ipv4Address;

  /** Accommodated device IF address(IPv6). */
  private String ipv6Address;

  /** Accomodated device IF prefix(IPv4). */
  private String ipv4Prefix;

  /** Accomodated device IF prefix(IPv6). */
  private String ipv6Prefix;

  /** Port mode. */
  private String portMode;

  /** Base IF information. */
  private BaseIf baseIf;

  /** Information for BGP. */
  private Bgp bgp;

  /** Static route information list. */
  private ArrayList<StaticRoute> staticRoutes = new ArrayList<StaticRoute>();

  /** Information for VRRP. */
  private Vrrp vrrp;

  /** QoS capability. */
  private QosGetVlanIfs qos;

  /** IRB instance configuration. */
  private IrbUpdateValue irb = null;

  /** Q-in-Q. */
  private Boolean qInQ;

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
   * Getting IF state.
   *
   * @return IF state
   */
  public String getIfState() {
    return ifState;
  }

  /**
   * Setting IF state.
   *
   * @param ifState
   *          IF state
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
   * Setting MTU value.
   *
   * @param mtu
   *          MTU value
   */
  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  /**
   * Getting accomodated device IF address(IPv4).
   *
   * @return accomodated device IF address(IPv4)
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting accomodated device IF address(IPv4).
   *
   * @param ipv4Address
   *          accomodated device IF address(IPv4)
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting accomodated device IF address(IPv6).
   *
   * @return accomodated device IF address(IPv6)
   */
  public String getIpv6Address() {
    return ipv6Address;
  }

  /**
   * Setting accomodated device IF address(IPv6).
   *
   * @param ipv6Address
   *          accomodated device IF address(IPv6)
   */
  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  /**
   * Getting accomodated device IF address(IPv4).
   *
   * @return Accomodated device IF prefix(IPv4)
   */
  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting accomodated device IF prefix(IPv4).
   *
   * @param ipv4Prefix
   *          accomodated device IF prefix(IPv4)
   */
  public void setIpv4Prefix(String ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting accomodated device IF prefix(IPv6).
   *
   * @return Accomodated device IF prefix(IPv6)
   */
  public String getIpv6Prefix() {
    return ipv6Prefix;
  }

  /**
   * Setting accomodated device IF prefix(IPv6).
   *
   * @param ipv6Prefix
   *          Accomodated device IF prefix(IPv6)
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
   * Getting Base IF Information.
   *
   * @return Base IF
   */
  public BaseIf getBaseIf() {
    return baseIf;
  }

  /**
   * Setting Base IF Information.
   *
   * @param baseIf
   *          Base IF
   */
  public void setBaseIf(BaseIf baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting information for BGP.
   *
   * @return Information for BGP
   */
  public Bgp getBgp() {
    return bgp;
  }

  /**
   * Setting information for BGP.
   *
   * @param bgp
   *          Information for BGP
   */
  public void setBgp(Bgp bgp) {
    this.bgp = bgp;
  }

  /**
   * Getting static route information list.
   *
   * @return Static route information list
   */
  public ArrayList<StaticRoute> getStaticRoutes() {
    return staticRoutes;
  }

  /**
   * Setting static route information list.
   *
   * @param staticRoutes
   *          Static route information list
   */
  public void setStaticRoutes(ArrayList<StaticRoute> staticRoutes) {
    this.staticRoutes = staticRoutes;
  }

  /**
   * Getting information for VRRP.
   *
   * @return Information for VRRP
   */
  public Vrrp getVrrp() {
    return vrrp;
  }

  /**
   * Setting information for VRRP.
   *
   * @param vrrp
   *          Information for VRRP
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
   * Getting IRB instance information.
   *
   * @return irb
   */
  public IrbUpdateValue getIrbValue() {
    return irb;
  }

  /**
   * Setting IRB instance information.
   *
   * @param irb
   *          Setting irb
   */
  public void setIrbValue(IrbUpdateValue irb) {
    this.irb = irb;
  }

  /**
   * Getting Q-in-Q.
   *
   * @return Q-in-Q
   */
  public Boolean getqInQ() {
    return qInQ;
  }

  /**
   * Setting Q-in-Q.
   *
   * @param qInQ
   *          Q-in-Q
   */
  public void setqInQ(Boolean qInQ) {
    this.qInQ = qInQ;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VlanIf [vlanIfId=" + vlanIfId + ", vlanId=" + vlanId + ", ifName=" + ifName + ", ifState=" + ifState
        + ", mtu=" + mtu + ", ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix="
        + ipv4Prefix + ", ipv6Prefix=" + ipv6Prefix + ", portMode=" + portMode + ", baseIf=" + baseIf + ", bgp=" + bgp
        + ", staticRoutes=" + staticRoutes + ", vrrp=" + vrrp + ", qos=" + qos + ", irb=" + irb + ", qInQ=" + qInQ
        + "]";
  }
}
