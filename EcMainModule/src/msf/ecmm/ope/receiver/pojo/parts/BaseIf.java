/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Base IF Information.
 */
public class BaseIf extends AbstractResponseMessage {

  /** IF Type. */
  private String ifType;

  /** IF ID. */
  private String ifId;

  /**
   * Getting IF type.
   *
   * @return IF type
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          IF type
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID.
   *
   * @return IF ID
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param ifId
   *          IF ID
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
    return "BaseIf [ifType=" + ifType + ", ifId=" + ifId + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (ifType == null) {
      throw new CheckDataException();
    }
    if (ifId == null) {
      throw new CheckDataException();
    }
  }
}
