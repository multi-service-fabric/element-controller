/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * The OSPF neighbor information(in the response).
 */
public class OspfNeighborsResponse {

  /** The node ID. */
  private String nodeId;

  /** OSPF neighbor IF information. */
  private List<OspfNeighborIfsResponse> ospfNeighborIfs = new ArrayList<>();

  /**
   * The node ID is acquired.
   *
   * @return The node ID 
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * The node ID is set.
   *
   * @param nodeId
   *          The node ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * The OSPF neighbor IF information is acquired.
   *
   * @return The OSPF neighbor IF information
   */
  public List<OspfNeighborIfsResponse> getOspfNeighborIfs() {
    return ospfNeighborIfs;
  }

  /**
   * The OSPF neighbor IF information is set.
   *
   * @param ospfNeighborIfs
   *          The OSPF neighbor IF information
   */
  public void setOspfNeighborIfs(List<OspfNeighborIfsResponse> ospfNeighborIfs) {
    this.ospfNeighborIfs = ospfNeighborIfs;
  }

  @Override
  public String toString() {
    return "OspfNeighborsResponse [nodeId=" + nodeId + ", ospfNeighborIfs=" + ospfNeighborIfs + "]";
  }

}
