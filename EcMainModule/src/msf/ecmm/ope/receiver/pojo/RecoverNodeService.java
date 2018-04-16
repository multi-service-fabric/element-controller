/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeRecoverNode;

/**
 * Recover Node Service Class.
 */
public class RecoverNodeService extends AbstractRestMessage {

  /** Model Information. */
  private EquipmentAddNode equipment;

  /** Device Information. */
  private NodeRecoverNode node;

  /**
   * Getting Model Information.
   *
   * @return model information
   */
  public EquipmentAddNode getEquipment() {
    return equipment;
  }

  /**
   * Setting Model Information.
   *
   * @param equipment
   *          model information
   */
  public void setEquipment(EquipmentAddNode equipment) {
    this.equipment = equipment;
  }

  /**
   * Getting Device Information.
   *
   * @return device information
   */
  public NodeRecoverNode getNode() {
    return node;
  }

  /**
   * Setting Device Information.
   *
   * @param node
   *          device information
   */
  public void setNode(NodeRecoverNode node) {
    this.node = node;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RecoverNodeService [equipment=" + equipment + ", node=" + node + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *            input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (equipment == null) {
      throw new CheckDataException();
    } else {
      equipment.check(ope);
    }
    if (node == null) {
      throw new CheckDataException();
    } else {
      node.check(ope);
    }
  }

}
