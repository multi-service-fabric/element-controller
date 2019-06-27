/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.receiver.pojo.parts.OspfNeighborsResponse;

/**
 * The OSPF neighbor information is acquired(in the response).
 */
public class GetOspfNeighborInfoResult extends AbstractResponseMessage {

  /** The OSPF neighbor information. */
  private List<OspfNeighborsResponse> ospfNeighbors = new ArrayList<>();

  /**
   * The OSPF neighbor information is acquired.
   *
   * @return The OSPF neighbor information
   */
  public List<OspfNeighborsResponse> getOspfNeighbors() {
    return ospfNeighbors;
  }

  /**
   * The OSPF neighbor information is set.
   *
   * @param ospfNeighbors
   *          The OSPF neighbor information
   */
  public void setOspfNeighbors(List<OspfNeighborsResponse> ospfNeighbors) {
    this.ospfNeighbors = ospfNeighbors;
  }

  @Override
  public String toString() {
    return "GetOspfNeighborInfoResult [ospfNeighbors=" + ospfNeighbors + "]";
  }

}
