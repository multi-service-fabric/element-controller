/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * BGP Configuration
 */
public class BgpAddNode {

  /** Neighbor Configuration */
  private Neighbor neighbor;

  /** Community Value of BGP */
  private String community;

  /** Expression contains both prioritized and unprioritized comunity values of BGP */
  private String communityWildcard;

  /**
   * Getting neighbor configuration.
   *
   * @return neighbor configuration
   */
  public Neighbor getNeighbor() {
    return neighbor;
  }

  /**
   * Setting neighbor configuration.
   *
   * @param neighbor
   *          neighbor configuration
   */
  public void setNeighbor(Neighbor neighbor) {
    this.neighbor = neighbor;
  }

  /**
   * Getting community value of BGP.
   *
   * @return community value of BGP
   */
  public String getCommunity() {
    return community;
  }

  /**
   * Setting community value of BGP.
   *
   * @param community
   *          community value of BGP
   */
  public void setCommunity(String community) {
    this.community = community;
  }

  /**
   * Getting expression contains both prioritized and unprioritized comunity values of BGP.
   *
   * @return expression contains both prioritized and unprioritized comunity values of BGP
   */
  public String getCommunityWildcard() {
    return communityWildcard;
  }

  /**
   * Setting expression contains both prioritized and unprioritized comunity values of BGP.
   *
   * @param communityWildcard
   *          expression contains both prioritized and unprioritized comunity values of BGP
   */
  public void setCommunityWildcard(String communityWildcard) {
    this.communityWildcard = communityWildcard;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BgpAddNode [neighbor=" + neighbor + ", community=" + community + ", communityWildcard=" + communityWildcard
        + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (neighbor == null) {
      throw new CheckDataException();
    } else {
      neighbor.check(ope);
    }
    if (community == null) {
      throw new CheckDataException();
    }
    if (communityWildcard == null) {
      throw new CheckDataException();
    }
  }

}
