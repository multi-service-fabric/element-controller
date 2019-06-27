/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.fcctrl.pojo.parts.NodeConfigToFc;

/**
 * Notified Config-Audit difference information.
 */
public class ConfigAuditNotification extends AbstractRequest {

  /** List of Config information and diffence information. */
  private List<NodeConfigToFc> node = new ArrayList<>();

  /**
   * Getting list of Config information and diffence information.
   *
   * @return list of Config information and diffence information
   */
  public List<NodeConfigToFc> getNodes() {
    return node;
  }

  /**
   * Setting list of Config information and diffence information.
   *
   * @param node
   *          list of Config information and diffence information
   */
  public void setNodes(List<NodeConfigToFc> node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return "ConfigAuditNotification [node=" + node + "]";
  }

}
