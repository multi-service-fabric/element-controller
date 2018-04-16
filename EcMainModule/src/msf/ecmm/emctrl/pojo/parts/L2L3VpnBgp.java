/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BGP Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bgp")
public class L2L3VpnBgp {

  /** IP Address List of Neighbors */
  @XmlElement(name = "neighbor")
  private List<L2L3VpnNeighbor> vpnNeighbor = null;

  /** BGP Community Value */
  private String community = null;

  /** BGP Prioritized/Unprioritized */
  @XmlElement(name = "community-wildcard")
  private String communityWildcard = null;

  /**
   * Generating new instance.
   */
  public L2L3VpnBgp() {
    super();
  }

  /**
   * Getting IP address list of neighbors.
   *
   * @return IP address list of neighbors
   */
  public List<L2L3VpnNeighbor> getVpnNeighbor() {
    return vpnNeighbor;
  }

  /**
   * Setting IP address list of neighbors.
   *
   * @param vpnNeighbor
   *          IP address list of neighbors
   */
  public void setVpnNeighbor(List<L2L3VpnNeighbor> vpnNeighbor) {
    this.vpnNeighbor = vpnNeighbor;
  }

  /**
   * Getting BGP community value.
   *
   * @return BGP community value
   */
  public String getCommunity() {
    return community;
  }

  /**
   * Setting BGP community value.
   *
   * @param community
   *          BGP community value
   */
  public void setCommunity(String community) {
    this.community = community;
  }

  /**
   * Getting BGP prioritized/unprioritized.
   *
   * @return BGP prioritized/unprioritized
   */
  public String getCommunityWildcard() {
    return communityWildcard;
  }

  /**
   * Setting BGP prioritized/unprioritized.
   *
   * @param communityWildcard
   *          BGP prioritized/unprioritized
   */
  public void setCommunityWildcard(String communityWildcard) {
    this.communityWildcard = communityWildcard;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3VpnBgp [l3VpnNeighbor=" + vpnNeighbor + ", community=" + community + ", communityWildcard="
        + communityWildcard + "]";
  }

}
