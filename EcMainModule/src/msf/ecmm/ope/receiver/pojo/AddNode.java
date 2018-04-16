/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CreateNode;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.UpdateNode;

/**
 * Device Extention/Change Class.
 */
public class AddNode extends AbstractRestMessage {

  /** Model Information. */
  private EquipmentAddNode equipment;

  /** Generating Device Information. */
  private CreateNode createNode;

  /** Changing Device Information. */
  private UpdateNode updateNode;

  /**
   * Getting model information.
   *
   * @return model information
   */
  public EquipmentAddNode getEquipment() {
    return equipment;
  }

  /**
   * Setting model information.
   *
   * @param equipment
   *          model information
   */
  public void setEquipment(EquipmentAddNode equipment) {
    this.equipment = equipment;
  }

  /**
   * Getting generating device information.
   *
   * @return generating device information
   */
  public CreateNode getCreateNode() {
    return createNode;
  }

  /**
   * Setting generating device information.
   *
   * @param createNode
   *          generating device information
   */
  public void setCreateNode(CreateNode createNode) {
    this.createNode = createNode;
  }

  /**
   * Getting changing device information.
   *
   * @return changing device information
   */
  public UpdateNode getUpdateNode() {
    return updateNode;
  }

  /**
   * Setting changing device information.
   *
   * @param updateNode
   *          changing device information
   */
  public void setUpdateNode(UpdateNode updateNode) {
    this.updateNode = updateNode;
  }

  @Override
  public String toString() {
    return "AddNode [equipment=" + equipment + ", createNode=" + createNode + ", updateNode=" + updateNode + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (equipment == null) {
      throw new CheckDataException();
    } else {
      equipment.check(ope);
    }
    if (createNode == null) {
      throw new CheckDataException();
    } else {
      createNode.check(ope);
    }
    if (updateNode != null) {
      updateNode.check(ope);
    }
  }
}
