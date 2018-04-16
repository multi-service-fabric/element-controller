/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
  * Inflow / Outflow Amount Controllability Rest Class.
  */
public class Shaping {

  /** Inflow / Outflow amount Controllability. */
  private Boolean enable = null;

  /**
   * Getting Inflow / Outflow Amount Controllability.
   * @return enable
   */
  public Boolean getEnable() {
    return enable;
  }

  /**
   * Setting Inflow / Outflow Amount Controllability.
   * @param enable setting enable
   */
  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  /*
   * Stringizing Instance
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Shaping [enable=" + enable + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param operationType
   *           operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {
    if (enable == null) {
      throw new CheckDataException();
    }
  }

}
