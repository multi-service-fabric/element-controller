/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Geting and Setting received Config-Audit.
 */
public class NodeConfigAll {

  /** Config information and difference informaton. */
  private NodeConfig node;

  /**
   * Getting config information and difference informaton.
   *
   * @return config information and difference informaton.
   */
  public NodeConfig getNode() {
    return node;
  }

  /**
   * Setting config information and difference informaton.
   *
   * @param node
   *          config information and difference informaton.
   */
  public void setNode(NodeConfig node) {
    this.node = node;
  }

  @Override
  public String toString() {
	return "NodeConfigAll [node=" + node + "]";
  }

}
