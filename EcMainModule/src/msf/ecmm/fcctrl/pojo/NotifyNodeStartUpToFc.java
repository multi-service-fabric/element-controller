/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import msf.ecmm.ope.receiver.pojo.AddNode;

/**
 * Device Extention Completion Notification.
 */
public class NotifyNodeStartUpToFc extends AbstractRequest {

  /** Device Start-up Result. */
  private String status;

  /** Extended Device Information. */
  private AddNode nodeInfo;

  /**
   * Getting device start-up result.
   *
   * @return device start-up result
   */
  public String getStatus() {
    return status;
  }

  /**
   * Setting device start-up result.
   *
   * @param status
   *          device start-up result
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Getting extended device information.
   *
   * @return extended device information.
   */
  public AddNode getNodeInfo() {
    return nodeInfo;
  }

  /**
   * Setting extended device information.
   *
   * @param nodeInfo
   *          extended device information.
   */
  public void setNodeInfo(AddNode nodeInfo) {
    this.nodeInfo = nodeInfo;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "NotifyNodeStartUp [status=" + status + "]";
  }

}
