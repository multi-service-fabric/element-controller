/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Device Information List.
 */
public class NodeLogical {

  /** Device ID. */
  private String nodeId;

  /** Failure Status. */
  private String failureStatus;

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
   * Getting failure status.
   *
   * @return failureStatus
   */
  public String getFailureStatus() {
    return failureStatus;
  }

  /**
   * Setting failure status.
   *
   * @param failureStatus
   *          set failureStatus
   */
  public void setFailureStatus(String failureStatus) {
    this.failureStatus = failureStatus;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "NodeLogical [nodeId=" + nodeId + ", failureStatus=" + failureStatus + "]";
  }

}
