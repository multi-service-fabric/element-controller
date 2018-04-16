/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Changing Device Information for Device Removal.
 */
public class UpdateDeleteNode {

  /** Device ID. */
  String nodeId;

  /** Device Type. */
  String nodeType;

  /** OSPF Area in Multicluster. */
  String clusterArea;

  /**
   * Getting device ID.
   *
   * @return device ID.
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID.
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting device type.
   *
   * @return device type.
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Setting device type.
   *
   * @param nodeType
   *          device type.
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting OSPF Area in multicluster.
   *
   * @return OSPF Area in multicluster
   */
  public String getClusterArea() {
    return clusterArea;
  }

  /**
   * Setting OSPF Area in multicluster.
   *
   * @param clusterArea
   *          OSPF Area in multicluster
   */
  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdateDeleteNode [nodeId=" + nodeId + ", nodeType=" + nodeType + ", clusterArea=" + clusterArea + "]";
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
      if (!nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
        throw new CheckDataException();
      }
    }
    if (clusterArea == null) {
      throw new CheckDataException();
    }
  }
}
