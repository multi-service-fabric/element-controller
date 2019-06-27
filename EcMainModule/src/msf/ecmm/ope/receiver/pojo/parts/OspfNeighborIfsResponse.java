/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * The OSPF neighbor IF information(in the response).
 */
public class OspfNeighborIfsResponse {

  /** The node ID of the OSPF neighbor IF. */
  private String nodeId;

  /** The IF type the OSPF neighbor (physical IF/Lag IF/breakout IF). */
  private String ifType;

  /** The node IFID of the OSPF neighbor IF. */
  private String ifId;

  /**
   * The status of the OSPF neighbor IF.
   *("up"：ospfNbrState is 8（full）and the link is OK.
   * "down"：ospfNbrState is not 8（full）and the link is NG.
   * "notfound"：No neighbor inormation exists.
   * "unexecuted"：Unexecuted because the MIB acquisition in the node failed.).
   */
  private String status;

  /**
   * The node ID for the OSPF neighbor IF is acquired.
   *
   * @return The node ID for the  OSPF neighbor IF
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * The node ID for the OSPF neighbor IF is set.
   *
   * @param nodeId
   *          The node ID for the OSPF neighbor IF
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * The type of OSPF neighbor IF is acquired.
   *
   * @return The type of OSPF neighbor IF
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * The type of the OSPF neighbor IF is set.
   *
   * @param ifType
   *          The type of OSPF neighbor IF
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * The IFID of OSPF neighbor IF is acquired.
   *
   * @return The IFID of OSPF neighbor IF
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * The IFID of the OSPF neighbor IF is set.
   *
   * @param ifId
   *          The IFID of the OSPF neighbor IF
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * The status of the OSPF neighbor IF is acquired.
   *
   * @return The status of OSPF neighbor IF
   */
  public String getStatus() {
    return status;
  }

  /**
   * The status of the OSPF neighbor IF is set.
   *
   * @param status
   *          The status of the OSPF neighbor IF
   */
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "OspfNeighborIfsResponse [nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + ", status=" + status
        + "]";
  }

}
