/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * L3 Slice Static Connection Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "static")
public class L3SliceStatic {

  /** IPv4 User Route Information List */
  @XmlElement(name = "route")
  private List<Route> routeList = null;

  /** IPv6 User Route Information List */
  @XmlElement(name = "route6")
  private List<Route> routeList6 = null;

  /**
   * Generating new instance.
   */
  public L3SliceStatic() {
    super();
  }

  /**
   * Getting IPv4 user route information list.
   *
   * @return IPv4 user route information list
   */
  public List<Route> getRouteList() {
    return routeList;
  }

  /**
   * Setting IPv4 user route information list.
   *
   * @param routeList
   *          IPv4 user route information list
   */
  public void setRouteList(List<Route> routeList) {
    this.routeList = routeList;
  }

  /**
   * Getting IPv6 user route information list.
   *
   * @return IPv6 user route information list
   */
  public List<Route> getRouteList6() {
    return routeList6;
  }

  /**
   * Setting IPv6 user route information list.
   *
   * @param routeList6
   *          IPv6 user route information list
   */
  public void setRouteList6(List<Route> routeList6) {
    this.routeList6 = routeList6;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3SliceStatic [routeList=" + routeList + ", routeList6=" + routeList6 + "]";
  }
}
