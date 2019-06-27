/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.Node;

/**
 * Device List Information Acquisition.
 */
public class GetNodeList extends AbstractResponseMessage {

  /** Device Information. */
  private ArrayList<Node> nodes = new ArrayList<Node>();

  /**
   * Getting device information.
   *
   * @return device information
   */
  public ArrayList<Node> getNode() {
    return nodes;
  }

  /**
   * Setting device information.
   *
   * @param nodes
   *          device information
   */
  public void setNode(ArrayList<Node> nodes) {
    this.nodes = nodes;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetNodeList [nodes=" + nodes + "]";
  }
}
