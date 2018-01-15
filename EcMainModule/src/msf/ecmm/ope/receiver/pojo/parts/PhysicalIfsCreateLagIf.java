/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * LagIF Composing IF Information.
 *
 */
public class PhysicalIfsCreateLagIf {

  /** IF Type. */
  private String ifType;

  /** IF ID. */
  private String ifId;

  /**
   * Getting IF type.
   *
   * @return ifType
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          set ifType
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID.
   *
   * @return ifId
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param ifId
   *          set ifId
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
    return "PhysicalIfsCreateLagIf [ifType=" + ifType + ", ifId=" + ifId + "]";
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
    } else if (!(ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF))
        && !(ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF))) {
      throw new CheckDataException();
    }

    if (ifId == null) {
      throw new CheckDataException();
    }
  }

}
