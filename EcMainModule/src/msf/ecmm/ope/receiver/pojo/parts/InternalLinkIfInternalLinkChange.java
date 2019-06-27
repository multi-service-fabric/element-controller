/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Internal link IF change/ IF information to be generated/ Internal link IF information.
 */
public class InternalLinkIfInternalLinkChange {

  /** Type of internal link IF（Physical IF／LagIF／breakoutIF）. */
  private String ifType;

  /** IF ID which is corresponding to IF type. */
  private String ifId;

  /** Cost value. */
  private Integer cost;

  /**
   * Getting type of internal link IF.
   *
   * @return Type of internal link IF
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting type of Internal Link IF.
   *
   * @param ifType
   *          Type of internal link IF
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
   * Getting cost value.
   *
   * @return Cost value
   */
  public Integer getCost() {
    return cost;
  }

  /**
   * Setting Cost value.
   *
   * @param cost
   *          Cost value
   */
  public void setCost(Integer cost) {
    this.cost = cost;
  }

  /**
   * Stringizing instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "InternalLinkIfInternalLinkChange [ifType=" + ifType + ", ifId=" + ifId + ", cost=" + cost + "]";
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
    } else if (!(ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF))
        && !(ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF))
        && !(ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF))) {
      throw new CheckDataException();
    }

    if (ifId == null) {
      throw new CheckDataException();
    }

    if (cost == null) {
      throw new CheckDataException();
    }
  }
}
