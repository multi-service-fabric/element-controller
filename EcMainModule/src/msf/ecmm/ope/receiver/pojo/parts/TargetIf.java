/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information of IF in which Inter-Cluster Link is to be generated/deleted.
 */
public class TargetIf {

  /** Device ID. */
  private String nodeId;

  /** IF Type (Physical IF/LAG IF/breakoutIF). */
  private String ifType;

  /** IF ID corresponding to IF Type. */
  private String ifId;

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

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
    return "TargetIf [nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + "]";
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

    if (nodeId == null) {
      throw new CheckDataException();
    }

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
  }
}
