/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * AS Configuration
 */
public class As {

  /** AS Number */
  private String asNumber;

  /**
   * Getting AS number.
   *
   * @return AS number
   */
  public String getAsNumber() {
    return asNumber;
  }

  /**
   * Setting AS number.
   *
   * @param asNumber
   *          AS number
   */
  public void setAsNumber(String asNumber) {
    this.asNumber = asNumber;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "As [asNumber=" + asNumber + "]";
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
    if (asNumber == null) {
      throw new CheckDataException();
    }
  }

}
