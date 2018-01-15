/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Model IF Information.
 */
public class EquipmentIf {

  /** Physical IF ID. */
  private String physicalIfId;

  /** IF Slot Name. */
  private String ifSlot;

  /** Port Speed Type. */
  public ArrayList<String> portSpeedType = new ArrayList<String>();

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  /**
   * Setting physical IF ID.
   *
   * @param physicalIfId
   *          physical IF ID
   */
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  /**
   * Getting IF slot name.
   *
   * @return IF slot name
   */
  public String getIfSlot() {
    return ifSlot;
  }

  /**
   * Setting IF slot name.
   *
   * @param ifSlot
   *          IF slot name
   */
  public void setIfSlot(String ifSlot) {
    this.ifSlot = ifSlot;
  }

  /**
   * Getting port speed type.
   *
   * @return port speed type
   */
  public ArrayList<String> getPortSpeedType() {
    return portSpeedType;
  }

  /**
   * Setting port speed type.
   *
   * @param portSpeedType
   *          port speed type
   */
  public void setPortSpeedType(ArrayList<String> portSpeedType) {
    this.portSpeedType = portSpeedType;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "EquipmentIf [physicalIfId=" + physicalIfId + ", ifSlot=" + ifSlot + ", portSpeedType=" + portSpeedType
        + "]";
  }

  /**
   * Mandatory Item Check.
   *
   * @param operationType
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {
    if (physicalIfId == null) {
      throw new CheckDataException();
    }
    if (ifSlot == null) {
      throw new CheckDataException();
    }

    if (portSpeedType == null) {
      throw new CheckDataException();
    }
  }

}
