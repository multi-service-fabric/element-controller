/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Virtual Link Configuration Information Between B-Leafs.
 */
public class VirtualLinkCluster {

  /** Opposing Border-Leaf Device ID. */
  String nodeId;

  /**
   * Getting opposing Border-Leaf device ID.
   *
   * @return opposing Border-Leaf device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting opposing Border-Leaf device ID.
   *
   * @param nodeId
   *          opposing Border-Leaf device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return "VirtualLinkCluster [nodeId=" + nodeId + "]";
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
    if (nodeId == null) {
      throw new CheckDataException();
    }
  }
}
