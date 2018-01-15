/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Unused Physical IF.
 */
public class UnusePhysicalIf {

  @Override
  public String toString() {
    return "UnusePhysicalIf [physicalIfId=" + physicalIfId + "]";
  }

  /** Physical IFID. */
  String physicalIfId;

  /**
   * Getting physical IFID.
   *
   * @return physical IFID.
   */
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  /**
   * Setting physical IFID.
   *
   * @param physicalIfId
   *          physical IFID.
   */
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
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
    if (physicalIfId == null) {
      throw new CheckDataException();
    }
  }
}
