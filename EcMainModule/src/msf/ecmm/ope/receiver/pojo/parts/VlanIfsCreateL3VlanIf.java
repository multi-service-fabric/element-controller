/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VLAN IF to be controlled information.
 */
public class VlanIfsCreateL3VlanIf {

  /** ID of VLAN IF to be controlled. */
  private String vlanIfId = null;

  /** Information of IF in which CP is created. */
  private BaseIfCreateVlanIf baseIf = null;

  /** CP VLAN ID. */
  private Integer vlanId = null;
	
  /** MTU Value for each CP IF. */
  private Integer mtu = null;

  /** Accommodated Device IF Address (IPv4). */
  private String ipv4Address = null;

  /** Accommodated Device IF Address (IPv6). */
  private String ipv6Address = null;
	
  /** Accommodate Device IF Prefix (IPv4). */
  private Integer ipv4Prefix = null;

  /** Accommodated Device IF Prefix (IPv6). */
  private Integer ipv6Prefix = null;

  /** Information for BGP. */
  private Bgp bgp = null;

  /** Static Route Information List. */
  private ArrayList<StaticRoute> staticRoutes;

  /** Information for VRRP. */
  private VrrpCreateVlanIf vrrp = null;

  /** QoS configuration. */
  private QosValues qos = null;

  /** Route Distingusher. */
  private String routeDistinguisher = null;

  /**
   * Getting ID information of CP to be controlled.
   *
   * @return ID information of CP to be controlled
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting ID information of CP to be controlled.
   *
   * @param vlanIfId
   *          ID information of CP to be controlled
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting information of IF in which CP is created.
   *
   * @return information of IF in which CP is created
   */
  public BaseIfCreateVlanIf getBaseIf() {
    return baseIf;
  }

  /**
   * Setting information of IF in which CP is created.
   *
   * @param baseIf
   *          information of IF in which CP is created
   */
  public void setBaseIf(BaseIfCreateVlanIf baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting CP VLAN ID.
   *
   * @return CP VLAN ID
   */
  public Integer getVlanId() {
    return vlanId;
  }

  /**
   * Setting CP VLAN ID.
   *
   * @param vlanId
   *          CP VLAN ID
   */
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Getting MTU value for each CP IF.
   *
   * @return MTU value for each CP IF
   */
  public Integer getMtu() {
    return mtu;
  }

  /**
   * Setting MTU value for each CP IF.
   *
   * @param mtu
   *          MTU value for each CP IF
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
   * Setting accommodated device If address (IPv6).
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
  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv4).
   *
   * @param ipv4Prefix
   *          accommodated device IF prefix (IPv4)
   */
  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting accommodated device IF prefix (IPv6).
   *
   * @return accommodated device IF prefix (IPv6)
   */
  public Integer getIpv6Prefix() {
    return ipv6Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv6).
   *
   * @param ipv6Prefix
   *          accommodated device IF prefix (IPv6)
   */
  public void setIpv6Prefix(Integer ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
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
  public VrrpCreateVlanIf getVrrp() {
    return vrrp;
  }

  /**
   * Setting information for VRRP.
   *
   * @param vrrp
   *          information for VRRP
   */
  public void setVrrp(VrrpCreateVlanIf vrrp) {
    this.vrrp = vrrp;
  }

  /**
   * Getting QoS configuration.
   *
   * @return qos
   */
  public QosValues getQos() {
    return qos;
  }

  /**
   * Setting QoS configuration.
   *
   * @param qos
   *          setting qos
   */
  public void setQos(QosValues qos) {
    this.qos = qos;
  }

  /**
   * Getting Route Distingusher.
   *
   * @return routeDistingusher
   */
  public String getRouteDistingusher() {
    return routeDistinguisher;
  }

  /**
   * Setting Route Distingusher.
   *
   * @param rd
   *          set Route Distingusher
   */
  public void setRouteDistingusher(String rd) {
    this.routeDistinguisher = rd;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "VlanIfsCreateL3VlanIf [vlanIfId=" + vlanIfId + ", baseIf=" + baseIf + ", vlanId=" + vlanId + ", mtu=" + mtu
        + ", ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix=" + ipv4Prefix
        + ", ipv6Prefix=" + ipv6Prefix + ", bgp=" + bgp + ", staticRoutes=" + staticRoutes + ", vrrp=" + vrrp + ", qos="
        + qos + ", routeDistinguisher=" + routeDistinguisher + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (baseIf == null) {
      throw new CheckDataException();
    } else {
      baseIf.check(ope);
    }
    if (vlanId == null) {
      throw new CheckDataException();
    }
    if (mtu == null) {
      throw new CheckDataException();
    }
    if (ipv4Address == null && ipv6Address == null) {
      throw new CheckDataException();
    }
    if (ipv4Address != null) {
      if (ipv4Prefix == null) {
        throw new CheckDataException();
      }
    }
    if (ipv6Address != null) {
      if (ipv6Prefix == null) {
        throw new CheckDataException();
      }
    }
    if (bgp != null) {
      bgp.check(ope);
    }
    if (staticRoutes != null) {
      for (StaticRoute sr : staticRoutes) {
        sr.check(ope);
      }
    }
    if (vrrp != null) {
      vrrp.check(ope);
    }
    if (routeDistinguisher == null) {
      throw new CheckDataException();
    }
  }

}
