/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeOsUpgrade;

/**
 * Class for upgrading OS in the node.
 */
public class NodeOsUpgradeRequest extends AbstractRestMessage {

  /** The equipment information. */
  private EquipmentAddNode equipment;
  /** The node information. */
  private NodeOsUpgrade node;

  /**
   * The equipment information is acquired.
   *
   * @return The equipment information
   */
  public EquipmentAddNode getEquipment() {
    return equipment;
  }

  /**
   * The equipment infomation is set.
   *
   * @param equipment
   *          The equipment information
   */
  public void setEquipment(EquipmentAddNode equipment) {
    this.equipment = equipment;
  }

  /**
   * The node information is set.
   *
   * @return The node information.
   */
  public NodeOsUpgrade getNode() {
    return node;
  }

  /**
   * The node information is set.
   *
   * @param node
   *          The node information.
   */
  public void setNode(NodeOsUpgrade node) {
    this.node = node;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodeOsUpgradeRequest [equipment=" + equipment + ", node=" + node + "]";
  }

  /**
   * The input parameers are checked.
   *
   * @param ope
   *          The Operation type
   * @throws CheckDataException
   *          The input paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (equipment == null) {
      throw new CheckDataException();
    }
    equipment.check(ope);
    if (node == null) {
      throw new CheckDataException();
    }
    node.check(ope);
  }
}
