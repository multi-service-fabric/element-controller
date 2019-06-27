/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Device Information for Device Removal.
 */
public class NodeDeleteNode {

  /** Device ID. */
  private String nodeId;

  /** Device Type. */
  private String nodeType;

  /** IF Information of Opposing Device. */
  private ArrayList<OppositeNodesDeleteNode> oppositeNodes = new ArrayList<OppositeNodesDeleteNode>();

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting device information.
   *
   * @return device information
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Setting device information.
   *
   * @param nodeType
   *          device information
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting IF information of opposing device.
   *
   * @return IF information of opposing device
   */
  public ArrayList<OppositeNodesDeleteNode> getOppositeNodes() {
    return oppositeNodes;
  }

  /**
   * Setting IF information of opposing device.
   *
   * @param oppositeNodes
   *          IF information of opposing device
   */
  public void setOppositeNodes(ArrayList<OppositeNodesDeleteNode> oppositeNodes) {
    this.oppositeNodes = oppositeNodes;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string.
   */
  @Override
  public String toString() {
    return "NodeDeleteNode [nodeId=" + nodeId + ", nodeType=" + nodeType + ", oppositeNodes=" + oppositeNodes + "]";
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
    if (nodeType == null) {
      throw new CheckDataException();
    } else {
      if (!nodeType.equals(CommonDefinitions.NODETYPE_SPINE)
          && !nodeType.equals(CommonDefinitions.NODETYPE_LEAF)
          && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
        throw new CheckDataException();
      }
    }
    if (!oppositeNodes.isEmpty()) {
      for (OppositeNodesDeleteNode oppoNodes : oppositeNodes) {
        oppoNodes.check(ope);
      }
    }
  }
}
