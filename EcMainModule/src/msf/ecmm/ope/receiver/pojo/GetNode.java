/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.Node;

/**
 * Device Information Acquisition.
 */
public class GetNode extends AbstractResponseMessage {

  /** Device Information. */
  private Node node;

  /**
   * Getting device information.
   *
   * @return device information
   */
  public Node getNode() {
    return node;
  }

  /**
   * Setting device information.
   *
   * @param node
   *          device information
   */
  public void setNode(Node node) {
    this.node = node;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetNode [node=" + node + "]";
  }
}
