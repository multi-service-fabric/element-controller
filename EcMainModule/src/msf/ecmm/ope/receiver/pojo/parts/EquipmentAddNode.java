/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Model Information for Device Extention.
 */
public class EquipmentAddNode {

  /** Device ID. */
  private String equipmentTypeId;

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  /**
   * Setting device ID.
   * @param equipmentTypeId device ID.
   */
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  @Override
  public String toString() {
    return "EquipmentAddNode [equipmentTypeId=" + equipmentTypeId + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type.
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (equipmentTypeId == null) {
      throw new CheckDataException();
    }
  }

}
