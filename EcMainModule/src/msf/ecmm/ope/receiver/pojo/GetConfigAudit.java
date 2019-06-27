/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.NodeConfig;

/**
 * Config-Audit List Acquisition
 */
public class GetConfigAudit extends AbstractResponseMessage {

  /** The configuration information the diffrence information in the node. */
  private NodeConfig node;

  /**
   * Configuration informaton and difference information are acquired.
   *
   * @return Configuration informaton and difference information
   */
  public NodeConfig getNode() {
    return node;
  }

  /**
   * Configuration informaton and difference information are set.
   *
   * @param node
   *          Configuration informaton and difference information
   */
  public void setNode(NodeConfig node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return "GetConfigAudit [node=" + node + "]";
  }

}
