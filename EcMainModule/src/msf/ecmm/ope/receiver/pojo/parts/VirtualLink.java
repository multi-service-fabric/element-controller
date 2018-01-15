/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VirtualLink Configuration Class.
 */
public class VirtualLink {

  /** Opposing Border-Leaf Device ID. */
  private String nodeId;

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
    return "VirtualLink [nodeId=" + nodeId + "]";
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
