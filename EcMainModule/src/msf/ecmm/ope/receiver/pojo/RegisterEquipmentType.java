/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.Equipment;

/**
 * Model Information Registration
 */
public class RegisterEquipmentType extends AbstractRestMessage {

  /** Model Information */
  private Equipment equipment;

  /**
   * Getting model information.
   *
   * @return model information
   */
  public Equipment getEquipment() {
    return equipment;
  }

  /**
   * Setting model information.
   *
   * @param equipment
   *          model information
   */
  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  /**
   * Stringinzing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "RegiterEquipmentType [equipment=" + equipment + "]";
  }

  /**
   * Input Parameter Check
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
  }
}
