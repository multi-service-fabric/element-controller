/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Changing Device Information.
 */
public class UpdateNode {

  /** Device ID. */
  String nodeId;

  /** Device Type. */
  String nodeType;

  /** OSPF Area in Multicluster. */
  String clusterArea;

  /** Configuration Information of Virtual Link between B-Leafs. */
  VirtualLinkCluster virtualLink;

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
   * Getting configuration information of Virtual Link between B-Leafs.
   *
   * @return configuration information of Virtual Link between B-Leafs.
   */
  public VirtualLinkCluster getVirtualLink() {
    return virtualLink;
  }

  /**
   * Setting configuration information of Virtual Link between B-Leafs.
   *
   * @param virtualLink
   *          configuration information of Virtual Link between B-Leafs.
   */
  public void setVirtualLink(VirtualLinkCluster virtualLink) {
    this.virtualLink = virtualLink;
  }

  @Override
  public String toString() {
    return "UpdateNode [nodeId=" + nodeId + ", nodeType=" + nodeType + ", clusterArea=" + clusterArea + ", virtualLink="
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
    } else if (!nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
      throw new CheckDataException();
    }
    if (clusterArea == null) {
      throw new CheckDataException();
    }
    if (virtualLink == null) {
      throw new CheckDataException();
    } else {
      virtualLink.check(ope);
    }
  }
}
