/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.Equipment;

/**
 * Model Information Acquisition
 */
public class GetEquipmentType extends AbstractResponseMessage {

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
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetEquipmentType [equipment=" + equipment + "]";
  }

}
