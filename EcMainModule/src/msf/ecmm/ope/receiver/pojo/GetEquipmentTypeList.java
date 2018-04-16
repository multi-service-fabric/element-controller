/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.Equipment;

/**
 * Model List Information Acquisition
 */
public class GetEquipmentTypeList extends AbstractResponseMessage {

  /** Model Information */
  private ArrayList<Equipment> equipments = new ArrayList<Equipment>();

  /**
   * Getting model information.
   *
   * @return model information
   */
  public ArrayList<Equipment> getEquipment() {
    return equipments;
  }

  /**
   * Setting model information.
   *
   * @param equipments
   *          model information
   */
  public void setEquipment(ArrayList<Equipment> equipments) {
    this.equipments = equipments;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetEquipmentTypeList [equipments=" + equipments + "]";
  }

}
