/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Device Interface Information.
 */
public class IfsLogical {

  /** Device ID. */
  private String nodeId;

  /** IF Type. */
  private String ifType;

  /** IF ID. */
  private String ifId;

  /** IF Status. */
  private String status;

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
   * Getting IF type.
   *
   * @return ifType
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          set ifType
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID.
   *
   * @return ifId
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param ifId
   *          set ifId
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Getting IF status.
   *
   * @return status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Setting IF status.
   *
   * @param status
   *          set status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "IfsLogical [nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + ", status=" + status + "]";
  }

}
