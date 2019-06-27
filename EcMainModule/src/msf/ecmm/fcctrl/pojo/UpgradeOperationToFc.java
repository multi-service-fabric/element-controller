/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;


/**
 * The completion of OS upgrade operation  in the node is notified.
 */
public class UpgradeOperationToFc extends AbstractRequest {

  /** The node type. */
  private String fabricType;

  /** The node ID. */
  private String nodeId;

  /** The operation type. */
  private String operationType;

  /** The result of upgrading OS in the node. */
  private String osUpgradeResult;

  /**
   * The type of the node is acquired.
   *
   * @return The type of the node
   */
  public String getFabricType() {
    return fabricType;
  }

  /**
   * The node type is set.
   *
   * @param fabricType
   *          The node type
   */
  public void setFabricType(String fabricType) {
    this.fabricType = fabricType;
  }

  /**
   * The node ID is acquired.
   *
   * @return The node type.
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * The node ID is acquired.
   *
   * @param nodeId
   *          The node ID.
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * The operation type is acquired.
   *
   * @return The operation type.
   */
  public String getOperationType() {
    return operationType;
  }

  /**
   * The operation type is set.
   *
   * @param operationType
   *          The operation type.
   */
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  /**
   * The result of the OS upgrade operation in the node is acquired.
   *
   * @return The result of the OS upgrade operation in the node.
   */
  public String getOsUpgradeResult() {
    return osUpgradeResult;
  }

  /**
   * The result of the OS upgrade operation in the node is set.
   *
   * @param osUpgradeResult
   *          The result of the OS upgrade operation in the node
   */
  public void setOsUpgradeResult(String osUpgradeResult) {
    this.osUpgradeResult = osUpgradeResult;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "UpgradeOperationToFc [fabricType=" + fabricType + ", nodeId=" + nodeId + ", operationType=" + operationType
        + ", osUpgradeResult=" + osUpgradeResult + "]";
  }

}
