/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import java.util.ArrayList;

import msf.ecmm.fcctrl.pojo.parts.IfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;

/**
 * Logical IF Status Update.
 */
public class UpdateLogicalIfStatus extends AbstractRequest {

  /** Device Information List. */
  private ArrayList<NodeLogical> nodes = new ArrayList<NodeLogical>();

  /** IF Information List. */
  private ArrayList<IfsLogical> ifs = new ArrayList<IfsLogical>();

  /**
   * Getting device information list.
   *
   * @return device information list
   */
  public ArrayList<NodeLogical> getNodes() {
    return nodes;
  }

  /**
   * Setting device information list.
   *
   * @param nodes
   *          device information list
   */
  public void setNodes(ArrayList<NodeLogical> nodes) {
    this.nodes = nodes;
  }

  /**
   * Getting IF information list.
   *
   * @return ifs
   */
  public ArrayList<IfsLogical> getIfs() {
    return ifs;
  }

  /**
   * Setting IF information list.
   *
   * @param ifs
   *          set ifs
   */
  public void setIfs(ArrayList<IfsLogical> ifs) {
    this.ifs = ifs;
  }

  /**
   * Stiringizing Instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "UpdateLogicalIfStatus [nodes=" + nodes + ", ifs=" + ifs + "]";
  }

}
