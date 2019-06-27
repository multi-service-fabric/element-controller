/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Intenal-link(in case opposite node ID exists).
 */
public class InternalLinkOppo {

  /** internal link type. */
  private String ifType;

  /** Internal link IF ID. */
  private String ifId;

  /** Opposite node ID. */
  private String oppositeNodeId;

  /**
   * Getting internal link type.
   *
   * @return internal link type.
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting internal link type.
   *
   * @param ifType
   *          internal link type.
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting internal link IF ID.
   *
   * @return internal link IF ID.
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting internal link IF ID.
   *
   * @param ifId
   *          internal link IF ID.
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Getting opposite node ID.
   *
   * @return opposite node ID
   */
  public String getOppositeNodeId() {
    return oppositeNodeId;
  }

  /**
   * Setting opposite node ID.
   *
   * @param oppositeNodeId
   *          opposite node ID
   */
  public void setOppositeNodeId(String oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  /*
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InternalLinkOppo [ifType=" + ifType + ", ifId=" + ifId + ", oppositeNodeId=" + oppositeNodeId + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (ifType == null) {
      throw new CheckDataException();
    }
    if (!ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF) && !ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)
        && !ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      throw new CheckDataException();
    }

    if (ifId == null) {
      throw new CheckDataException();
    }

    if (oppositeNodeId == null) {
      throw new CheckDataException();
    }
  }
}
