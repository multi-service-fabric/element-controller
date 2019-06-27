/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;

/**
 * Updating node information.
 */
public class NodeInfoUpdate extends AbstractRestMessage {

  /** Node information. */
  private EquipmentAddNode node;

  /**
   * Getting  node information.
   *
   * @return node information.
   */
  public EquipmentAddNode getNode() {
    return node;
  }

  /**
   * Getting  node information.
   *
   * @param node
   *           node information.
   */
  public void setNode(EquipmentAddNode node) {
    this.node = node;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodeInfoUpdate [node=" + node + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (node == null) {
      throw new CheckDataException();
    } else {
      node.check(ope);
    }
  }
}
