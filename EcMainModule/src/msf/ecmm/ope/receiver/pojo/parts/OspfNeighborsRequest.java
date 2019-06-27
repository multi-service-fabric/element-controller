/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * The OSPF neighbor information(in the request).
 */
public class OspfNeighborsRequest {

  /** The node ID. */
  private String nodeId;

  /** The OSPF neighbor IF information. */
  private List<BaseIfInfo> ospfNeighborIfs = new ArrayList<>();

  /**
   * The node ID is acquired.
   *
   * @return The node ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * The node ID is set
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
  public List<BaseIfInfo> getOspfNeighborIfs() {
    return ospfNeighborIfs;
  }

  /**
   * The OSPF neighbor IF information is set.
   *
   * @param ospfNeighborIfs
   *          The OSPF neighbor IF information
   */
  public void setOspfNeighborIfs(List<BaseIfInfo> ospfNeighborIfs) {
    this.ospfNeighborIfs = ospfNeighborIfs;
  }

  @Override
  public String toString() {
    return "RequestOspfNeighbors [nodeId=" + nodeId + ", ospfNeighborIfs=" + ospfNeighborIfs + "]";
  }

  /**
   * The input paramter is checked.
   *
   * @param ope
   *          The operation type
   * @throws CheckDataException
   *           The input paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (nodeId == null) {
      throw new CheckDataException();
    }

    if (ospfNeighborIfs.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (BaseIfInfo ospfNeighborIf : ospfNeighborIfs) {
        if (ospfNeighborIf == null) {
          throw new CheckDataException();
        } else {
          ospfNeighborIf.check(ope);
        }
      }
    }
  }
}
