/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.NodeChangeNode;
import msf.ecmm.ope.receiver.pojo.parts.PairNode;

/**
 * Device Change Class.
 */
public class ChangeNode extends AbstractRestMessage {

  /** Control Type. */
  private String action;

  /** Device Information. */
  NodeChangeNode node;

  /** Opposing B-Leaf Device Information. */
  PairNode pairNode;

  /**
   * Getting device type.
   * @return device type
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting device type.
   * @param action device type
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting device information.
   * @return device information
   */
  public NodeChangeNode getNode() {
    return node;
  }

  /**
   * Setting device information.
   * @param node device information
   */
  public void setNode(NodeChangeNode node) {
    this.node = node;
  }

  /**
   * Getting opposing B-Leaf device information.
   * @return opposing B-Leaf device information
   */
  public PairNode getPairNode() {
    return pairNode;
  }

  /**
   * Setting opposing B-Leaf device information.
   * @param pairNode opposing B-Leaf device information
   */
  public void setPairNode(PairNode pairNode) {
    this.pairNode = pairNode;
  }

  /**
   * Stingizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "ChangeNode [action=" + action + ", node=" + node + ", pairNode=" + pairNode + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operatioin type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (action == null) {
      throw new CheckDataException();
    }
    if (node == null) {
      throw new CheckDataException();
    } else {
      node.check(ope);
    }
    if (pairNode != null) {
      pairNode.check(ope);
    }
  }
}
