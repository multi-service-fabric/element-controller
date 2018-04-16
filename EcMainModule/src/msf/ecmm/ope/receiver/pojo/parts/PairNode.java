/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Opposing B-Leaf Device Information.
 */
public class PairNode {

  /** Device ID. */
  private String nodeId;

  /** Device Type. */
  private String nodeType;

  /** OSPF Area in Multicluster. */
  private String clusterArea;

  /** VirtualLink Configuration. */
  VirtualLink virtualLink;

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
   * Getting VirtualLink configuration.
   *
   * @return VirtualLink configuration
   */
  public VirtualLink getVirtualLink() {
    return virtualLink;
  }

  /**
   * Setting VirtualLink configuration.
   *
   * @param virtualLink
   *          VirtualLink configuration
   */
  public void setVirtualLink(VirtualLink virtualLink) {
    this.virtualLink = virtualLink;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string.
   */
  @Override
  public String toString() {
    return "PairNode [nodeId=" + nodeId + ", nodeType=" + nodeType + ", clusterArea=" + clusterArea + ", virtualLink="
        + virtualLink + "]";
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
    } else if (!nodeType.equals(CommonDefinitions.NODETYPE_LEAF)
        && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
      throw new CheckDataException();
    }
    if (clusterArea == null) {
      throw new CheckDataException();
    }
    if (virtualLink != null) {
      virtualLink.check(ope);
    }
  }
}
