/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Tracking IF Information.
 */
public class TrackingIf {

  /** Tracking IF Type. */
  private String ifType = null;

  /** IF ID of Tracking IF. */
  private String ifId = null;

  /**
   * Getting Tracking IF type.
   *
   * @return Tracking IF type
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting Tracking IF type.
   *
   * @param ifType
   *          Tracking IF type
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID of Tracking IF.
   *
   * @return IF ID of Tracking IF
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID of Tracking IF.
   *
   * @param ifId
   *          IF ID of Tracking IF
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "[ifType=" + ifType + ", ifId=" + ifId + "]";
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

    if (ifType == null) {
      throw new CheckDataException();
    } else if (!ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF) && !ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)
        && !ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      throw new CheckDataException();
    }
    if (ifId == null) {
      throw new CheckDataException();
    }
  }

}
