/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Device Extention Notification.
 */
public class NodeChangeNode {

  /** Device ID. */
  private String nodeId;

  /** Device type. */
  private String nodeType;

  /** OSPF Area at the time of multi cluster. */
  private String clusterArea;

  /** VirtualLink configuraiton. */
  VirtualLink virtualLink;

  /** OSPF route aggregation configuraiton. */
  AddressInfo range;

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
   *          Device ID
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
   * Getting device information.
   *
   * @param nodeType
   *          Device information
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting OSPF Area at the time of multi-cluster.
   *
   * @return OSPF Area at the time of multi-cluster
   */
  public String getClusterArea() {
    return clusterArea;
  }

  /**
   * Setting OSPF Area at the time of multi-cluster.
   *
   * @param clusterArea
   *          OSPF Area at the time of multi-cluster
   */
  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  /**
   * Getting VirtualLink configuration
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
   * Getting OSPF route aggregation configuration.
   *
   * @return OSPF route aggregation configuration
   */
  public AddressInfo getRange() {
    return range;
  }

  /**
   * Setting OSPF route aggregation configuration.
   *
   * @param range
   *          OSPF route aggregation configuration
   */
  public void setRange(AddressInfo range) {
    this.range = range;
  }

  /**
   * Stringizing instance.
   *
   * @return Instance string.
   */
  @Override
  public String toString() {
    return "NodeChangeNode [nodeId=" + nodeId + ", nodeType=" + nodeType + ", clusterArea=" + clusterArea
        + ", virtualLink=" + virtualLink + ", range=" + range + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
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
    if (range != null) {
      range.check(ope);
    }
  }
}
