/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.receiver.pojo.parts.NodeConfigAll;

/**
 * Getting Config-Audit list.
 */
public class GetConfigAuditList extends AbstractResponseMessage {

  /** List of Config information and difference information. */
  private List<NodeConfigAll> nodes = new ArrayList<>();

  /**
   * Getting List of Config information and difference information.
   *
   * @return List of Config information and difference information.
   */
  public List<NodeConfigAll> getNodes() {
    return nodes;
  }

  /**
   * Setting List of Config information and difference information.
   *
   * @param nodes
   *          List of Config information and difference information.
   */
  public void setNodes(List<NodeConfigAll> nodes) {
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    return "GetConfigAuditList [nodes=" + nodes + "]";
  }

}
